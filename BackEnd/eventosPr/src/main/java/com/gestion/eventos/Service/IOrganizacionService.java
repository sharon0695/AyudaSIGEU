package com.gestion.eventos.Service;

import java.util.List;
import java.util.Optional;

import com.gestion.eventos.Model.OrganizacionModel;

public interface IOrganizacionService {
    OrganizacionModel guardarOrganizacion(OrganizacionModel organizacion);
    List<OrganizacionModel> listarOrganizaciones();
    String buscarOrganizacionPorNombre(String nombre);
    OrganizacionModel editarOrganizacion(String nit, Integer idUsuarioEditor, OrganizacionModel organizacionActualizada);
    Optional<OrganizacionModel> obtenerOrganizacionPorNit(String nit);
    void eliminarOrganizacion(String nit, Integer solicitanteId);
}
