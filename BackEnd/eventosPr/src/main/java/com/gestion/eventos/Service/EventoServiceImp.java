package com.gestion.eventos.Service;

import com.gestion.eventos.Model.EventoModel;
import com.gestion.eventos.Repository.IEventoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class EventoServiceImp implements IEventoService{
    @Autowired IEventoRepository eventoRepository;

    @Override
    public EventoModel registrarEvento(EventoModel evento) {
        try {
            // Validar campos obligatorios 
            validarCamposEvento(evento);
            
            // Establecer estado inicial como borrador
            evento.setEstado(EventoModel.estado.valueOf("borrador"));


            return eventoRepository.save(evento);

        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el evento: " + e.getMessage(), e);
        }
    }

    private void validarCamposEvento(EventoModel evento) {
        if (evento.getNombre() == null || evento.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio");
        }
        if (evento.getDescripcion() == null || evento.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del evento es obligatoria");
        }
        if (evento.getTipo() == null || evento.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del evento es obligatorio");
        }
        if (evento.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del evento es obligatoria");
        }
        if (evento.getHora_inicio() == null) {
            throw new IllegalArgumentException("La hora de inicio del evento es obligatoria");
        }
        if (evento.getHora_fin() == null) {
            throw new IllegalArgumentException("La hora de fin del evento es obligatoria");
        }
        if (evento.getCodigo_lugar() == null || evento.getCodigo_lugar().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del lugar es obligatorio");
        }
    }
    @Override
    public List<EventoModel> listarEventos() {
        return eventoRepository.findAll();    
    }

    public Optional<EventoModel> buscarPorCodigo(Integer codigo) {
        return eventoRepository.findById(codigo);
    }
    
}
