package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.ColaboracionModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColaboracionRepository extends JpaRepository<ColaboracionModel, Integer>{
    List<ColaboracionModel> findAllByCodigoEvento_Codigo(Integer codigo);
    void deleteByCodigoEvento_Codigo(Integer codigo);
    long countByNitOrganizacion_Nit(String nitOrganizacion);
}
