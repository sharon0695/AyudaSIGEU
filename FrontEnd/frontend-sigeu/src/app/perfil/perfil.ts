import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Api } from '../services/usuarios.service'; 
import { PerfilService } from '../services/perfil.service';

@Component({
  selector: 'app-perfil',
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class Perfil {
  mensaje = '';
  usuario: any = null;
  showEdit = false;
  edit = { celular: '', contrasena: '' };

  constructor(private auth: AuthService, private router: Router, private api: Api, private perfil: PerfilService) {}

  ngOnInit() {
    this.cargarPerfil();
  }

  private cargarPerfil() {
    const raw = localStorage.getItem('auth_user');
    this.usuario = raw ? JSON.parse(raw) : null;
  }

  openEdit() { this.showEdit = true; }
  closeEdit() { this.showEdit = false; }

  onLogout() {
    if (!confirm('¿Seguro que quieres cerrar sesión?')) return;
    this.auth.logoutRemote().subscribe({
      next: () => {
        this.auth.logout();
        this.mensaje = 'Sesión cerrada';
        this.router.navigateByUrl('/login');
      },
      error: () => {
        this.auth.logout();
        this.router.navigateByUrl('/login');
      }
    });
  }

  onSaveEdit() {
    if (!this.usuario?.identificacion) { this.mensaje = 'No hay usuario cargado'; return; }
    const fileInput = document.getElementById('new-avatar') as HTMLInputElement | null;
    const foto = fileInput?.files?.[0];  
    this.perfil.actualizarPerfil(this.usuario.identificacion, {
      contrasena: this.edit.contrasena || undefined,
      celular: this.edit.celular || undefined,
      fotoFile: foto || undefined,
    }).subscribe({
      next: () => { this.mensaje = 'Perfil actualizado'; this.closeEdit(); this.cargarPerfil(); },
      error: (err) => { console.error('Error', err); this.mensaje = err?.error?.mensaje || 'No se pudo actualizar'; }
    });
  }
}