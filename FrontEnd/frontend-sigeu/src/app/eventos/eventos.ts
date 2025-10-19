import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EventosService, Evento } from '../services/eventos.service';
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
      error: () => (this.mensaje = 'No fue posible cargar eventos'),
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

  crear(evento: Evento) {
    const payload = {
      nombre: this.nuevoEvento.nombre,
      descripcion: this.nuevoEvento.descripcion,
      tipo: this.nuevoEvento.tipo,
      fecha: this.nuevoEvento.fecha,
      horaInicio: this.nuevoEvento.hora_inicio,
      horaFin: this.nuevoEvento.hora_fin,
      codigoLugar: this.selectedEspacios[0] || this.nuevoEvento.codigo_lugar,
      nitOrganizacion: (this.selectedOrganizaciones[0]?.nit) || this.nuevoEvento.nit_organizacion,
    };
    this.eventosService.registrar(payload).subscribe({
      next: () => this.listar(),
      error: () => (this.mensaje = 'No fue posible registrar el evento'),
    });
  }

  onSubmitCrearEvento(event: Event) {
    event.preventDefault();
    if (this.editMode && this.editCodigo != null) {
      const body = {
        ...this.nuevoEvento,
        codigoLugar: this.selectedEspacios[0] || this.nuevoEvento.codigo_lugar,
        nitOrganizacion: this.selectedOrganizaciones[0]?.nit || this.nuevoEvento.nit_organizacion,
        horaInicio: this.nuevoEvento.hora_inicio,
        horaFin: this.nuevoEvento.hora_fin,
      };
      this.eventosService.editar(body).subscribe({
        next: () => this.listar(),
        error: () => (this.mensaje = 'No fue posible actualizar el evento')
      });
    } else {
      this.crear(this.nuevoEvento);
    }
    this.showModal = false;
  }

  openModal() {
    this.showModal = true;
    const userId = this.auth.getUserId();
    if (userId && !this.selectedResponsables.length) {
      this.selectedResponsables.push({ id: userId, aval: null });
    }
  }

  closeModal() { this.showModal = false; }

  addEspacio() { this.selectedEspacios.push(''); }
  removeEspacio(i: number) { this.selectedEspacios.splice(i, 1); }
  addOrganizacion() { this.selectedOrganizaciones.push({ nit: '', tipo: 'legal', alterno: '', aval: null }); }
  removeOrganizacion(i: number) { this.selectedOrganizaciones.splice(i, 1); }
  addResponsable() { this.selectedResponsables.push({ id: 0, aval: null }); }
  removeResponsable(i: number) { this.selectedResponsables.splice(i, 1); }

  nuevaOrganizacion() {
    this.router.navigate(['/organizaciones'], { queryParams: { nuevo: '1' } });
  }

  openEdit(e: any) {
    this.editMode = true;
    this.editCodigo = e?.codigo ?? null;
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
    this.openModal();
  }
  onOrgTipoChange(i: number) {
    if (this.selectedOrganizaciones[i].tipo !== 'alterno') {
      this.selectedOrganizaciones[i].alterno = '';
    }
  }
  onOrgAvalChange(event: Event, i: number) {
    const file = (event.target as HTMLInputElement).files?.[0] || null;
    if (file && file.type !== 'application/pdf') {
      this.mensaje = 'El aval de organización debe ser un PDF';
      (event.target as HTMLInputElement).value = '';
      return;
    }
    this.selectedOrganizaciones[i].aval = file;
  }
  onRespAvalChange(event: Event, i: number) {
  const file = (event.target as HTMLInputElement).files?.[0] || null;
  if (file && file.type !== 'application/pdf') {
    this.mensaje = 'El aval del responsable debe ser un PDF';
    (event.target as HTMLInputElement).value = '';
    return;
  }
  this.selectedResponsables[i].aval = file;
}
}