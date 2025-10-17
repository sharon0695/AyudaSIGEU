package com.gestion.eventos.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table (name ="Espacio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspacioModel {
    @Id
    private String codigo;
    private String nombre;
    private Integer capacidad;
    private String tipo;
}
