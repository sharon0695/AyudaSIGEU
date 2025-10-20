package com.gestion.eventos.Service;

import com.gestion.eventos.DTO.LoginRequest;
import com.gestion.eventos.DTO.LoginResponse;
import com.gestion.eventos.DTO.MensajeResponse;
import com.gestion.eventos.DTO.UsuarioRegistroRequest;
import com.gestion.eventos.Model.UsuarioModel;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IUsuarioService {
    MensajeResponse guardarUsuario(UsuarioRegistroRequest request);
    LoginResponse login(LoginRequest request);
    List<UsuarioModel> listarUsuario();
    void enviarCredencialesPorCorreo(String correo);
    UsuarioModel actualizarPerfil(Integer identificacion, String contrasenaActual, String nuevaContrasena, String celular, MultipartFile fotoPerfil) throws IOException;
    ResponseEntity<Resource> obtenerFoto(Integer id) throws IOException;
    void logout(String authHeader);
}
    