package com.gestion.eventos.Service;

import com.gestion.eventos.Model.UnidadAcademicaModel;
import java.util.List;

public interface IUnidadAcademicaService {
    UnidadAcademicaModel guardarUniAcad(UnidadAcademicaModel uniAcad);
    List<UnidadAcademicaModel> listarUnidades();
}
