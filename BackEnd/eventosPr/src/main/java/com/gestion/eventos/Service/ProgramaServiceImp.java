package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.eventos.Model.ProgramaModel;
import com.gestion.eventos.Repository.IProgramaRepository;
@Service
public class ProgramaServiceImp implements IProgramaService{
    @Autowired IProgramaRepository programaRepository;

    @Override
    public ProgramaModel guardarPrograma(ProgramaModel programa) {
        return programaRepository.save(programa);
    }

    @Override
    public List<ProgramaModel> listarProgramas() {
        return programaRepository.findAll();    
    }

}
