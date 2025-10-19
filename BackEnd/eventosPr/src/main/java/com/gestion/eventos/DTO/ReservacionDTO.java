package com.gestion.eventos.DTO;

import java.sql.Time;

public class ReservacionDTO {
    private String codigoEspacio;
    private Time horaInicio;
    private Time horaFin;

    public ReservacionDTO() {}

    public ReservacionDTO(String codigoEspacio, Time horaInicio, Time horaFin) {
        this.codigoEspacio = codigoEspacio;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getCodigoEspacio() {
        return codigoEspacio;
    }

    public void setCodigoEspacio(String codigoEspacio) {
        this.codigoEspacio = codigoEspacio;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }
}
