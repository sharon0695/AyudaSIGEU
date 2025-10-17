package com.gestion.eventos.Model;

import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table (name ="Reservacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservacionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn (name="codigo_evento")
    private EventoModel codigo_evento;
    @ManyToOne
    @JoinColumn (name="codigo_espacio")
    private EspacioModel codigo_espacio;
    private Time hora_inicio;
    private Time hora_fin;
}
