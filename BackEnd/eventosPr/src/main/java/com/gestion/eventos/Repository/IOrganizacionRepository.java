package com.gestion.eventos.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.eventos.Model.OrganizacionModel;

@Repository
public interface IOrganizacionRepository extends JpaRepository<OrganizacionModel, String> {
    Optional<OrganizacionModel> findByNombre(String nombre);
    Optional<OrganizacionModel> findByNit(String nit);
}
