import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EventosService, EventoRegistroCompleto, OrganizacionDTO, ResponsableDTO, ReservacionDTO } from '../services/eventos.service';
import { EspacioService } from '../services/espacio.service';
import { OrganizacionesService } from '../services/organizaciones.service';
import { Api } from '../services/usuarios.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-eventos',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './eventos.html',
  styleUrl: './eventos.css'
})
export class Eventos {
  eventos: any[] = [];
  mensaje = '';
  nuevoEvento: any = {
    nombre: '',
    descripcion: '',
    tipo: '',
    fecha: '',
    hora_inicio: '',
    hora_fin: '',
    codigo_lugar: '',
    nit_organizacion: ''
  };
  showModal = false;
  editMode = false;
  editCodigo: number | null = null;

  // Modal de mensajes
  showMessageModal = false;
  messageType: 'success' | 'error' = 'success';
  messageText = '';
  messageTitle = '';
  
  espaciosListado: Array<{ codigo: string; nombre?: string }> = [];
  organizacionesListado: Array<{ nit: string; nombre?: string }> = [];
  usuariosListado: Array<{ identificacion: number; nombre?: string; apellido?: string }> = [];

  selectedEspacios: string[] = [];
  selectedOrganizaciones: Array<{ nit: string; tipo: 'legal' | 'alterno'; alterno?: string; aval?: File | null }> = [];
  selectedResponsables: Array<{ id: number; aval?: File | null }> = [];

  constructor(
    private eventosService: EventosService,
    private espacioService: EspacioService,
    private organizacionesService: OrganizacionesService,
    private api: Api,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.listar();
    this.cargarListas();
  }

  listar() {
    this.eventosService.listar().subscribe({
      next: (data) => (this.eventos = data || []),
      error: () => this.showMessage('error', 'Error de Carga', 'No fue posible cargar eventos'),
    });
  }

  private cargarListas() {
    this.espacioService.listar().subscribe({
      next: (data) => {
        this.espaciosListado = (data || []).map((e: any) => ({ codigo: e.codigo, nombre: e.nombre }));
      }
    });
    this.organizacionesService.listar().subscribe({
      next: (data) => {
        this.organizacionesListado = (data || []).map((o: any) => ({ nit: o.nit, nombre: o.nombre }));
      }
    });
    this.api.getUsuarios().subscribe({
      next: (data) => {
        this.usuariosListado = (data || [])
          .filter((u: any) => {
            const rol = (u.rol || '').toString().toLowerCase();
            return rol === 'docente' || rol === 'estudiante';
          })
          .map((u: any) => ({ identificacion: u.identificacion, nombre: u.nombre, apellido: u.apellido }));
      }
    });
  }

  private validarFormulario(): string | null {
    // Validación de campos básicos
    if (!this.nuevoEvento.nombre?.trim()) {
      return 'El nombre del evento es obligatorio';
    }
    
    if (!this.nuevoEvento.fecha) {
      return 'La fecha del evento es obligatoria';
    }
    
    if (!this.nuevoEvento.hora_inicio) {
      return 'La hora de inicio es obligatoria';
    }
    
    if (!this.nuevoEvento.hora_fin) {
      return 'La hora de fin es obligatoria';
    }
    
    if (!this.nuevoEvento.tipo) {
      return 'El tipo de evento es obligatorio';
    }

    // Validar fecha no sea anterior a hoy
    const fechaEvento = new Date(this.nuevoEvento.fecha + 'T00:00:00');
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    
    if (fechaEvento < hoy) {
      return 'La fecha del evento debe ser igual o posterior a la fecha actual';
    }

    // Validar horas
    const horaInicio = this.nuevoEvento.hora_inicio;
    const horaFin = this.nuevoEvento.hora_fin;

    if (horaInicio === horaFin) {
      return 'La hora de inicio y la hora de fin no pueden ser iguales';
    }

    if (horaInicio >= horaFin) {
      return 'La hora de fin debe ser posterior a la hora de inicio';
    }

    // Validar espacios
    if (!this.selectedEspacios.length || this.selectedEspacios.every(e => !e)) {
      return 'Debe seleccionar al menos un espacio';
    }

    // Validar responsables
    if (!this.selectedResponsables.length || this.selectedResponsables.every(r => r.id === 0)) {
      return 'Debe haber al menos un responsable';
    }

    // Validar archivos PDF
    for (let i = 0; i < this.selectedOrganizaciones.length; i++) {
      const org = this.selectedOrganizaciones[i];
      if (org.aval && org.aval.type !== 'application/pdf') {
        return `El aval de la organización ${i + 1} debe ser un archivo PDF`;
      }
    }

    for (let i = 0; i < this.selectedResponsables.length; i++) {
      const resp = this.selectedResponsables[i];
      if (resp.aval && resp.aval.type !== 'application/pdf') {
        return `El aval del responsable ${i + 1} debe ser un archivo PDF`;
      }
    }

    return null;
  }

