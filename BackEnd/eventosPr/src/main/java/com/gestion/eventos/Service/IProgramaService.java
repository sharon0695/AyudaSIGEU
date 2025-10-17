package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.ProgramaModel;

public interface IProgramaService {
    ProgramaModel guardarPrograma(ProgramaModel programa);
    List<ProgramaModel> listarProgramas();
}
