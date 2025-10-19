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
      error: () => (this.mensaje = 'No fue posible cargar organizaciones'),
    });
  }

  buscar() {
    if (!this.nombreBusqueda) { this.organizaciones = this.organizacionesTodas.slice(); return; }
    const term = this.nombreBusqueda.trim().toLowerCase();
    this.organizaciones = this.organizacionesTodas.filter(o => (o?.nombre || '').toLowerCase() === term);
    if (!this.organizaciones.length) {
      this.organizaciones = this.organizacionesTodas.filter(o => (o?.nombre || '').toLowerCase().includes(term));
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
    if (!idUsuario) { this.mensaje = 'Debes iniciar sesión'; return; }
    const existe = this.organizaciones.find(o => o.nit === this.newOrg.nit);
    if (existe) {
      this.orgs.editar(this.newOrg.nit, idUsuario, this.newOrg).subscribe({
        next: () => { this.mensaje = 'Organización actualizada'; this.listar(); this.showModal = false; },
        error: (err) => { this.mensaje = err?.error?.mensaje || 'No se pudo actualizar'; }
      });
    } else {
      const body = { ...this.newOrg, usuario: { identificacion: idUsuario } };
      this.orgs.registrar(body).subscribe({
        next: () => { this.mensaje = 'Organización registrada'; this.listar(); this.showModal = false; },
        error: (err) => { this.mensaje = err?.error?.mensaje || 'No se pudo registrar'; }
      });
    }
  }
  openModal() { this.showModal = true; }
  closeModal() { this.showModal = false; }

  onEliminar(org: any) {
    if (!org?.nit) { this.mensaje = 'Organización inválida'; return; }
    const confirmacion = confirm(`¿Eliminar la organización ${org.nombre || org.nit}?`);
    if (!confirmacion) { this.mensaje = 'Operación cancelada'; return; }
    const idUsuario = this.auth.getUserId();
    if (!idUsuario) { this.mensaje = 'Debes iniciar sesión para eliminar organizaciones'; return; }
    this.orgs.eliminar(org.nit, idUsuario).subscribe({
      next: () => { this.listar(); this.mensaje = 'Organización eliminada correctamente';},
      error: (err) => { const msg = err?.error?.mensaje || err?.error || 'No se pudo eliminar la organización'; this.mensaje = msg; }
    });
  }

  onVisualizar(org: any) { this.viewOrg = { ...org }; this.showViewModal = true; }
  closeViewModal() { this.showViewModal = false; this.viewOrg = null; }

  onEditar(org: any) {
  const idUsuario = this.auth.getUserId();
  if (!idUsuario) { this.mensaje = 'Debes iniciar sesión'; return; }
  if (org?.usuario?.identificacion && org.usuario.identificacion !== idUsuario) {
    this.mensaje = 'No tienes permisos para editar esta organización';
    return;
  }
  this.newOrg = {
    nit: org.nit || '',
    nombre: org.nombre || '',
    representante_legal: org.representante_legal || '',
    ubicacion: org.ubicacion || '',
    telefono: org.telefono || '',
    sector_economico: org.sector_economico || '',
    actividad_principal: org.actividad_principal || ''
  };
  this.showModal = true;
}
}