  crear() {
    // Validar formulario
    const errorValidacion = this.validarFormulario();
    if (errorValidacion) {
      this.showMessage('error', 'Error de Validación', errorValidacion);
      return;
    }

    const userId = this.auth.getUserId();
    if (!userId) {
      this.showMessage('error', 'Error de Sesión', 'Debes iniciar sesión para crear eventos');
      return;
    }

    // Construir organizaciones
    const organizaciones: OrganizacionDTO[] = this.selectedOrganizaciones
      .filter(org => org.nit)
      .map(org => ({
        nit: org.nit,
        representante_alterno: org.tipo === 'alterno' ? org.alterno : undefined,
        certificado_participacion: org.aval ? org.aval.name : undefined
      }));

    // Construir responsables
    const responsables: ResponsableDTO[] = this.selectedResponsables
      .filter(resp => resp.id > 0)
      .map(resp => ({
        id_usuario: resp.id,
        documentoAval: resp.aval ? resp.aval.name : undefined
        // NO enviar tipoAval si no es necesario
      }));

    // Construir reservaciones
    const reservaciones: ReservacionDTO[] = this.selectedEspacios
      .filter(espacio => espacio)
      .map(espacio => ({
        codigo_espacio: espacio,
        hora_inicio: this.nuevoEvento.hora_inicio + ':00', // Añadir segundos
        hora_fin: this.nuevoEvento.hora_fin + ':00' // Añadir segundos
      }));

    const payload: EventoRegistroCompleto = {
      nombre: this.nuevoEvento.nombre,
      descripcion: this.nuevoEvento.descripcion || '',
      tipo: this.nuevoEvento.tipo || 'Academico',
      fecha: this.nuevoEvento.fecha, // Formato: "YYYY-MM-DD"
      hora_inicio: this.nuevoEvento.hora_inicio + ':00', // Añadir segundos: "HH:mm:ss"
      hora_fin: this.nuevoEvento.hora_fin + ':00', // Añadir segundos: "HH:mm:ss"
      id_usuario_registra: userId,
      organizaciones,
      responsables,
      reservaciones
    };

    this.eventosService.registrar(payload).subscribe({
      next: (response) => {
        this.showMessage('success', '¡Registro Exitoso!', response?.mensaje || 'El evento ha sido registrado exitosamente');
        this.listar();
        this.closeModal();
      },
      error: (err) => {
        console.error('Error al registrar evento:', err);
        const mensajeError = err?.error?.mensaje || err?.error?.message || 'No fue posible registrar el evento';
        this.showMessage('error', 'Error al Registrar', mensajeError);
      }
    });
  }

