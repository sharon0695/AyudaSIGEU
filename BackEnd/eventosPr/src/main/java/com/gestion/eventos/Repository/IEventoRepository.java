package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.EventoModel;

public interface IEventoRepository extends JpaRepository<EventoModel, Integer>{
    //consultas
}
