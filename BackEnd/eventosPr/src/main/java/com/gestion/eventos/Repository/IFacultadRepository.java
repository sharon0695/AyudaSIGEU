package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.FacultadModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFacultadRepository extends JpaRepository<FacultadModel, String>{
    //consultas
}
