package com.gestion.eventos.Service;

import com.gestion.eventos.Model.ColaboracionModel;
import java.util.List;

public interface IColaboracionService {
    ColaboracionModel crearColaboracion(ColaboracionModel colaboracion);
    List<ColaboracionModel> listarColaboraciones();
}
