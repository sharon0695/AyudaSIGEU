package com.gestion.eventos.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "Facultad")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultadModel {
    @Id
    private String id;
    private String nombre;
    private String decano;
}
