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
@Table (name = "Evaluacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionModel {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn (name="codigo_evento")
    private EventoModel codigo_evento;
    @ManyToOne
    @JoinColumn (name="id_secretaria")
    private UsuarioModel id_secreAcad;
    private String observaciones;
    private String acta_comite;
}
