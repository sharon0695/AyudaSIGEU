package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.gestion.eventos.DTO.LoginRequest;
import com.gestion.eventos.DTO.LoginResponse;
import com.gestion.eventos.DTO.MensajeResponse;
import com.gestion.eventos.DTO.UsuarioRegistroRequest;
import com.gestion.eventos.Model.UsuarioModel;
import org.springframework.core.io.Resource;
import java.io.IOException;

public interface IUsuarioService {
    MensajeResponse guardarUsuario(UsuarioRegistroRequest request);
    LoginResponse login(LoginRequest request);
    List<UsuarioModel> listarUsuario();
    void enviarCredencialesPorCorreo(String correo);
    UsuarioModel actualizarPerfil(Integer identificacion, String contrasena, String celular, MultipartFile fotoPerfil) throws IOException;
    ResponseEntity<Resource> obtenerFoto(Integer id) throws IOException;
    void logout(String authHeader);
}
    