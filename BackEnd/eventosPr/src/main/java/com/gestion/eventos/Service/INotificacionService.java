package com.gestion.eventos.Service;

import com.gestion.eventos.Model.NotificacionModel;
import java.util.List;

public interface INotificacionService {
    NotificacionModel guardarNotificacion(NotificacionModel notificacion);
    List<NotificacionModel> listarNotificaciones();
}
