package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.eventos.Model.NotificacionModel;
import com.gestion.eventos.Repository.INotificacionRepository;
@Service
public class NotificacionServiceImp implements INotificacionService{
    @Autowired INotificacionRepository notificacionRepository;

    @Override
    public NotificacionModel guardarNotificacion(NotificacionModel notificacion) {
        return notificacionRepository.save(notificacion);    
    }

    @Override
    public List<NotificacionModel> listarNotificaciones() {
        return notificacionRepository.findAll();    
    }

}