  onSubmitCrearEvento(event: Event) {
    event.preventDefault();
    
    if (this.editMode && this.editCodigo != null) {
      // Validar formulario antes de editar
      const errorValidacion = this.validarFormulario();
      if (errorValidacion) {
        this.showMessage('error', 'Error de Validación', errorValidacion);
        return;
      }

      const form = new FormData();
      form.append('codigo', String(this.editCodigo));
      form.append('nombre', this.nuevoEvento.nombre || '');
      form.append('descripcion', this.nuevoEvento.descripcion || '');
      form.append('tipo', this.nuevoEvento.tipo || '');
      form.append('fecha', this.nuevoEvento.fecha || '');
      form.append('horaInicio', this.nuevoEvento.hora_inicio || '');
      form.append('horaFin', this.nuevoEvento.hora_fin || '');
      
      this.selectedEspacios.forEach(v => { if (v) form.append('espacios', v); });
      this.selectedResponsables.forEach(r => { 
        if (r.id) form.append('responsables', String(r.id)); 
        if (r.aval) form.append('avalResponsables', r.aval); 
      });
      this.selectedOrganizaciones.forEach(o => {
        if (o.nit) form.append('organizaciones', o.nit);
        form.append('representanteAlternoOrganizacion', o.tipo === 'alterno' ? (o.alterno || '') : '');
        if (o.aval) form.append('avalOrganizaciones', o.aval);
      });
      
      this.eventosService.editar(form).subscribe({
        next: () => {
          this.showMessage('success', '¡Actualización Exitosa!', 'El evento ha sido actualizado exitosamente');
          this.listar();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error al actualizar evento:', err);
          const mensajeError = err?.error?.mensaje || err?.error?.message || 'No fue posible actualizar el evento';
          this.showMessage('error', 'Error al Actualizar', mensajeError);
        }
      });
    } else {
      this.crear();
    }
  }

  openModal() {
    this.showModal = true;
    this.mensaje = '';
    if (!this.editMode) {
      // Limpiar formulario para nuevo evento
      this.nuevoEvento = {
        nombre: '',
        descripcion: '',
        tipo: 'Academico',
        fecha: '',
        hora_inicio: '',
        hora_fin: '',
        codigo_lugar: '',
        nit_organizacion: ''
      };
      this.selectedEspacios = [];
      this.selectedOrganizaciones = [];
      this.selectedResponsables = [];
      
      // Agregar el usuario actual como responsable por defecto
      const userId = this.auth.getUserId();
      if (userId) {
        this.selectedResponsables.push({ id: userId, aval: null });
      }
    }
  }

  closeModal() { 
    this.showModal = false; 
    this.editMode = false; 
    this.editCodigo = null; 
    this.mensaje = '';
    // Limpiar formulario
    this.nuevoEvento = {
      nombre: '',
      descripcion: '',
      tipo: 'Academico',
      fecha: '',
      hora_inicio: '',
      hora_fin: '',
      codigo_lugar: '',
      nit_organizacion: ''
    };
    this.selectedEspacios = [];
    this.selectedOrganizaciones = [];
    this.selectedResponsables = [];
  }

  // Métodos para el modal de mensajes
  showMessage(type: 'success' | 'error', title: string, message: string) {
    this.messageType = type;
    this.messageTitle = title;
    this.messageText = message;
    this.showMessageModal = true;
    
    // Auto cerrar después de 5 segundos
    setTimeout(() => {
      this.closeMessageModal();
    }, 5000);
  }

  closeMessageModal() {
    this.showMessageModal = false;
    this.messageText = '';
    this.messageTitle = '';
  }

  addEspacio() { 
    const availableEspacios = this.espaciosListado.filter(e => !this.selectedEspacios.includes(e.codigo));
    
    if (availableEspacios.length === 0) {
      this.showMessage('error', 'Sin Espacios Disponibles', 'No hay más espacios disponibles para agregar');
      return;
    }
    
    this.selectedEspacios.push(''); 
  }

  removeEspacio(i: number) { 
    this.selectedEspacios.splice(i, 1); 
  }

  addOrganizacion() { 
    const existingNits = this.selectedOrganizaciones.map(o => o.nit).filter(n => n);
    const availableOrgs = this.organizacionesListado.filter(o => !existingNits.includes(o.nit));
    
    if (availableOrgs.length === 0) {
      this.showMessage('error', 'Sin Organizaciones Disponibles', 'No hay más organizaciones disponibles para agregar');
      return;
    }
    
    this.selectedOrganizaciones.push({ nit: '', tipo: 'legal', alterno: '', aval: null }); 
  }

  removeOrganizacion(i: number) { 
    this.selectedOrganizaciones.splice(i, 1); 
  }

  addResponsable() { 
    const existingIds = this.selectedResponsables.map(r => r.id).filter(id => id > 0);
    const availableUsers = this.usuariosListado.filter(u => !existingIds.includes(u.identificacion));
    
    if (availableUsers.length === 0) {
      this.showMessage('error', 'Sin Usuarios Disponibles', 'No hay más usuarios disponibles para agregar como responsables');
      return;
    }
    
    this.selectedResponsables.push({ id: 0, aval: null }); 
  }

