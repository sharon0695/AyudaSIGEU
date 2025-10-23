import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrganizacionesService } from '../services/organizaciones.service';
import { AuthService } from '../services/auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-organizaciones',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './organizaciones.html',
  styleUrl: './organizaciones.css'
})
export class Organizaciones {
  organizaciones: any[] = [];
  organizacionesTodas: any[] = [];
  nombreBusqueda = '';
  mensaje = '';
  newOrg: any = {
    nit: '',
    nombre: '',
    representante_legal: '',
    ubicacion: '',
    telefono: '',
    sector_economico: '',
    actividad_principal: ''
  };

  // Modal de mensajes
  showMessageModal = false;
  messageType: 'success' | 'error' = 'success';
  messageText = '';
  messageTitle = '';
  editMode: boolean = false;
  originalNit: string | null = null;


  constructor(private orgs: OrganizacionesService, private auth: AuthService) {}
  showModal = false;
  showViewModal = false;
  viewOrg: any;

  ngOnInit() {
    this.listar();
  }

  listar() {
    this.orgs.listar().subscribe({
      next: (data) => { this.organizaciones = data || []; this.organizacionesTodas = this.organizaciones.slice(); },
      error: () => this.showMessage('error', 'Error de Carga', 'No fue posible cargar organizaciones'),
    });
  }

  buscar() {
    if (!this.nombreBusqueda) { this.organizaciones = this.organizacionesTodas.slice(); return; }
    const term = this.nombreBusqueda.trim().toLowerCase();
    this.organizaciones = this.organizacionesTodas.filter(o => (o?.nombre || '').toLowerCase() === term);
    if (!this.organizaciones.length) {
      this.organizaciones = this.organizacionesTodas.filter(o => (o?.nombre || '').toLowerCase().includes(term));
    }
    if (!this.organizaciones.length) {
      this.showMessage('error', 'Sin Resultados', 'No se encontraron organizaciones con ese nombre');
    }
  }

  registrarNueva(org: any) {
    const idUsuario = this.auth.getUserId();
    if (!idUsuario) { this.mensaje = 'Debes iniciar sesión para registrar organizaciones'; return; }
    const body = { ...org, usuario: { identificacion: idUsuario } };
    this.orgs.registrar(body).subscribe({ next: () => this.listar(), error: () => (this.mensaje = 'No fue posible registrar la organización') });
  }  

  onSubmitRegistrarOrg(event: Event) {
    event.preventDefault();
    const idUsuario = this.auth.getUserId();
    if (!idUsuario) { 
      this.showMessage('error', 'Error de Sesión', 'Debes iniciar sesión'); 
      return; 
    }
    const existe = this.organizaciones.find(o => o.nit === this.newOrg.nit);
    if (existe) {
      this.orgs.editar(this.originalNit!, idUsuario, this.newOrg).subscribe({
        next: () => { 
          this.showMessage('success', '¡Éxito!', 'Organización actualizada correctamente'); 
          this.listar(); 
          this.showModal = false; 
        },
        error: (err) => { 
          this.showMessage('error', 'Error al Actualizar', err?.error?.mensaje || 'No se pudo actualizar la organización'); 
        }
      });
    } else {
      const body = { ...this.newOrg, usuario: { identificacion: idUsuario } };
      this.orgs.registrar(body).subscribe({
        next: () => { 
          this.showMessage('success', '¡Éxito!', 'Organización registrada correctamente'); 
          this.listar(); 
          this.showModal = false; 
        },
        error: (err) => { 
          this.showMessage('error', 'Error al Registrar', err?.error?.mensaje || 'No se pudo registrar la organización'); 
        }
      });
    }
  }
  openModal() { this.showModal = true; }
  closeModal() { this.showModal = false; }

  // Métodos para el modal de mensajes
  showMessage(type: 'success' | 'error', title: string, message: string) {
    this.messageType = type;
    this.messageTitle = title;
    this.messageText = message;
    this.showMessageModal = true;
  }

  closeMessageModal() {
    this.showMessageModal = false;
    this.messageText = '';
    this.messageTitle = '';
  }

  onEliminar(org: any) {
    if (!org?.nit) { 
      this.showMessage('error', 'Error de Validación', 'Organización inválida'); 
      return; 
    }
    const confirmacion = confirm(`¿Eliminar la organización ${org.nombre || org.nit}?`);
    if (!confirmacion) { 
      this.showMessage('error', 'Operación Cancelada', 'La eliminación fue cancelada'); 
      return; 
    }
    const idUsuario = this.auth.getUserId();
    if (!idUsuario) { 
      this.showMessage('error', 'Error de Sesión', 'Debes iniciar sesión para eliminar organizaciones'); 
      return; 
    }
    this.orgs.eliminar(org.nit, idUsuario).subscribe({
      next: () => {
        this.showMessage('success', '¡Éxito!', 'Organización eliminada correctamente');
        this.listar();
      },
      error: (err) => { 
        const msg = err?.error?.mensaje || err?.error || 'No se pudo eliminar la organización'; 
        this.showMessage('error', 'Error al Eliminar', msg); 
      }
    });
  }

  onVisualizar(org: any) { this.viewOrg = { ...org }; this.showViewModal = true; }
  closeViewModal() { this.showViewModal = false; this.viewOrg = null; this.editMode = false; this.originalNit = null;
    this.newOrg = {
      nit: '',
      nombre: '',
      representante_legal: '',
      ubicacion: '',
      telefono: '',
      sector_economico: '',
      actividad_principal: ''
    };
  }

  onEditar(org: any) {
  const idUsuario = this.auth.getUserId();
  if (!idUsuario) { 
    this.showMessage('error', 'Error de Sesión', 'Debes iniciar sesión'); 
    return; 
  }
  if (org?.usuario?.identificacion && org.usuario.identificacion !== idUsuario) {
    this.showMessage('error', 'Sin Permisos', 'No tienes permisos para editar esta organización');
    return;
  }
  this.newOrg = JSON.parse(JSON.stringify(org));  
  this.editMode = true;
  this.originalNit = org.nit;
  this.showModal = true;
}
}