package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.EspacioModel;

public interface IEspacioRepository extends JpaRepository<EspacioModel, String>{
 //consultas
}
