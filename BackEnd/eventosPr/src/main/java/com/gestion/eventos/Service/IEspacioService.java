package com.gestion.eventos.Service;

import com.gestion.eventos.Model.EspacioModel;
import java.util.List;

public interface IEspacioService {
    EspacioModel guardarEspacio(EspacioModel espacio);
    List<EspacioModel> listarEspacios();
}
