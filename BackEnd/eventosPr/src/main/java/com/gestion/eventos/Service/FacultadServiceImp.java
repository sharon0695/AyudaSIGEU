package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.eventos.Model.FacultadModel;
import com.gestion.eventos.Repository.IFacultadRepository;
@Service
public class FacultadServiceImp implements IFacultadService{
    @Autowired IFacultadRepository facultadRepository;

    @Override
    public FacultadModel guardarFacultad(FacultadModel facultad) {
        return facultadRepository.save(facultad);    
    }

    @Override
    public List<FacultadModel> listarFacultades() {
        return facultadRepository.findAll();   
    }

}
