package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.ReservacionModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReservacionRepository extends JpaRepository<ReservacionModel, Integer>{
    List<ReservacionModel> findAllByCodigoEvento_Codigo(Integer codigo);    
}
