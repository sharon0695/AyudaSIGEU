package com.gestion.eventos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class EventoReservacionResponse {
  private String codigoEspacio;
  private String horaInicio;
  private String horaFin;
}
