package com.gestion.eventos.Controller;

import com.gestion.eventos.Model.ReservacionModel;
import com.gestion.eventos.Service.IReservacionService;
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
@RequestMapping ("/reservacion")
public class ReservacionController {
    @Autowired IReservacionService reservacionService;
    
    @PostMapping
    public ResponseEntity<ReservacionModel> crearReservacion(@RequestBody ReservacionModel reservacion) {
        ReservacionModel nuevaReservacion = reservacionService.crearReservacion(reservacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReservacion);
    }
    @GetMapping ("/ruta2")
    public ResponseEntity<List<ReservacionModel>> listarReservaciones(){
        return new ResponseEntity<>(reservacionService.listarReservaciones(), HttpStatus.OK);
    }
}   
