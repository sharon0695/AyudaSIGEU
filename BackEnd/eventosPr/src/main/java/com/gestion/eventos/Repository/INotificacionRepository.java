package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.NotificacionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificacionRepository extends JpaRepository<NotificacionModel, Integer>{
    //consultas
}
