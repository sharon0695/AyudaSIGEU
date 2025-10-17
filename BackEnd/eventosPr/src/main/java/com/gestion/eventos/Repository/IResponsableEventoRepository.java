package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.ResponsableEventoModel;

public interface IResponsableEventoRepository extends JpaRepository<ResponsableEventoModel, Integer>{
    //consultas
}
