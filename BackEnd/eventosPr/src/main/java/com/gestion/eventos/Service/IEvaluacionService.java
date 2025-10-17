package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.EvaluacionModel;

public interface IEvaluacionService {
    EvaluacionModel guardarEvaluacion(EvaluacionModel evaluacion);
    List<EvaluacionModel> listarEvaluaciones();
}
