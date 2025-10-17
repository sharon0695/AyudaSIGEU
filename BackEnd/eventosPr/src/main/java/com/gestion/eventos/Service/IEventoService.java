package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.EventoModel;

public interface IEventoService {
    List<EventoModel> listarEventos();
    EventoModel registrarEvento(EventoModel evento);
}
