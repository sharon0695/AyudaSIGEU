package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.UnidadAcademicaModel;

public interface IUnidadAcademicaService {
    UnidadAcademicaModel guardarUniAcad(UnidadAcademicaModel uniAcad);
    List<UnidadAcademicaModel> listarUnidades();
}
