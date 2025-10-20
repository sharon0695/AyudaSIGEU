package com.gestion.eventos.Controller;

import com.gestion.eventos.Model.FacultadModel;
import com.gestion.eventos.Service.IFacultadService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/facultad")
public class FacultadController {
    @Autowired IFacultadService facultadService;
    @PostMapping ("/registrar")
    public ResponseEntity<FacultadModel> crearFacultad(@RequestBody FacultadModel facultad){
        return new ResponseEntity<>(facultadService.guardarFacultad(facultad),HttpStatus.CREATED);
    }
    @GetMapping ("/listar")
    public ResponseEntity<List<FacultadModel>> listarFacultades(){
        return new ResponseEntity<>(facultadService.listarFacultades(), HttpStatus.OK);
    }
}   

