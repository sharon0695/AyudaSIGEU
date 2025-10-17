package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.eventos.Model.ReservacionModel;
import com.gestion.eventos.Repository.IEventoRepository;
import com.gestion.eventos.Repository.IReservacionRepository;
@Service
public class ReservacionServiceImp implements IReservacionService{
    @Autowired IReservacionRepository reservacionRepository;

    @Autowired IEventoRepository eventoRepository;

    public ReservacionModel crearReservacion(ReservacionModel reservacion) {
        if (reservacion.getCodigo_evento() == null) {
            throw new IllegalArgumentException("Debe asociar la reservación a un evento existente.");
        }

        if (reservacion.getCodigo_espacio() == null) {
            throw new IllegalArgumentException("Debe seleccionar un espacio para la reservación.");
        }

        if (reservacion.getHora_inicio() == null) {
            throw new IllegalArgumentException("Debe seleccionar una hora de inicio para la reservación.");
        }

        if (reservacion.getHora_fin() == null) {
            throw new IllegalArgumentException("Debe seleccionar una hora de fin para la reservación.");
        }

        eventoRepository.findById(reservacion.getCodigo_evento().getCodigo())
            .orElseThrow(() -> new IllegalArgumentException("El evento asociado no existe."));

        return reservacionRepository.save(reservacion);
    }

    @Override
    public List<ReservacionModel> listarReservaciones() {
        return reservacionRepository.findAll();    
    }

}
