package com.gestion.eventos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventoRegistroResponse {
    private String mensaje;
    private Integer codigoEvento;
}