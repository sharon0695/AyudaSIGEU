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

  ngOnInit() {
    this.listar();
  }

  listar() {
    this.orgs.listar().subscribe({
      next: (data) => (this.organizaciones = data || []),
      error: () => (this.mensaje = 'No fue posible cargar organizaciones'),
    });
  }

  buscar() {
    if (!this.nombreBusqueda) return this.listar();
    this.orgs.buscarPorNombre(this.nombreBusqueda).subscribe({
      next: (res) => {
        this.organizaciones = res ? [{ nombre: res }] : [];
      },
      error: () => (this.mensaje = 'Búsqueda fallida'),
    });
  }

  registrarNueva(org: any) {
    const idUsuario = this.auth.getUserId();
    if (!idUsuario) { this.mensaje = 'Debes iniciar sesión para registrar organizaciones'; return; }
    const body = { ...org, usuario: { identificacion: idUsuario } };
    this.orgs.registrar(body).subscribe({ next: () => this.listar(), error: () => (this.mensaje = 'No fue posible registrar la organización') });
  }
  showModal = false;

  onSubmitRegistrarOrg(event: Event) {
    event.preventDefault();
    this.registrarNueva(this.newOrg);
    this.showModal = false;
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
      next: () => { this.mensaje = 'Organización eliminada correctamente'; this.listar(); },
      error: (err) => {
        const msg = err?.error?.mensaje || err?.error || 'No se pudo eliminar la organización';
        this.mensaje = msg;
      }
    });
  }

  onVisualizar(org: any) {
    const detalle = `NIT: ${org.nit}\nNombre: ${org.nombre}\nRepresentante: ${org.representante_legal}\nUbicación: ${org.ubicacion}\nTeléfono: ${org.telefono || '-'}\nSector: ${org.sector_economico || '-'}\nActividad: ${org.actividad_principal || '-'}`;
    alert(detalle);
  }

  onEditar(org: any) {
    this.newOrg = { ...org };
    this.showModal = true;
  }

}