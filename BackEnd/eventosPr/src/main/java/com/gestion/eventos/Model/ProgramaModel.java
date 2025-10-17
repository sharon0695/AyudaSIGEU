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
@Table (name ="Programa_academico")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaModel {
    @Id
    private String codigo;
    private String nombre;
    private String director;
    @ManyToOne
    @JoinColumn (name="id_facultad")
    private FacultadModel id_facultad;
}
