package com.gestion.eventos.DTO;

import lombok.Data;
@Data
public class UsuarioUpdateRequest {
    private Integer identificacion;   
    private String contrasena;    
    private String celular;             
}