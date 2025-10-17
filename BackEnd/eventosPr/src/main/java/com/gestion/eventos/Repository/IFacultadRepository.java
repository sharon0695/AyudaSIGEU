package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.FacultadModel;

public interface IFacultadRepository extends JpaRepository<FacultadModel, String>{
    //consultas
}
