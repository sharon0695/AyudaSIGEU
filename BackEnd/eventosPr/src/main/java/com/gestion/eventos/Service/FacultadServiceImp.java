package com.gestion.eventos.Service;

import com.gestion.eventos.Model.FacultadModel;
import com.gestion.eventos.Repository.IFacultadRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
