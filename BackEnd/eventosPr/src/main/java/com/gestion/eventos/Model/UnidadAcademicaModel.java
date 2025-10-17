package com.gestion.eventos.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table (name ="Unidad_academica")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadAcademicaModel {
    @Id
    private String codigo;
    private String nombre;
    private String tipo;
    @ManyToOne
    @JoinColumn (name="id_facultad")
    private FacultadModel id_facultad;
}
