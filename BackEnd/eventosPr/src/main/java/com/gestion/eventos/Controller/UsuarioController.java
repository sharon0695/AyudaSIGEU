package com.gestion.eventos.Controller;

import com.gestion.eventos.DTO.LoginRequest;
import com.gestion.eventos.DTO.LoginResponse;
import com.gestion.eventos.DTO.MensajeResponse;
import com.gestion.eventos.DTO.UsuarioRegistroRequest;
import com.gestion.eventos.Model.UsuarioModel;
import com.gestion.eventos.Service.IUsuarioService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping ("/usuarios")
public class UsuarioController {
    @Autowired IUsuarioService usuarioService;
    
    @PostMapping ("/registrar")
    public ResponseEntity<MensajeResponse> crearUsuario(@RequestBody UsuarioRegistroRequest usuarioRequest){
        MensajeResponse response = usuarioService.guardarUsuario(usuarioRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    } 
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        usuarioService.logout(authHeader);
        return ResponseEntity.noContent().build(); // 204 sin contenido
    }
    
    @GetMapping ("/listar")
    public ResponseEntity<List<UsuarioModel>> listarUsuarios(){
        return new ResponseEntity<>(usuarioService.listarUsuario(), HttpStatus.OK);
    }

    @PostMapping("/recuperar")
    public ResponseEntity<String> recuperarContrasena(@RequestParam String correo) {
        usuarioService.enviarCredencialesPorCorreo(correo);
        return ResponseEntity.ok("Se ha enviado la contraseña al correo registrado");
    }

    @PutMapping(value = "/editarPerfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioModel> editarPerfil(
            @RequestParam Integer identificacion,
            @RequestParam(required = false) String contrasena,
            @RequestParam(required = false) String celular,
            @RequestParam(required = false) MultipartFile fotoPerfil
    ) throws IOException {
        UsuarioModel actualizado = usuarioService.actualizarPerfil(identificacion, contrasena, celular, fotoPerfil);
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/foto/{id}")
    public ResponseEntity<Resource> obtenerFoto(@PathVariable Integer id) throws IOException {
        return usuarioService.obtenerFoto(id);
    }
}   
