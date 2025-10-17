package com.gestion.eventos.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table (name ="Usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModel {
    @Id
    private Integer identificacion;
    private Integer codigo;
    private String nombre;
    private String apellido;
    @Column(name = "correo_institucional")
    private String correoInstitucional;
    private String contrasena;
    public enum rol{estudiante, docente, secretaria_academica, administrador}
    @Enumerated(EnumType.STRING)
    private rol rol;
    private String celular;
    @ManyToOne
    @JoinColumn (name="id_facultad")
    private FacultadModel id_facultad;
    @ManyToOne
    @JoinColumn (name="codigo_programa")
    private ProgramaModel codigo_programa;
    @ManyToOne
    @JoinColumn (name="codigo_unidad")
    private UnidadAcademicaModel codigo_unidad;
    @Column(name = "foto_perfil", nullable = true)
    private String fotoPerfil;
}
