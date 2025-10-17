package com.gestion.eventos.Service;

import com.gestion.eventos.Model.ResponsableEventoModel;
import java.util.List;

public interface IResponsableEventoService {
    ResponsableEventoModel crearResponsable(ResponsableEventoModel responsableEvento);
    List<ResponsableEventoModel> listarResponsables();
}
