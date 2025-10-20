package com.gestion.eventos.Controller;

import com.gestion.eventos.DTO.EventoRegistroCompleto;
import com.gestion.eventos.DTO.EventoRegistroResponse;
import com.gestion.eventos.DTO.MensajeResponse;
import com.gestion.eventos.Model.EventoModel;
import com.gestion.eventos.Service.IEventoService;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


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
    @PutMapping(value = "/editar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MensajeResponse> editarEventoCompleto(
        @RequestParam Integer codigo,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String descripcion,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) String fecha,
        @RequestParam(required = false) String horaInicio,
        @RequestParam(required = false) String horaFin,
        @RequestParam(required = false) String codigoLugar,
        @RequestParam(required = false) String nitOrganizacion,
        @RequestParam(required = false) List<String> espacios,
        @RequestParam(required = false) List<Integer> responsables,
        @RequestParam(required = false) List<String> organizaciones,
        @RequestParam(required = false) List<MultipartFile> avalResponsables,
        @RequestParam(required = false) List<MultipartFile> avalOrganizaciones,
        @RequestParam(required = false) List<String> representanteAlternoOrganizacion
        ) {
        EventoModel cambios = new EventoModel();
        cambios.setNombre(nombre);
        cambios.setDescripcion(descripcion);
        cambios.setTipo(tipo);
        if (fecha != null && !fecha.isBlank()) cambios.setFecha(Date.valueOf(LocalDate.parse(fecha)));
        if (horaInicio != null && !horaInicio.isBlank()) cambios.setHora_inicio(Time.valueOf(LocalTime.parse(horaInicio)));
        if (horaFin != null && !horaFin.isBlank()) cambios.setHora_fin(Time.valueOf(LocalTime.parse(horaFin)));
        cambios.setCodigo_lugar(codigoLugar);
        cambios.setNitOrganizacion(nitOrganizacion);

        eventoService.actualizarEvento(codigo, cambios);
        eventoService.reemplazarOrganizaciones(codigo, organizaciones, representanteAlternoOrganizacion, avalOrganizaciones);
        eventoService.reemplazarResponsables(codigo, responsables, avalResponsables);

        return ResponseEntity.ok(new MensajeResponse("Evento actualizado exitosamente"));
    }
    @GetMapping("/detalles/{codigo}")
    public ResponseEntity<Map<String, Object>> obtenerAsociaciones(@PathVariable Integer codigo) {
    eventoService.buscarPorCodigo(codigo).orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    var resp = new HashMap<String, Object>();
    resp.put("organizaciones", eventoService.obtenerOrganizacionesEvento(codigo));
    resp.put("responsables", eventoService.obtenerResponsablesEvento(codigo));
    return ResponseEntity.ok(resp);
    }
}   

