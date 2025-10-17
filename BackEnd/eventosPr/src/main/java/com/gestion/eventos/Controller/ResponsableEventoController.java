package com.gestion.eventos.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.eventos.Model.ResponsableEventoModel;
import com.gestion.eventos.Service.IResponsableEventoService;

@RestController
@RequestMapping ("/responsable")
public class ResponsableEventoController {
    @Autowired IResponsableEventoService responsableEventoService;
    @PostMapping
    public ResponseEntity<ResponsableEventoModel> crearResponsable(@RequestBody ResponsableEventoModel responsable) {
        ResponsableEventoModel nuevo = responsableEventoService.crearResponsable(responsable);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
    @GetMapping ("/ruta2")
    public ResponseEntity<List<ResponsableEventoModel>> listarResponsables(){
        return new ResponseEntity<>(responsableEventoService.listarResponsables(), HttpStatus.OK);
    }
}   

