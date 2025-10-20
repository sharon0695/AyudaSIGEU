package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.ProgramaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProgramaRepository extends JpaRepository<ProgramaModel, String>{
    //consultas
}
