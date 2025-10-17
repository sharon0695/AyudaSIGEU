package com.gestion.eventos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private long expiresInMillis;
    private Integer identificacion;
    private String nombre;
    private String apellido;
    private String correoInstitucional;
    private String rol;
}
