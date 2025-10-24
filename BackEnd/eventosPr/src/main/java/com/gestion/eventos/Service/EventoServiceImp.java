package com.gestion.eventos.Service;

import com.gestion.eventos.DTO.EventoOrganizacionResponse;
import com.gestion.eventos.DTO.EventoRegistroCompleto;
import com.gestion.eventos.DTO.EventoReservacionResponse;
import com.gestion.eventos.DTO.EventoResponsableResponse;
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
import java.sql.Time;
import java.util.ArrayList;
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
        validarCamposEvento(request);
        
        if (request.getId_usuario_registra() == null) {
            throw new IllegalArgumentException("El ID del usuario que registra el evento es obligatorio");
        }
        
        usuarioRepository.findById(request.getId_usuario_registra())
            .orElseThrow(() -> new IllegalArgumentException("El usuario registrador no existe en el sistema"));
        
        EventoModel evento = new EventoModel();
        evento.setNombre(request.getNombre());
        evento.setDescripcion(request.getDescripcion());
        evento.setTipo(request.getTipo());
        evento.setFecha(request.getFecha());
        evento.setHora_inicio(request.getHora_inicio());
        evento.setHora_fin(request.getHora_fin());
        evento.setEstado(EventoModel.estado.borrador);
        
        evento = eventoRepository.save(evento);
        
        if (request.getOrganizaciones() != null && !request.getOrganizaciones().isEmpty()) {
            procesarOrganizaciones(request.getOrganizaciones(), evento, request.getId_usuario_registra());
        }
        
        if (request.getResponsables() != null && !request.getResponsables().isEmpty()) {
            procesarResponsables(request.getResponsables(), evento);
        }
        
        if (request.getReservaciones() != null && !request.getReservaciones().isEmpty()) {
            procesarReservaciones(request.getReservaciones(), evento);
        }
        
        return evento;
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
        if (!request.getTipo().equalsIgnoreCase("Academico") && !request.getTipo().equalsIgnoreCase("Ludico")) {
            throw new IllegalArgumentException("El tipo de evento debe ser 'Academico' o 'Ludico'");
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
        
        java.time.LocalDate fechaActual = java.time.LocalDate.now();
        java.time.LocalDate fechaEvento = request.getFecha().toLocalDate();
        
        if (fechaEvento.isBefore(fechaActual)) {
            throw new IllegalArgumentException("La fecha del evento no puede ser anterior a la fecha actual");
        }
        
        if (request.getHora_inicio().equals(request.getHora_fin())) {
            throw new IllegalArgumentException("La hora de inicio y la hora de fin no pueden ser iguales");
        }
        
        if (request.getHora_fin().before(request.getHora_inicio())) {
            throw new IllegalArgumentException("La hora de fin no puede ser anterior a la hora de inicio");
        }
        
        if (request.getReservaciones() == null || request.getReservaciones().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un espacio para el evento");
        }
        
        if (request.getResponsables() == null || request.getResponsables().isEmpty()) {
            throw new IllegalArgumentException("Debe asignar al menos un responsable al evento");
        }
    }

    private void procesarOrganizaciones(List<EventoRegistroCompleto.OrganizacionDTO> organizacionesDTO, EventoModel evento, Integer idUsuarioRegistra) {
        int contador = 0;
        for (EventoRegistroCompleto.OrganizacionDTO orgDTO : organizacionesDTO) {
            contador++;
            final int numeroOrganizacion = contador;
            
            if (orgDTO.getNit() == null || orgDTO.getNit().trim().isEmpty()) {
                throw new IllegalArgumentException("El NIT de la organización " + numeroOrganizacion + " es obligatorio");
            }
        
            Optional<OrganizacionModel> orgExistente = organizacionRepository.findByNit(orgDTO.getNit());
            
            OrganizacionModel organizacion;
            
            if (orgExistente.isPresent()) {
                organizacion = orgExistente.get();
            } else {
                validarDatosOrganizacion(orgDTO, numeroOrganizacion);
                
                organizacion = new OrganizacionModel();
                organizacion.setNit(orgDTO.getNit());
                organizacion.setNombre(orgDTO.getNombre());
                organizacion.setRepresentante_legal(orgDTO.getRepresentante_legal());
                organizacion.setUbicacion(orgDTO.getUbicacion());
                organizacion.setTelefono(orgDTO.getTelefono());
                organizacion.setSector_economico(orgDTO.getSector_economico());
                organizacion.setActividad_principal(orgDTO.getActividad_principal());
                
                UsuarioModel usuario = usuarioRepository.findById(idUsuarioRegistra)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario registrador no encontrado"));
                organizacion.setUsuario(usuario);
                
                organizacion = organizacionRepository.save(organizacion);
            }
            
            if (orgDTO.getCertificado_participacion() != null && 
                !orgDTO.getCertificado_participacion().trim().isEmpty() &&
                !orgDTO.getCertificado_participacion().toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("El certificado de la organización " + numeroOrganizacion + " debe ser un archivo PDF");
            }
            
            if (orgDTO.getRepresentante_alterno() != null && 
                !orgDTO.getRepresentante_alterno().trim().isEmpty()) {
                if (orgDTO.getRepresentante_alterno().length() < 3) {
                    throw new IllegalArgumentException("El nombre del representante alterno de la organización " + numeroOrganizacion + " debe tener al menos 3 caracteres");
                }
            }
            
            ColaboracionModel colaboracion = new ColaboracionModel();
            colaboracion.setNitOrganizacion(organizacion);
            colaboracion.setCodigoEvento(evento);
            colaboracion.setCertificado_participacion(orgDTO.getCertificado_participacion());
            colaboracion.setRepresentante_alterno(orgDTO.getRepresentante_alterno());
            
            colaboracionRepository.save(colaboracion);
        }
    }

    private void validarDatosOrganizacion(EventoRegistroCompleto.OrganizacionDTO orgDTO, int numeroOrg) {
        if (orgDTO.getNit() == null || orgDTO.getNit().trim().isEmpty()) {
            throw new IllegalArgumentException("El NIT de la organización " + numeroOrg + " es obligatorio");
        }
        if (orgDTO.getNombre() == null || orgDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la organización " + numeroOrg + " es obligatorio");
        }
        if (orgDTO.getRepresentante_legal() == null || orgDTO.getRepresentante_legal().trim().isEmpty()) {
            throw new IllegalArgumentException("El representante legal de la organización " + numeroOrg + " es obligatorio");
        }
        if (orgDTO.getUbicacion() == null || orgDTO.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación de la organización " + numeroOrg + " es obligatoria");
        }
        if (orgDTO.getTelefono() == null || orgDTO.getTelefono().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono de la organización " + numeroOrg + " es obligatorio");
        }
        if (!orgDTO.getTelefono().matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono de la organización " + numeroOrg + " solo debe contener números");
        }
        if (orgDTO.getSector_economico() == null || orgDTO.getSector_economico().trim().isEmpty()) {
            throw new IllegalArgumentException("El sector económico de la organización " + numeroOrg + " es obligatorio");
        }
        if (orgDTO.getActividad_principal() == null || orgDTO.getActividad_principal().trim().isEmpty()) {
            throw new IllegalArgumentException("La actividad principal de la organización " + numeroOrg + " es obligatoria");
        }
    }

    private void procesarResponsables(List<EventoRegistroCompleto.ResponsableDTO> responsablesDTO, EventoModel evento) {
        int contador = 0;
        for (EventoRegistroCompleto.ResponsableDTO respDTO : responsablesDTO) {
            contador++;
            final int numeroResponsable = contador;
            
            if (respDTO.getId_usuario() == null) {
                throw new IllegalArgumentException("El responsable " + numeroResponsable + " no ha sido seleccionado");
            }
            
            UsuarioModel usuario = usuarioRepository.findById(respDTO.getId_usuario())
                .orElseThrow(() -> new IllegalArgumentException("El responsable " + numeroResponsable + " no existe en el sistema"));
            
            if (respDTO.getDocumentoAval() != null && 
                !respDTO.getDocumentoAval().trim().isEmpty() &&
                !respDTO.getDocumentoAval().toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("El documento de aval del responsable " + numeroResponsable + " debe ser un archivo PDF");
            }
            
            ResponsableEventoModel responsable = new ResponsableEventoModel();
            responsable.setId_usuario(usuario);
            responsable.setCodigoEvento(evento);
            responsable.setDocumentoAval(respDTO.getDocumentoAval());
            
            // Solo asignar tipoAval si viene en el request y es válido
            if (respDTO.getTipoAval() != null && !respDTO.getTipoAval().trim().isEmpty()) {
                try {
                    responsable.setTipoAval(
                        ResponsableEventoModel.tipo_aval.valueOf(respDTO.getTipoAval().toLowerCase())
                    );
                } catch (IllegalArgumentException e) {
                }
            }
            
            responsableEventoRepository.save(responsable);
        }
    }

    private void procesarReservaciones(List<EventoRegistroCompleto.ReservacionDTO> reservacionesDTO, EventoModel evento) {
        int contador = 0;
        for (EventoRegistroCompleto.ReservacionDTO resDTO : reservacionesDTO) {
            contador++;
            final int numeroEspacio = contador;
            
            if (resDTO.getCodigo_espacio() == null || resDTO.getCodigo_espacio().trim().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar el espacio " + numeroEspacio);
            }
            
            if (resDTO.getHora_inicio() == null) {
                throw new IllegalArgumentException("La hora de inicio del espacio " + numeroEspacio + " es obligatoria");
            }
            
            if (resDTO.getHora_fin() == null) {
                throw new IllegalArgumentException("La hora de fin del espacio " + numeroEspacio + " es obligatoria");
            }
            
            if (resDTO.getHora_inicio().equals(resDTO.getHora_fin())) {
                throw new IllegalArgumentException("La hora de inicio y fin del espacio " + numeroEspacio + " no pueden ser iguales");
            }
            
            if (resDTO.getHora_fin().before(resDTO.getHora_inicio())) {
                throw new IllegalArgumentException("La hora de fin del espacio " + numeroEspacio + " debe ser posterior a la hora de inicio");
            }
            
            final String codigoEspacio = resDTO.getCodigo_espacio();
            EspacioModel espacio = espacioRepository.findById(codigoEspacio)
                .orElseThrow(() -> new IllegalArgumentException("El espacio " + codigoEspacio + " no existe en el sistema"));
            
            ReservacionModel reservacion = new ReservacionModel();
            reservacion.setCodigoEvento(evento);
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

    @Override
    public Optional<EventoModel> buscarPorCodigo(Integer codigo) {
        return eventoRepository.findById(codigo);
    }

    @Override
    public EventoModel actualizarEvento(Integer codigo, EventoModel cambios) {
        EventoModel existente = eventoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("El evento con código " + codigo + " no existe"));

        if (existente.getEstado() != null) {
            String st = existente.getEstado().name();
            if (!"borrador".equalsIgnoreCase(st) && !"rechazado".equalsIgnoreCase(st)) {
                throw new RuntimeException("Solo se pueden editar eventos en estado Borrador o Rechazado. Estado actual: " + st);
            }
        }

        if (cambios.getNombre() != null) {
            if (cambios.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del evento no puede estar vacío");
            }
            existente.setNombre(cambios.getNombre());
        }
        
        if (cambios.getDescripcion() != null) {
            existente.setDescripcion(cambios.getDescripcion());
        }
        
        if (cambios.getTipo() != null) {
            if (!cambios.getTipo().equalsIgnoreCase("Academico") && 
                !cambios.getTipo().equalsIgnoreCase("Ludico")) {
                throw new IllegalArgumentException("El tipo de evento debe ser 'Academico' o 'Ludico'");
            }
            existente.setTipo(cambios.getTipo());
        }
        
        if (cambios.getFecha() != null) {
            java.time.LocalDate fechaActual = java.time.LocalDate.now();
            java.time.LocalDate fechaEvento = cambios.getFecha().toLocalDate();
            
            if (fechaEvento.isBefore(fechaActual)) {
                throw new IllegalArgumentException("La fecha del evento debe ser igual o posterior a la fecha actual");
            }
            existente.setFecha(cambios.getFecha());
        }
        
        if (cambios.getHora_inicio() != null) {
            existente.setHora_inicio(cambios.getHora_inicio());
        }
        
        if (cambios.getHora_fin() != null) {
            existente.setHora_fin(cambios.getHora_fin());
        }
        
        if (existente.getHora_inicio() != null && existente.getHora_fin() != null) {
            if (existente.getHora_inicio().equals(existente.getHora_fin())) {
                throw new IllegalArgumentException("La hora de inicio y la hora de fin no pueden ser iguales");
            }
            if (existente.getHora_fin().before(existente.getHora_inicio())) {
                throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
            }
        }
        
        return eventoRepository.save(existente);
    }

    private String guardarPdf(MultipartFile file, String nombreDestino) {
        try {
            if (!"application/pdf".equals(file.getContentType())) {
                throw new RuntimeException("El archivo debe ser un PDF. Tipo recibido: " + file.getContentType());
            }
            
            Path dir = java.nio.file.Paths.get("src/main/resources/static/uploads/avales/");
            Files.createDirectories(dir);
            Path destino = dir.resolve(nombreDestino);
            Files.copy(file.getInputStream(), destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/avales/" + nombreDestino;
        } catch (java.io.IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public void reemplazarOrganizaciones(Integer codigo, List<String> organizaciones, List<String> alternos, List<MultipartFile> avales) {
        eventoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("El evento con código " + codigo + " no existe"));
        
        colaboracionRepository.deleteByCodigoEvento_Codigo(codigo);
        
        if (organizaciones == null || organizaciones.isEmpty()) {
            return;
        }
        
        int contador = 0;
        for (String nit : organizaciones) {
            contador++;
            final int numeroOrganizacion = contador;
            
            if (nit == null || nit.trim().isEmpty()) {
                throw new IllegalArgumentException("El NIT de la organización " + numeroOrganizacion + " es obligatorio");
            }
            
            final String nitFinal = nit;
            organizacionRepository.findByNit(nitFinal)
                .orElseThrow(() -> new IllegalArgumentException("La organización con NIT " + nitFinal + " no existe en el sistema"));
            
            String alterno = alternos != null && alternos.size() >= numeroOrganizacion ? alternos.get(numeroOrganizacion - 1) : null;
            MultipartFile file = (avales != null && avales.size() >= numeroOrganizacion) ? avales.get(numeroOrganizacion - 1) : null;
            String url = null;
            
            if (file != null && !file.isEmpty()) {
                if (!"application/pdf".equals(file.getContentType())) {
                    throw new RuntimeException("El aval de la organización " + numeroOrganizacion + " debe ser un archivo PDF");
                }
                String nombre = codigo + "_org_" + nit + ".pdf";
                url = guardarPdf(file, nombre);
            }

            ColaboracionModel c = new ColaboracionModel();
            c.setCodigoEvento(eventoRepository.findById(codigo).orElseThrow());
            OrganizacionModel org = new OrganizacionModel(); 
            org.setNit(nit); 
            c.setNitOrganizacion(org);
            c.setRepresentante_alterno(alterno);
            c.setCertificado_participacion(url);
            colaboracionRepository.save(c);
        }
    }

    @Override
    public void reemplazarResponsables(Integer codigo, List<Integer> responsables, List<MultipartFile> avales) {
        eventoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("El evento con código " + codigo + " no existe"));
        
        responsableEventoRepository.deleteByCodigoEvento_Codigo(codigo);
        
        if (responsables == null || responsables.isEmpty()) {
            throw new IllegalArgumentException("Debe asignar al menos un responsable al evento");
        }
        
        int contador = 0;
        for (Integer id : responsables) {
            contador++;
            final int numeroResponsable = contador;
            
            if (id == null || id == 0) {
                throw new IllegalArgumentException("Debe seleccionar el responsable " + numeroResponsable);
            }
            
            final Integer idFinal = id;
            usuarioRepository.findById(idFinal)
                .orElseThrow(() -> new IllegalArgumentException("El responsable con ID " + idFinal + " no existe en el sistema"));
            
            MultipartFile file = (avales != null && avales.size() >= numeroResponsable) ? avales.get(numeroResponsable - 1) : null;
            String url = null;
            
            if (file != null && !file.isEmpty()) {
                if (!"application/pdf".equals(file.getContentType())) {
                    throw new RuntimeException("El aval del responsable " + numeroResponsable + " debe ser un archivo PDF");
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

    @Override
    public void reemplazarReservaciones(Integer codigo, List<String> espacios, String horaInicio, String horaFin) {
        eventoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("El evento con código " + codigo + " no existe"));
        
        reservacionRepository.deleteByCodigoEvento_Codigo(codigo);
        
        if (espacios == null || espacios.isEmpty()) {
            return;
        }
        
        Time horaInicioTime = horaInicio != null && !horaInicio.isBlank() 
            ? Time.valueOf(java.time.LocalTime.parse(horaInicio)) 
            : null;
        Time horaFinTime = horaFin != null && !horaFin.isBlank() 
            ? Time.valueOf(java.time.LocalTime.parse(horaFin)) 
            : null;
        
        int contador = 0;
        for (String codigoEspacio : espacios) {
            contador++;
            final int numeroEspacio = contador;
            final String codigoEspacioFinal = codigoEspacio;
            
            if (codigoEspacio == null || codigoEspacio.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar el espacio " + numeroEspacio);
            }
            
            EspacioModel espacio = espacioRepository.findById(codigoEspacioFinal)
                .orElseThrow(() -> new IllegalArgumentException("El espacio " + codigoEspacioFinal + " no existe"));
            
            ReservacionModel reservacion = new ReservacionModel();
            reservacion.setCodigoEvento(eventoRepository.findById(codigo).orElseThrow());
            reservacion.setCodigo_espacio(espacio);
            reservacion.setHora_inicio(horaInicioTime);
            reservacion.setHora_fin(horaFinTime);
            
            reservacionRepository.save(reservacion);
        }
    }

    @Override
    public List<EventoOrganizacionResponse> obtenerOrganizacionesEvento(Integer codigo) {
    var list = colaboracionRepository.findAllByCodigoEvento_Codigo(codigo);
    var out = new ArrayList<EventoOrganizacionResponse>();
    for (var c : list) {
        out.add(new EventoOrganizacionResponse(
        c.getNitOrganizacion() != null ? c.getNitOrganizacion().getNit() : null,
        c.getRepresentante_alterno(),
        c.getCertificado_participacion()
        ));
    }
    return out;
    }

    @Override
    public List<EventoResponsableResponse> obtenerResponsablesEvento(Integer codigo) {
        var list = responsableEventoRepository.findAllByCodigoEvento_Codigo(codigo);
        var out = new ArrayList<EventoResponsableResponse>();
        for (var r : list) {
            out.add(new EventoResponsableResponse(
                r.getId_usuario() != null ? r.getId_usuario().getIdentificacion() : null,
                r.getDocumentoAval()
            ));
        }
        return out;
    }

    @Override
    public List<EventoReservacionResponse> obtenerReservacionesEvento(Integer codigo) {
        var list = reservacionRepository.findAllByCodigoEvento_Codigo(codigo);
        var out = new ArrayList<EventoReservacionResponse>();
        for (var r : list) {
            String codEspacio = null;
            
            // Acceder al código del espacio desde el objeto EspacioModel
            if (r.getCodigo_espacio() != null) {
                codEspacio = r.getCodigo_espacio().getCodigo();
            }
            
            out.add(new EventoReservacionResponse(
                codEspacio,
                r.getHora_inicio() != null ? r.getHora_inicio().toString() : null,
                r.getHora_fin() != null ? r.getHora_fin().toString() : null
            ));
        }
        return out;
    }
    @Override
    @Transactional
    public void eliminarEvento(Integer codigo) {
    // Buscar el evento
    EventoModel evento = eventoRepository.findById(codigo)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

    // Validar estado permitido para eliminar
    String estado = evento.getEstado().name();
    if (!estado.equalsIgnoreCase("borrador") && !estado.equalsIgnoreCase("rechazado")) {
        throw new RuntimeException("Solo se pueden eliminar eventos en estado 'borrador' o 'rechazado'");
    }
    // 🔹 Eliminar responsables asociados al evento
    List<ResponsableEventoModel> responsables = responsableEventoRepository.findAllByCodigoEvento_Codigo(codigo);
    if (!responsables.isEmpty()) {
    responsableEventoRepository.deleteAll(responsables);
}

    // 🔹 Eliminar colaboraciones asociadas al evento
    List<ColaboracionModel> colaboraciones = colaboracionRepository.findAllByCodigoEvento_Codigo(codigo);
    if (!colaboraciones.isEmpty()) {
        colaboracionRepository.deleteAll(colaboraciones);
    }

    // 🔹 Eliminar reservaciones asociadas al evento
    List<ReservacionModel> reservaciones = reservacionRepository.findAllByCodigoEvento_Codigo(codigo);
    if (!reservaciones.isEmpty()) {
        reservacionRepository.deleteAll(reservaciones);
    }

    // 🔹 Finalmente eliminar el evento
    eventoRepository.delete(evento);
    }

}