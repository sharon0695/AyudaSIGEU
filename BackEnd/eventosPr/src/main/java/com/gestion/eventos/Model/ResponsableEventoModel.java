package com.gestion.eventos.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Responsable_evento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsableEventoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer consecutivo;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioModel id_usuario;
    
    @ManyToOne
    @JoinColumn(name = "codigo_evento")
    private EventoModel codigoEvento;
    
    @Column(name = "documentoAval")
    private String documentoAval;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_aval")
    private tipo_aval tipoAval;
    
    public enum tipo_aval {
        director_programa, 
        director_docencia
    }
}