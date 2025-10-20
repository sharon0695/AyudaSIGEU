package com.gestion.eventos.Controller;

import com.gestion.eventos.Model.EvaluacionModel;
import com.gestion.eventos.Service.IEvaluacionService;
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
@RequestMapping ("/rutaEva")
public class EvaluacionController {
    @Autowired IEvaluacionService evaluacionService;
    @PostMapping ("/ruta1")
    public ResponseEntity<EvaluacionModel> crearEvaluacion(@RequestBody EvaluacionModel evaluacion){
        return new ResponseEntity<>(evaluacionService.guardarEvaluacion(evaluacion),HttpStatus.CREATED);
    }
    @GetMapping ("/ruta2")
    public ResponseEntity<List<EvaluacionModel>> listarEvaluaciones(){
        return new ResponseEntity<>(evaluacionService.listarEvaluaciones(), HttpStatus.OK);
    }
}   
