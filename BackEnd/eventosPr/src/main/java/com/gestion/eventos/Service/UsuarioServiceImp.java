package com.gestion.eventos.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gestion.eventos.DTO.LoginRequest;
import com.gestion.eventos.DTO.LoginResponse;
import com.gestion.eventos.DTO.MensajeResponse;
import com.gestion.eventos.DTO.UsuarioRegistroRequest;
import com.gestion.eventos.Model.UsuarioModel;
import com.gestion.eventos.Repository.IUsuarioRepository;
import com.gestion.eventos.Security.JwtUtil;
import com.gestion.eventos.Security.TokenBlacklistService;

@Service
public class UsuarioServiceImp implements IUsuarioService {
    
    @Autowired IUsuarioRepository usuarioRepository;  
    @Autowired JavaMailSender mailSender;
    @Autowired JwtUtil jwtUtil;
    @Autowired TokenBlacklistService tokenBlacklistService;
   
    @Override
    public MensajeResponse guardarUsuario(UsuarioRegistroRequest request) {
        // Validación de campos obligatorios
         if(request.getIdentificacion()==null || request.getNombre()==null || request.getApellido()==null ||
        request.getCorreoInstitucional()==null || request.getContrasena()==null || request.getRol()==null){
            throw new RuntimeException("Hay campos obligatorios vacíos");
        }
        //Validar correo institucional
        if(!request.getCorreoInstitucional().endsWith("@uao.edu.co")){
            throw new RuntimeException("Solo se permite el uso del correo institucional");
        }
        // Validación de usuario único
        if(usuarioRepository.findByIdentificacion(request.getIdentificacion()).isPresent()){
            throw new RuntimeException("Ya existe un usuario con ese número de identificación");
        }
        if(usuarioRepository.findByCorreoInstitucional(request.getCorreoInstitucional()).isPresent()){
            throw new RuntimeException("Ya existe un usuario registrado con ese correo");
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setIdentificacion(request.getIdentificacion());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreoInstitucional(request.getCorreoInstitucional());
        usuario.setContrasena(request.getContrasena());
        usuario.setRol(UsuarioModel.rol.valueOf(request.getRol())); // Enum

        //Campos opcionales según el rol
        switch (usuario.getRol()) {
            case estudiante -> {
                if(request.getCodigo()==null || request.getCodigoPrograma()==null || request.getCodigoPrograma().isBlank()){
                    throw new RuntimeException("El estudiante debe llenar su código y código del programa");
                }
            }
            case docente -> {
                if(request.getCodigoUnidad()==null || request.getCodigoUnidad().isBlank()){
                    throw new RuntimeException("El docente debe estar asociado a una unidad académica");
                }
            }
            case secretaria_academica -> {
                if(request.getIdFacultad()==null || request.getIdFacultad().isBlank()){
                    throw new RuntimeException("La secretaría debe tener una facultad asociada");
                }
            }
            default -> {
            }
        }
        usuarioRepository.save(usuario);
        return new MensajeResponse("Usuario creado con éxito"); 
}
 

    @Override
    public List<UsuarioModel> listarUsuario() {
        return usuarioRepository.findAll();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String correo = request.getCorreoInstitucional();
        String contrasena = request.getContrasena();

        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreoInstitucional(correo);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        UsuarioModel usuario = usuarioOpt.get();

        if (!contrasena.equals(usuario.getContrasena())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(usuario);

        return new LoginResponse(
            token,
            "Bearer",
            jwtUtil.getExpirationMillis(),
            usuario.getIdentificacion(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getCorreoInstitucional(),
            usuario.getRol().name()
        );
    }

    @Override
    public void enviarCredencialesPorCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreoInstitucional(correo);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario con ese correo");
        }

        UsuarioModel usuario = usuarioOpt.get();
        String contrasena = usuario.getContrasena();

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correo);
        mensaje.setSubject("Recuperación de credenciales");
        mensaje.setText("Hola " + usuario.getNombre() + 
                        ",\n\nTu contraseña registrada en el sistema es: " + contrasena +
                        "\n\nPor seguridad, te recomendamos cambiarla después de iniciar sesión.");

        
                        
        mailSender.send(mensaje);
    }
    @Override
    public UsuarioModel actualizarPerfil(Integer identificacion, String contrasena, String celular, MultipartFile fotoPerfil) throws IOException {
        UsuarioModel usuario = usuarioRepository.findById(identificacion)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar contraseña
        if (contrasena != null && !contrasena.trim().isEmpty()) {
            usuario.setContrasena(contrasena);
        }

        // Validar y actualizar celular (solo números y 10 dígitos)
        if (celular != null && !celular.trim().isEmpty()) {
            if (!celular.matches("\\d{10}")) {
                throw new RuntimeException("El número de celular debe contener exactamente 10 dígitos");
            }
            usuario.setCelular(celular);
        }

        // Guardar imagen si viene
        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String contentType = fotoPerfil.getContentType();
            if (contentType == null || !contentType.equals("image/png")) {
                throw new RuntimeException("Solo se permiten imágenes PNG");
            }

            String directorio = "src/main/resources/static/uploads/perfiles/";
            String nombreArchivo = usuario.getIdentificacion() + "_" + fotoPerfil.getOriginalFilename();
            Path rutaArchivo = Paths.get(directorio, nombreArchivo);
            Files.createDirectories(rutaArchivo.getParent());
            fotoPerfil.transferTo(rutaArchivo.toFile());

            usuario.setFotoPerfil("/uploads/perfiles/" + nombreArchivo);
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public ResponseEntity<Resource> obtenerFoto(Integer id) throws IOException {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getFotoPerfil() == null) {
            return ResponseEntity.notFound().build();
        }

        Path ruta = Paths.get("src/main/resources/static" + usuario.getFotoPerfil());
        Resource recurso = new UrlResource(ruta.toUri());

        if (!recurso.exists() || !recurso.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(recurso);
    }

    @Override
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklist(token);
        }
    }

}






