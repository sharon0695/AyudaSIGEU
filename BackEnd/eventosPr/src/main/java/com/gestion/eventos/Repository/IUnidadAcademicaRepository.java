package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.UnidadAcademicaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUnidadAcademicaRepository extends JpaRepository<UnidadAcademicaModel, String>{
    //consultas
}
