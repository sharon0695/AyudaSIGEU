package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.ReservacionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservacionRepository extends JpaRepository<ReservacionModel, Integer>{
    //consultas
}
