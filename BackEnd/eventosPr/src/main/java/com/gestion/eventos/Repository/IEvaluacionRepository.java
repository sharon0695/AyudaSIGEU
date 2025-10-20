package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.EvaluacionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEvaluacionRepository extends JpaRepository<EvaluacionModel, Integer>{
    //consultas
}
