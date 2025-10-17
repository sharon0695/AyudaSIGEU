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

import com.gestion.eventos.Model.UnidadAcademicaModel;
import com.gestion.eventos.Service.IUnidadAcademicaService;

@RestController
@RequestMapping ("/unidad")
public class UnidadAcademicaController {
    @Autowired IUnidadAcademicaService unidadAcademicaService;
    @PostMapping ("/registrar")
    public ResponseEntity<UnidadAcademicaModel> crearUnidadAcad(@RequestBody UnidadAcademicaModel unidadAcademica){
        return new ResponseEntity<>(unidadAcademicaService.guardarUniAcad(unidadAcademica),HttpStatus.CREATED);
    }
    @GetMapping ("/listar")
    public ResponseEntity<List<UnidadAcademicaModel>> listarUnidades(){
        return new ResponseEntity<>(unidadAcademicaService.listarUnidades(), HttpStatus.OK);
    }
}   
