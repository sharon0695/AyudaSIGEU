package com.gestion.eventos.DTO;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import lombok.Data;

@Data
public class EventoRegistroCompleto {
    private String nombre;
    private String descripcion;
    private String tipo;
    private Date fecha;
    private Time hora_inicio;
    private Time hora_fin;
    private Integer id_usuario_registra; 
    private List<OrganizacionDTO> organizaciones;
    private List<ResponsableDTO> responsables;    
    private List<ReservacionDTO> reservaciones;
    
    @Data
    public static class OrganizacionDTO {
        private String nit;
        private String nombre;
        private String representante_legal;
        private String ubicacion;
        private String telefono;
        private String sector_economico;
        private String actividad_principal;
        private String certificado_participacion;
        private String representante_alterno;
    }
    
    @Data
    public static class ResponsableDTO {
        private Integer id_usuario;
        private String documentoAval;
        private String tipoAval; 
    }
    
    @Data
    public static class ReservacionDTO {
        private String codigo_espacio;
        private Time hora_inicio;
        private Time hora_fin;
    }
}