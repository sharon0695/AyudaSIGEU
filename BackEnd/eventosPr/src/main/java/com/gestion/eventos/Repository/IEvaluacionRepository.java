package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.EvaluacionModel;

public interface IEvaluacionRepository extends JpaRepository<EvaluacionModel, Integer>{
    //consultas
}
