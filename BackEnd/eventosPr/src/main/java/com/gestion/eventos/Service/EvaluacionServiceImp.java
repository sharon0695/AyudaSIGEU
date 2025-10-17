package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.eventos.Model.EvaluacionModel;
import com.gestion.eventos.Repository.IEvaluacionRepository;
@Service
public class EvaluacionServiceImp implements IEvaluacionService{
    @Autowired IEvaluacionRepository evaluacionRepository;

    @Override
    public EvaluacionModel guardarEvaluacion(EvaluacionModel evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    @Override
    public List<EvaluacionModel> listarEvaluaciones() {
        return evaluacionRepository.findAll();    
    }

}
