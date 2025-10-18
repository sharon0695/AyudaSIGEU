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
        // El backend devuelve String; ajusta si cambia
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

  onSubmitRegistrarOrg(event: Event) {
    event.preventDefault();
    this.registrarNueva(this.newOrg);
  }
}