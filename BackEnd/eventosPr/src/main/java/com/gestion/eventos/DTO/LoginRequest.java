package com.gestion.eventos.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String correoInstitucional;
    private String contrasena;

    public String getContrasena() {
        return contrasena;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }
}