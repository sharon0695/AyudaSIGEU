package com.gestion.eventos.Service;

import com.gestion.eventos.Model.FacultadModel;
import java.util.List;

public interface IFacultadService {
    FacultadModel guardarFacultad(FacultadModel facultad);
    List<FacultadModel> listarFacultades();
}
