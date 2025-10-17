package com.gestion.eventos.Model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table (name ="Notificacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionModel {
    @Id
    private Integer id;
    private Integer remitente;
    @ManyToOne
    @JoinColumn (name="id_destinatario")
    private UsuarioModel destinatario;
    private Time hora;
    private Date fecha;
    private String detalles;
}
