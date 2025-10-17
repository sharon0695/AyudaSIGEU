package com.gestion.eventos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestion.eventos.Model.EspacioModel;
import com.gestion.eventos.Repository.IEspacioRepository;
@Service
public class EspacioServiceImp implements IEspacioService{
    @Autowired IEspacioRepository espacioRepository;

    @Override
    public EspacioModel guardarEspacio(EspacioModel espacio) {
        return espacioRepository.save(espacio);
    }

    @Override
    public List<EspacioModel> listarEspacios() {
        return espacioRepository.findAll();
    }

}
