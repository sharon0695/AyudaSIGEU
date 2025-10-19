package com.gestion.eventos.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table (name ="Colaboracion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColaboracionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn (name="nit_organizacion")
    private OrganizacionModel nit_organizacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="codigo_evento")
    private EventoModel codigoEvento;
    private String certificado_participacion;
    private String representante_alterno;
}
