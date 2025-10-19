package com.gestion.eventos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gestion.eventos.Model.ResponsableEventoModel;

public interface IResponsableEventoRepository extends JpaRepository<ResponsableEventoModel, Integer>{
  
    @Query("SELECT COALESCE(MAX(r.consecutivo), 0) FROM ResponsableEventoModel r")
    Integer findMaxConsecutivo();
}
