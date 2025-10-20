package com.gestion.eventos.Repository;

import com.gestion.eventos.Model.OrganizacionModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizacionRepository extends JpaRepository<OrganizacionModel, String> {
    Optional<OrganizacionModel> findByNombre(String nombre);
    Optional<OrganizacionModel> findByNit(String nit);
}
