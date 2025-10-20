package com.gestion.eventos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class EventoOrganizacionResponse { 
    private String nit; 
    private String representanteAlterno; 
    private String certificadoUrl; 
}
