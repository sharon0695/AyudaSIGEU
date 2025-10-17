package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestion.eventos.Model.ReservacionModel;

public interface IReservacionRepository extends JpaRepository<ReservacionModel, Integer>{
    //consultas
}
