package com.gestion.eventos.Service;

import com.gestion.eventos.Model.UnidadAcademicaModel;
import com.gestion.eventos.Repository.IUnidadAcademicaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UnidadAcademicaServiceImp implements IUnidadAcademicaService{
    @Autowired IUnidadAcademicaRepository unidadAcademicaRepository;

    @Override
    public UnidadAcademicaModel guardarUniAcad(UnidadAcademicaModel uniAcad) {
        return unidadAcademicaRepository.save(uniAcad);    
    }

    @Override
    public List<UnidadAcademicaModel> listarUnidades() {
        return unidadAcademicaRepository.findAll();
    }

}
