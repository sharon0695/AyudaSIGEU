package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.ResponsableEventoModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IResponsableEventoRepository extends JpaRepository<ResponsableEventoModel, Integer>{  
    @Query("SELECT COALESCE(MAX(r.consecutivo), 0) FROM ResponsableEventoModel r")
    Integer findMaxConsecutivo();
    List<ResponsableEventoModel> findAllByCodigoEvento_Codigo(Integer codigo);
    void deleteByCodigoEvento_Codigo(Integer codigo);
}
