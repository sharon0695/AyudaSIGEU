package com.gestion.eventos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.eventos.Model.OrganizacionModel;
import com.gestion.eventos.Service.IOrganizacionService;

@RestController
@RequestMapping ("/organizacionExterna")
public class OrganizacionController {
    @Autowired IOrganizacionService organizacionService;
    @PostMapping ("/registrar")
    public ResponseEntity<OrganizacionModel> crearOrganizacion(@RequestBody OrganizacionModel organizacion){
        return new ResponseEntity<>(organizacionService.guardarOrganizacion(organizacion),HttpStatus.CREATED);
    }
    @GetMapping ("/listar")
    public ResponseEntity<List<OrganizacionModel>> listarOrganizaciones(){
        return new ResponseEntity<>(organizacionService.listarOrganizaciones(), HttpStatus.OK);
    }
    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<String> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(organizacionService.buscarOrganizacionPorNombre(nombre));
    }
    @PutMapping("/editar/{nit}")
    public ResponseEntity<?> editarOrganizacion(
        @PathVariable String nit,
        @RequestParam Integer idUsuarioEditor, 
        @RequestBody OrganizacionModel organizacionActualizada) {

        OrganizacionModel orgActualizada = organizacionService.editarOrganizacion(nit, idUsuarioEditor, organizacionActualizada);
        return new ResponseEntity<>(orgActualizada, HttpStatus.OK);
    }
    @GetMapping("/{nit}")
    public ResponseEntity<OrganizacionModel> obtenerOrganizacionPorNit(@PathVariable String nit) {
        Optional<OrganizacionModel> organizacion = organizacionService.obtenerOrganizacionPorNit(nit);
        return organizacion
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/eliminar/{nit}")
    public ResponseEntity<String> eliminarOrganizacion(
        @PathVariable String nit,
        @RequestParam Integer solicitanteId
    ) {
        organizacionService.eliminarOrganizacion(nit, solicitanteId);
        return ResponseEntity.ok("Organización eliminada correctamente");
    }
}
