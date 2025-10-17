package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.NotificacionModel;

public interface INotificacionService {
    NotificacionModel guardarNotificacion(NotificacionModel notificacion);
    List<NotificacionModel> listarNotificaciones();
}
