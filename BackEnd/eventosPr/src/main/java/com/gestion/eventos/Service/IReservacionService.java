package com.gestion.eventos.Service;

import com.gestion.eventos.Model.ReservacionModel;
import java.util.List;

public interface IReservacionService {
    ReservacionModel crearReservacion(ReservacionModel reservacion);
    List<ReservacionModel> listarReservaciones();
}
