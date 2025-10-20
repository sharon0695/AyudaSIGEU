package com.gestion.eventos.Service;

import com.gestion.eventos.Model.EvaluacionModel;
import java.util.List;

public interface IEvaluacionService {
    EvaluacionModel guardarEvaluacion(EvaluacionModel evaluacion);
    List<EvaluacionModel> listarEvaluaciones();
}
