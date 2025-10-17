package com.gestion.eventos.Service;

import java.util.List;

import com.gestion.eventos.Model.EspacioModel;

public interface IEspacioService {
    EspacioModel guardarEspacio(EspacioModel espacio);
    List<EspacioModel> listarEspacios();
}
