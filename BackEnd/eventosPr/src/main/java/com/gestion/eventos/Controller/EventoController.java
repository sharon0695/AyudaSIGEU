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

import com.gestion.eventos.Model.EventoModel;
import com.gestion.eventos.Service.IEventoService;


@RestController
@RequestMapping ("/eventos")
public class EventoController {
    @Autowired IEventoService eventoService;

    @PostMapping("/registrar")
    public ResponseEntity<EventoModel> registrarEvento(@RequestBody EventoModel evento) {
        EventoModel eventoRegistrado = eventoService.registrarEvento(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoRegistrado);
    }
    @GetMapping ("/listar")
    public ResponseEntity<List<EventoModel>> listarEventos(){
        return new ResponseEntity<>(eventoService.listarEventos(), HttpStatus.OK);
    }
}   

