package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.UnidadAcademicaModel;

public interface IUnidadAcademicaRepository extends JpaRepository<UnidadAcademicaModel, String>{
    //consultas
}
