package com.gestion.eventos.DTO;

import lombok.Data;

@Data
public class UsuarioRegistroRequest{
    private Integer identificacion;
    private String nombre;
    private String apellido;
    private String correoInstitucional;
    private String contrasena;
    private String rol;
    private Integer codigo; 
    private String codigoPrograma; 
    private String codigoUnidad;   
    private String idFacultad; 
}
