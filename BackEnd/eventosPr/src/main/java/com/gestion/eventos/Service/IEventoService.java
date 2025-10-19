package com.gestion.eventos.Service;

import com.gestion.eventos.DTO.EventoRegistroCompleto;
import com.gestion.eventos.Model.EventoModel;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface IEventoService {
    List<EventoModel> listarEventos();
    EventoModel registrarEventoCompleto(EventoRegistroCompleto request);
    EventoModel actualizarEvento(Integer codigo, EventoModel cambios);
    void reemplazarOrganizaciones(Integer codigo, List<String> organizaciones, List<String> alternos, List<MultipartFile> avales);
    void reemplazarResponsables(Integer codigo, List<Integer> responsables, List<MultipartFile> avales);
}