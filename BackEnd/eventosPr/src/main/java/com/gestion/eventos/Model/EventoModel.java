package com.gestion.eventos.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;
import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table (name ="Evento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;
    private String nombre;
    private String descripcion;
    private String tipo;
    private Date fecha;
    private Time hora_inicio;
    private Time hora_fin;
    public enum estado{aprobado, rechazado, borrador, enviado, publicado}
    @Enumerated(EnumType.STRING)
    private estado estado;
    private String codigo_lugar;
    @Column(name = "nit_organizacion")
    private String nitOrganizacion;
}