  removeResponsable(i: number) { 
    this.selectedResponsables.splice(i, 1); 
  }

  showOrgInline = false;
  orgInline: any = { nit: '', nombre: '', representante_legal: '', telefono: '', ubicacion: '', sector_economico: '', actividad_principal: '' };

  openOrgInlineModal() { 
    this.orgInline = {}; 
    this.showOrgInline = true; 
  }

  closeOrgInlineModal() { 
    this.showOrgInline = false; 
  }

  onSubmitOrgInline(event: Event) {
    event.preventDefault();
    const idUsuario = this.auth.getUserId(); 
    if (!idUsuario) { 
      this.showMessage('error', 'Error de Sesión', 'Debes iniciar sesión'); 
      return; 
    }
    const body = { ...this.orgInline, usuario: { identificacion: idUsuario } };
    this.organizacionesService.registrar(body).subscribe({
      next: () => { 
        this.selectedOrganizaciones.push({ nit: this.orgInline.nit, tipo: 'legal', alterno: '', aval: null }); 
        this.showOrgInline = false; 
        this.showMessage('success', '¡Éxito!', 'Organización creada y agregada al evento');
        this.cargarListas(); // Recargar lista de organizaciones
      },
      error: (err) => { 
        const mensajeError = err?.error?.mensaje || err?.error?.message || 'No se pudo crear organización';
        this.showMessage('error', 'Error al Crear Organización', mensajeError); 
      }
    });
  }

  nuevaOrganizacion() {
    this.router.navigate(['/organizaciones'], { queryParams: { nuevo: '1' } });
  }

  openEdit(e: any) {
    this.editMode = true;
    this.editCodigo = e?.codigo ?? null;
    this.selectedEspacios = [];
    this.selectedOrganizaciones = [];
    this.selectedResponsables = [];
    this.nuevoEvento = {
      nombre: e?.nombre || '',
      descripcion: e?.descripcion || '',
      tipo: e?.tipo || '',
      fecha: e?.fecha || '',
      hora_inicio: e?.horaInicio || e?.hora_inicio || '',
      hora_fin: e?.horaFin || e?.hora_fin || '',
      codigo_lugar: e?.codigo_lugar || '',
      nit_organizacion: e?.nit_organizacion || ''
    };
    if (this.editCodigo != null) {
      this.eventosService.obtenerDetalles(this.editCodigo).subscribe({
        next: (det) => {
          this.selectedOrganizaciones = (det.organizaciones || []).map(o => ({ 
            nit: o.nit, 
            tipo: o.representanteAlterno ? 'alterno' : 'legal', 
            alterno: o.representanteAlterno || '', 
            aval: null 
          }));
          this.selectedResponsables = (det.responsables || []).map(r => ({ id: r.idUsuario, aval: null }));
          this.selectedEspacios = (det.reservaciones || []).map((rv: any) => rv.codigoEspacio).filter(Boolean);
          this.openModal();
        },
        error: () => { this.openModal(); }
      });
    } else {
      this.openModal();
    }
  }

  onOrgTipoChange(i: number) {
    if (this.selectedOrganizaciones[i].tipo !== 'alterno') {
      this.selectedOrganizaciones[i].alterno = '';
    }
  }

  onOrgAvalChange(event: Event, i: number) {
    const file = (event.target as HTMLInputElement).files?.[0] || null;
    if (file && file.type !== 'application/pdf') {
      this.showMessage('error', 'Formato Incorrecto', 'El aval de organización debe ser un archivo PDF');
      (event.target as HTMLInputElement).value = '';
      return;
    }
    this.selectedOrganizaciones[i].aval = file;
  }

  onRespAvalChange(event: Event, i: number) {
    const file = (event.target as HTMLInputElement).files?.[0] || null;
    if (file && file.type !== 'application/pdf') {
      this.showMessage('error', 'Formato Incorrecto', 'El aval del responsable debe ser un archivo PDF');
      (event.target as HTMLInputElement).value = '';
      return;
    }
    this.selectedResponsables[i].aval = file;
  }
}