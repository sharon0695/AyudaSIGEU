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
@Table (name ="Organizacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizacionModel {
    @Id
    private String nit;
    private String nombre;
    private String representante_legal;
    private String ubicacion;
    private String telefono;
    private String sector_economico;
    private String actividad_principal;
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "identificacion", nullable = false)
    private UsuarioModel usuario;
}