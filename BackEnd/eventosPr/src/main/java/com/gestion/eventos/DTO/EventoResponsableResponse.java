package com.gestion.eventos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class EventoResponsableResponse { 
    private Integer idUsuario; 
    private String documentoAvalUrl; 
}
