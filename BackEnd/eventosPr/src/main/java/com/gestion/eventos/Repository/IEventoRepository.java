package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.EventoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEventoRepository extends JpaRepository<EventoModel, Integer>{    
    long countByNitOrganizacion(String nitOrganizacion);
}
