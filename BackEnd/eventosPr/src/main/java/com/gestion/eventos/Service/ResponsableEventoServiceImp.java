package com.gestion.eventos.Service;

import com.gestion.eventos.Model.ResponsableEventoModel;
import com.gestion.eventos.Repository.IEventoRepository;
import com.gestion.eventos.Repository.IResponsableEventoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ResponsableEventoServiceImp implements IResponsableEventoService{
    @Autowired IResponsableEventoRepository responsableEventoRepository;

    @Autowired IEventoRepository eventoRepository;

    public ResponsableEventoModel crearResponsable(ResponsableEventoModel responsable) {
        if (responsable.getCodigoEvento() == null) {
            throw new IllegalArgumentException("Debe asociar el responsable a un evento.");
        }

        if (responsable.getId_usuario() == null) {
            throw new IllegalArgumentException("Debe ingresar el id del responsable.");
        }

        if (responsable.getDocumentoAval() == null || responsable.getDocumentoAval().isEmpty()) {
            throw new IllegalArgumentException("Debe adjuntar el PDF del responsable.");
        }

        eventoRepository.findById(responsable.getCodigoEvento().getCodigo())
            .orElseThrow(() -> new IllegalArgumentException("El evento asociado no existe."));

        return responsableEventoRepository.save(responsable);
    }

    @Override
    public List<ResponsableEventoModel> listarResponsables() {
        return responsableEventoRepository.findAll();    
    }

}
