package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.ProgramaModel;

public interface IProgramaRepository extends JpaRepository<ProgramaModel, String>{
    //consultas
}
