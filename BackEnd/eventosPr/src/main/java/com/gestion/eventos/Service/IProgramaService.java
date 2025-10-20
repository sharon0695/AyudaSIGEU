package com.gestion.eventos.Service;

import com.gestion.eventos.Model.ProgramaModel;
import java.util.List;

public interface IProgramaService {
    ProgramaModel guardarPrograma(ProgramaModel programa);
    List<ProgramaModel> listarProgramas();
}
