package com.gestion.eventos.Service;

import com.gestion.eventos.Model.OrganizacionModel;
import com.gestion.eventos.Repository.IEventoRepository;
import com.gestion.eventos.Repository.IOrganizacionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class OrganizacionServiceImpl implements IOrganizacionService {

    @Autowired IOrganizacionRepository organizacionRepository;
    @Autowired IEventoRepository eventoRepository;

    @Override
    public OrganizacionModel guardarOrganizacion(OrganizacionModel organizacion) {
        //Campos obligatorios
        if(organizacion.getNit()==null || organizacion.getNombre()==null || organizacion.getRepresentante_legal()==null ||
        organizacion.getUbicacion()==null || organizacion.getSector_economico()==null || organizacion.getActividad_principal()==null){
            throw new RuntimeException("Hay campos obligatorios vacíos");
        }
        if (organizacion.getTelefono() == null || !organizacion.getTelefono().matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono solo debe contener números y no puede estar vacío");
        }
        if(organizacionRepository.findByNit(organizacion.getNit()).isPresent()){
            throw new RuntimeException("Ya existe una organización con ese NIT");
        }
        return organizacionRepository.save(organizacion);
    }

    @Override
    public List<OrganizacionModel> listarOrganizaciones() {
        return organizacionRepository.findAll();
    }

    @Override
    public OrganizacionModel editarOrganizacion(String nit, Integer idUsuarioEditor, OrganizacionModel organizacionActualizada) {
        Optional<OrganizacionModel> organizacionExistenteOpt = organizacionRepository.findByNit(nit);

        if (organizacionExistenteOpt.isEmpty()) {
            throw new NoSuchElementException("Organización no encontrada");
        }

        OrganizacionModel organizacionExistente = organizacionExistenteOpt.get();

        if (organizacionExistente.getUsuario() == null ||
            !organizacionExistente.getUsuario().getIdentificacion().equals(idUsuarioEditor)) {
            throw new RuntimeException("No tiene permisos para editar esta organización");
        }

        if (organizacionActualizada.getNombre() != null) {
            organizacionExistente.setNombre(organizacionActualizada.getNombre());
        }
        if (organizacionActualizada.getRepresentante_legal() != null) {
            organizacionExistente.setRepresentante_legal(organizacionActualizada.getRepresentante_legal());
        }
        if (organizacionActualizada.getUbicacion() != null) {
            organizacionExistente.setUbicacion(organizacionActualizada.getUbicacion());
        }
        if (organizacionActualizada.getTelefono() != null) {
            if (!organizacionActualizada.getTelefono().matches("\\d+")) {
                throw new IllegalArgumentException("El teléfono solo debe contener números");
            }
            organizacionExistente.setTelefono(organizacionActualizada.getTelefono());
        }
        if (organizacionActualizada.getSector_economico() != null) {
            organizacionExistente.setSector_economico(organizacionActualizada.getSector_economico());
        }
        if (organizacionActualizada.getActividad_principal() != null) {
            organizacionExistente.setActividad_principal(organizacionActualizada.getActividad_principal());
        }

        return organizacionRepository.save(organizacionExistente);
    }
     @Override
    public String buscarOrganizacionPorNombre(String nombre) {
        Optional<OrganizacionModel> organizacionOpt = organizacionRepository.findByNombre(nombre);
        
        if (organizacionOpt.isPresent()) {
            OrganizacionModel organizacion = organizacionOpt.get();
            
            // Construimos la información detallada
            StringBuilder info = new StringBuilder();
            info.append(" Organización encontrada:\n");
            info.append(" Nombre: ").append(organizacion.getNombre()).append("\n");
            info.append(" Representante legal: ").append(organizacion.getRepresentante_legal()).append("\n");
            info.append(" Ubicación: ").append(organizacion.getUbicacion()).append("\n");
            info.append(" Actividad principal: ").append(organizacion.getActividad_principal()).append("\n");
            info.append(" Teléfono: ").append(organizacion.getTelefono());
            
            return info.toString();
        } else {
            return "La organización '" + nombre + "' no existe en el sistema.";
        }
    }
    @Override
    public Optional<OrganizacionModel> obtenerOrganizacionPorNit(String nit) {
        return organizacionRepository.findByNit(nit);
    }

    @Override
    public void eliminarOrganizacion(String nit, Integer solicitanteId) {
    OrganizacionModel org = organizacionRepository.findByNit(nit)
        .orElseThrow(() -> new NoSuchElementException("Organización no encontrada"));
    if (org.getUsuario() == null || !org.getUsuario().getIdentificacion().equals(solicitanteId)) {
        throw new RuntimeException("No tiene permisos para eliminar esta organización");
    }
    long asociados = eventoRepository.countByNitOrganizacion(nit);
    if (asociados > 0) {
        throw new DataIntegrityViolationException("La organización no puede eliminarse porque está asociada a eventos");
    }
    organizacionRepository.delete(org);
}
}
