import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventosService, Evento } from '../services/eventos.service';
import { Router, RouterLink } from '@angular/router';
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

  espaciosListado: Array<{ codigo: string; nombre?: string }> = [];
  organizacionesListado: Array<{ nit: string; nombre?: string }> = [];
  usuariosListado: Array<{ identificacion: number; nombre?: string; apellido?: string }> = [];
  
  selectedEspacios: string[] = [];
  selectedOrganizaciones: string[] = [];
  selectedResponsables: number[] = [];

  
  constructor(private events: EventosService,
    private espacioService: EspacioService,
    private organizacionesService: OrganizacionesService,
    private api: Api,
    private auth: AuthService,
    private router: Router
  ) {}

  showModal = false;

  ngOnInit() { 
    this.listar(); 
    this.cargarListas();
  }

  private cargarListas() {
    this.espacioService.listar().subscribe({ next: (data) => {
      this.espaciosListado = (data || []).map((e: any) => ({ codigo: e.codigo, nombre: e.nombre }));
    }});
    this.organizacionesService.listar().subscribe({ next: (data) => {
      this.organizacionesListado = (data || []).map((o: any) => ({ nit: o.nit, nombre: o.nombre }));
    }});
    this.api.getUsuarios().subscribe({ next: (data) => {
      this.usuariosListado = (data || []).map((u: any) => ({ identificacion: u.identificacion, nombre: u.nombre, apellido: u.apellido }));
    }});
  }

  listar() { this.events.listar().subscribe({
      next: (data) => (this.eventos = data || []),
      error: () => (this.mensaje = 'No fue posible cargar organizaciones'),
    }); }

  crear(evento: Evento) {
    this.events.registrar(evento).subscribe({ next: () => this.listar(), error: () => (this.mensaje = 'No fue posible registrar el evento') });
  }

  onSubmitCrearEvento(event: Event) {
    event.preventDefault();
    this.crear(this.nuevoEvento);
    this.showModal = false;
  }
  openModal() {
    this.showModal = true;
    const userId = this.auth.getUserId();
    if (userId && !this.selectedResponsables.length) {
      this.selectedResponsables.push(userId);
    }
  }
  closeModal() { this.showModal = false; }

  addEspacio() { this.selectedEspacios.push(''); }
  removeEspacio(i: number) { this.selectedEspacios.splice(i, 1); }
  addOrganizacion() { this.selectedOrganizaciones.push(''); }
  removeOrganizacion(i: number) { this.selectedOrganizaciones.splice(i, 1); }
  addResponsable() { this.selectedResponsables.push(0); }
  removeResponsable(i: number) { this.selectedResponsables.splice(i, 1); }
  nuevaOrganizacion() { this.router.navigate(['/organizaciones'], { queryParams: { nuevo: '1' } }); }
}