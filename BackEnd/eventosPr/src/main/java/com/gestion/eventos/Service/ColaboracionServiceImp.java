package com.gestion.eventos.Service;

import com.gestion.eventos.Model.ColaboracionModel;
import com.gestion.eventos.Repository.IColaboracionRepository;
import com.gestion.eventos.Repository.IEventoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ColaboracionServiceImp implements IColaboracionService{
    @Autowired IColaboracionRepository colaboracionRepository;

    @Autowired IEventoRepository eventoRepository;

    public ColaboracionModel crearColaboracion(ColaboracionModel colaboracion) {
        if (colaboracion.getCodigoEvento() == null) {
            throw new IllegalArgumentException("Debe asociar la colaboración a un evento existente.");
        }

        if (colaboracion.getNitOrganizacion() == null) {
            throw new IllegalArgumentException("Debe indicar el Nit de la organización.");
        }

        if (colaboracion.getCertificado_participacion() == null || colaboracion.getCertificado_participacion().isEmpty()) {
            throw new IllegalArgumentException("Debe adjuntar el PDF de la colaboración.");
        }

        // Verificar si el evento existe
        eventoRepository.findById(colaboracion.getCodigoEvento().getCodigo())
            .orElseThrow(() -> new IllegalArgumentException("El evento asociado no existe."));

        return colaboracionRepository.save(colaboracion);
    }

    @Override
    public List<ColaboracionModel> listarColaboraciones() {
       return colaboracionRepository.findAll();
    }

}
