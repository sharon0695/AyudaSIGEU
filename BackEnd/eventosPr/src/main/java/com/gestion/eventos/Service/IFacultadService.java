package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.FacultadModel;

public interface IFacultadService {
    FacultadModel guardarFacultad(FacultadModel facultad);
    List<FacultadModel> listarFacultades();
}
