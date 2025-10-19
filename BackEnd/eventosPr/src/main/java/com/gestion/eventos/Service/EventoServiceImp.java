package com.gestion.eventos.Service;

import com.gestion.eventos.DTO.EventoRegistroCompleto;
import com.gestion.eventos.Model.ColaboracionModel;
import com.gestion.eventos.Model.EspacioModel;
import com.gestion.eventos.Model.EventoModel;
import com.gestion.eventos.Model.OrganizacionModel;
import com.gestion.eventos.Model.ReservacionModel;
import com.gestion.eventos.Model.ResponsableEventoModel;
import com.gestion.eventos.Model.UsuarioModel;
import com.gestion.eventos.Repository.IColaboracionRepository;
import com.gestion.eventos.Repository.IEspacioRepository;
import com.gestion.eventos.Repository.IEventoRepository;
import com.gestion.eventos.Repository.IOrganizacionRepository;
import com.gestion.eventos.Repository.IReservacionRepository;
import com.gestion.eventos.Repository.IResponsableEventoRepository;
import com.gestion.eventos.Repository.IUsuarioRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EventoServiceImp implements IEventoService {
    
    @Autowired private IEventoRepository eventoRepository;
    @Autowired private IOrganizacionRepository organizacionRepository;
    @Autowired private IColaboracionRepository colaboracionRepository;
    @Autowired private IResponsableEventoRepository responsableEventoRepository;
    @Autowired private IReservacionRepository reservacionRepository;
    @Autowired private IEspacioRepository espacioRepository;
    @Autowired private IUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public EventoModel registrarEventoCompleto(EventoRegistroCompleto request) {
        try {
            //Validar campos obligatorios del evento
            validarCamposEvento(request);
            
            if (request.getId_usuario_registra() == null) {
                throw new IllegalArgumentException("El ID del usuario que registra el evento es obligatorio");
            }
            
            usuarioRepository.findById(request.getId_usuario_registra())
                .orElseThrow(() -> new IllegalArgumentException("Usuario registrador no encontrado"));
            
            //Crear y guardar el evento principal
            EventoModel evento = new EventoModel();
            evento.setNombre(request.getNombre());
            evento.setDescripcion(request.getDescripcion());
            evento.setTipo(request.getTipo());
            evento.setFecha(request.getFecha());
            evento.setHora_inicio(request.getHora_inicio());
            evento.setHora_fin(request.getHora_fin());
            evento.setCodigo_lugar(request.getCodigo_lugar());
            evento.setEstado(EventoModel.estado.borrador);
            
            if (request.getOrganizacion() != null) {
                evento.setNitOrganizacion(request.getOrganizacion().getNit());
            }
            
            evento = eventoRepository.save(evento);
            
            //Procesar organización externa si existe
            if (request.getOrganizacion() != null) {
                procesarOrganizacion(request.getOrganizacion(), evento, request.getId_usuario_registra());
            }
            
            //Procesar responsables del evento
            if (request.getResponsables() != null && !request.getResponsables().isEmpty()) {
                procesarResponsables(request.getResponsables(), evento);
            }
            
            //Procesar reservaciones de espacios
            if (request.getReservaciones() != null && !request.getReservaciones().isEmpty()) {
                procesarReservaciones(request.getReservaciones(), evento);
            }
            
            return evento;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el evento: " + e.getMessage(), e);
        }
    }

    private void validarCamposEvento(EventoRegistroCompleto request) {
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del evento es obligatorio");
        }
        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del evento es obligatoria");
        }
        if (request.getTipo() == null || request.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del evento es obligatorio");
        }
        if (request.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del evento es obligatoria");
        }
        if (request.getHora_inicio() == null) {
            throw new IllegalArgumentException("La hora de inicio del evento es obligatoria");
        }
        if (request.getHora_fin() == null) {
            throw new IllegalArgumentException("La hora de fin del evento es obligatoria");
        }
        if (request.getCodigo_lugar() == null || request.getCodigo_lugar().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del lugar es obligatorio");
        }
    }

    private void procesarOrganizacion(EventoRegistroCompleto.OrganizacionDTO orgDTO, EventoModel evento, Integer idUsuarioRegistra) {
        // Verificar si la organización ya existe
        Optional<OrganizacionModel> orgExistente = organizacionRepository.findByNit(orgDTO.getNit());
        
        OrganizacionModel organizacion;
        
        if (orgExistente.isPresent()) {
            // Usar organización existente
            organizacion = orgExistente.get();
        } else {
            // Crear nueva organización
            validarDatosOrganizacion(orgDTO);
            
            organizacion = new OrganizacionModel();
            organizacion.setNit(orgDTO.getNit());
            organizacion.setNombre(orgDTO.getNombre());
            organizacion.setRepresentante_legal(orgDTO.getRepresentante_legal());
            organizacion.setUbicacion(orgDTO.getUbicacion());
            organizacion.setTelefono(orgDTO.getTelefono());
            organizacion.setSector_economico(orgDTO.getSector_economico());
            organizacion.setActividad_principal(orgDTO.getActividad_principal());
            
            // Asociar usuario que registra (debe existir en la BD)
            UsuarioModel usuario = usuarioRepository.findById(idUsuarioRegistra)
                .orElseThrow(() -> new IllegalArgumentException("Usuario registrador no encontrado"));
            organizacion.setUsuario(usuario);
            
            organizacion = organizacionRepository.save(organizacion);
        }
        
        // Crear colaboración entre la organización y el evento
        ColaboracionModel colaboracion = new ColaboracionModel();
        colaboracion.setNit_organizacion(organizacion);
        colaboracion.setCodigoEvento(evento);
        colaboracion.setCertificado_participacion(orgDTO.getCertificado_participacion());
        colaboracion.setRepresentante_alterno(orgDTO.getRepresentante_alterno());
        
        colaboracionRepository.save(colaboracion);
    }

    private void validarDatosOrganizacion(EventoRegistroCompleto.OrganizacionDTO orgDTO) {
        if (orgDTO.getNit() == null || orgDTO.getNit().trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT de la organización es obligatorio");
        }
        if (orgDTO.getNombre() == null || orgDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la organización es obligatorio");
        }
        if (orgDTO.getRepresentante_legal() == null || orgDTO.getRepresentante_legal().trim().isEmpty()) {
            throw new IllegalArgumentException("El representante legal es obligatorio");
        }
        if (orgDTO.getUbicacion() == null || orgDTO.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación es obligatoria");
        }
        if (orgDTO.getTelefono() == null || !orgDTO.getTelefono().matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono solo debe contener números");
        }
        if (orgDTO.getSector_economico() == null || orgDTO.getSector_economico().trim().isEmpty()) {
            throw new IllegalArgumentException("El sector económico es obligatorio");
        }
        if (orgDTO.getActividad_principal() == null || orgDTO.getActividad_principal().trim().isEmpty()) {
            throw new IllegalArgumentException("La actividad principal es obligatoria");
        }
    }

    private void procesarResponsables(List<EventoRegistroCompleto.ResponsableDTO> responsablesDTO, EventoModel evento) {
        for (EventoRegistroCompleto.ResponsableDTO respDTO : responsablesDTO) {
            // Validar datos del responsable
            if (respDTO.getId_usuario() == null) {
                throw new IllegalArgumentException("El ID del responsable es obligatorio");
            }
            if (respDTO.getDocumentoAval() == null || respDTO.getDocumentoAval().trim().isEmpty()) {
                throw new IllegalArgumentException("El documento de aval es obligatorio");
            }
            if (respDTO.getTipoAval() == null || respDTO.getTipoAval().trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de aval es obligatorio");
            }
            
            // Verificar que el usuario existe
            UsuarioModel usuario = usuarioRepository.findById(respDTO.getId_usuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario responsable no encontrado"));
            
            // Crear responsable
            ResponsableEventoModel responsable = new ResponsableEventoModel();
            responsable.setId_usuario(usuario);
            responsable.setCodigoEvento(evento);
            responsable.setDocumentoAval(respDTO.getDocumentoAval());
            
            try {
                responsable.setTipoAval(
                    ResponsableEventoModel.tipo_aval.valueOf(respDTO.getTipoAval().toLowerCase())
                );
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de aval inválido. Use: director_programa o director_docencia");
            }
            
            responsableEventoRepository.save(responsable);
        }
    }

    private void procesarReservaciones(List<EventoRegistroCompleto.ReservacionDTO> reservacionesDTO, EventoModel evento) {
        for (EventoRegistroCompleto.ReservacionDTO resDTO : reservacionesDTO) {
            // Validar datos de reservación
            if (resDTO.getCodigo_espacio() == null || resDTO.getCodigo_espacio().trim().isEmpty()) {
                throw new IllegalArgumentException("El código del espacio es obligatorio");
            }
            if (resDTO.getHora_inicio() == null) {
                throw new IllegalArgumentException("La hora de inicio de la reservación es obligatoria");
            }
            if (resDTO.getHora_fin() == null) {
                throw new IllegalArgumentException("La hora de fin de la reservación es obligatoria");
            }
            
            // Verificar que el espacio existe
            EspacioModel espacio = espacioRepository.findById(resDTO.getCodigo_espacio())
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado: " + resDTO.getCodigo_espacio()));
            
            // Crear reservación
            ReservacionModel reservacion = new ReservacionModel();
            reservacion.setCodigo_evento(evento);
            reservacion.setCodigo_espacio(espacio);
            reservacion.setHora_inicio(resDTO.getHora_inicio());
            reservacion.setHora_fin(resDTO.getHora_fin());
            
            reservacionRepository.save(reservacion);
        }
    }

    @Override
    public List<EventoModel> listarEventos() {
        return eventoRepository.findAll();
    }

    public Optional<EventoModel> buscarPorCodigo(Integer codigo) {
        return eventoRepository.findById(codigo);
    }

    @Override
    public EventoModel actualizarEvento(Integer codigo, EventoModel cambios) {
        EventoModel existente = eventoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Solo Borrador o Rechazado
        if (existente.getEstado() != null) {
            String st = existente.getEstado().name();
            if (!"borrador".equalsIgnoreCase(st) && !"rechazado".equalsIgnoreCase(st)) {
                throw new RuntimeException("Solo se puede editar eventos en estado Borrador o Rechazado");
            }
        }

        if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
        if (cambios.getDescripcion() != null) existente.setDescripcion(cambios.getDescripcion());
        if (cambios.getTipo() != null) existente.setTipo(cambios.getTipo());
        if (cambios.getFecha() != null) existente.setFecha(cambios.getFecha());
        if (cambios.getHora_inicio() != null) existente.setHora_inicio(cambios.getHora_inicio());
        if (cambios.getHora_fin() != null) existente.setHora_fin(cambios.getHora_fin());
        if (cambios.getCodigo_lugar() != null) existente.setCodigo_lugar(cambios.getCodigo_lugar());
        if (cambios.getNitOrganizacion() != null) existente.setNitOrganizacion(cambios.getNitOrganizacion());

        return eventoRepository.save(existente);
    }
    private String guardarPdf(MultipartFile file, String nombreDestino) {
        try {
            Path dir = java.nio.file.Paths.get("src/main/resources/static/uploads/avales/");
            Files.createDirectories(dir);
            Path destino = dir.resolve(nombreDestino);
            Files.copy(file.getInputStream(), destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/avales/" + nombreDestino;
        } catch (java.io.IOException e) {
            throw new RuntimeException("No se pudo guardar el PDF: " + e.getMessage(), e);
        }
    }
    @Override
    public void reemplazarOrganizaciones(Integer codigo, List<String> organizaciones, List<String> alternos, List<MultipartFile> avales){
        colaboracionRepository.deleteByCodigoEvento_Codigo(codigo);
        if (organizaciones == null) return;
        for (int i = 0; i < organizaciones.size(); i++) {
            String nit = organizaciones.get(i);
            String alterno = alternos != null && alternos.size() > i ? alternos.get(i) : null;
            MultipartFile file = (avales != null && avales.size() > i) ? avales.get(i) : null;
            String url = null;
            if (file != null && !file.isEmpty()) {
                if (!"application/pdf".equals(file.getContentType())) {
                    throw new RuntimeException("El aval debe ser PDF");
                }
                String nombre = codigo + "_org_" + nit + ".pdf";
                url = guardarPdf(file, nombre);
            }

            ColaboracionModel c = new ColaboracionModel();
            c.setCodigoEvento(eventoRepository.findById(codigo).orElseThrow());
            OrganizacionModel org = new OrganizacionModel(); 
            org.setNit(nit); 
            c.setNit_organizacion(org);
            c.setRepresentante_alterno(alterno);
            c.setCertificado_participacion(url);
            colaboracionRepository.save(c);
        }
    }

    @Override
    public void reemplazarResponsables(Integer codigo, List<Integer> responsables, List<MultipartFile> avales){
        responsableEventoRepository.deleteByCodigoEvento_Codigo(codigo);
        if (responsables == null) return;
        for (int i = 0; i < responsables.size(); i++) {
            Integer id = responsables.get(i);
            MultipartFile file = (avales != null && avales.size() > i) ? avales.get(i) : null;
            String url = null;
            if (file != null && !file.isEmpty()) {
                if (!"application/pdf".equals(file.getContentType())) {
                    throw new RuntimeException("El aval del responsable debe ser PDF");
                }
                String nombre = codigo + "_resp_" + id + ".pdf";
                url = guardarPdf(file, nombre);
            }

            ResponsableEventoModel r = new ResponsableEventoModel();
            r.setCodigoEvento(eventoRepository.findById(codigo).orElseThrow());
            UsuarioModel u = new UsuarioModel(); 
            u.setIdentificacion(id); 
            r.setId_usuario(u);
            r.setDocumentoAval(url);
            responsableEventoRepository.save(r);
        }
    }
}