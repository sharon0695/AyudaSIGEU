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

import com.gestion.eventos.DTO.EventoRegistroCompleto;
import com.gestion.eventos.DTO.EventoRegistroResponse;
import com.gestion.eventos.Model.EventoModel;
import com.gestion.eventos.Service.IEventoService;


@RestController
@RequestMapping ("/eventos")
public class EventoController {
    @Autowired IEventoService eventoService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarEvento(@RequestBody EventoRegistroCompleto request) {
        EventoModel eventoRegistrado = eventoService.registrarEventoCompleto(request);            
            EventoRegistroResponse response = new EventoRegistroResponse(
                "Registro de evento exitoso. El evento se encuentra en estado borrador",
                eventoRegistrado.getCodigo()
            );            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping ("/listar")
    public ResponseEntity<List<EventoModel>> listarEventos(){
        return new ResponseEntity<>(eventoService.listarEventos(), HttpStatus.OK);
    }
}   

