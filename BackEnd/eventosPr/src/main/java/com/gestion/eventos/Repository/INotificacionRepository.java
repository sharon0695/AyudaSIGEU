package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.NotificacionModel;

public interface INotificacionRepository extends JpaRepository<NotificacionModel, Integer>{
    //consultas
}
