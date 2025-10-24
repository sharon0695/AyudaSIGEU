import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Api } from '../services/usuarios.service'; 
import { PerfilService } from '../services/perfil.service';

@Component({
  selector: 'app-perfil',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class Perfil {
  mensaje = '';
  usuario: any = null;
  showEdit = false;
  edit = { celular: '', contrasenaActual: '', nuevaContrasena: '' };

  constructor(private auth: AuthService, private router: Router, private api: Api, private perfil: PerfilService) {}

  ngOnInit() {
    this.cargarPerfil();
  }

  private cargarPerfil() {
    const raw = localStorage.getItem('auth_user');
    this.usuario = raw ? JSON.parse(raw) : null;
    this.actualizarVistaPreviaImagen();
  }

  private actualizarVistaPreviaImagen() {
    const preview = document.getElementById('preview-avatar') as HTMLImageElement;
    if (preview && this.usuario?.fotoPerfil) {
      preview.src = `/api/usuarios/foto/${this.usuario.identificacion}`;
    } else if (preview) {
      preview.src = 'img/perfil.png';
    }
  }

  openEdit() { 
    this.showEdit = true; 
    this.edit = { celular: this.usuario?.celular || '', contrasenaActual: '', nuevaContrasena: '' };
    this.actualizarVistaPreviaImagen();
  }
  closeEdit() { 
    this.showEdit = false; 
    this.mensaje = '';
    this.edit = { celular: '', contrasenaActual: '', nuevaContrasena: '' };
  }

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
  if (!this.usuario?.identificacion) { 
    this.mensaje = 'No hay usuario cargado'; 
    return; 
  }

  const fileInput = document.getElementById('new-avatar') as HTMLInputElement | null;
  const foto = fileInput?.files?.[0];

  this.perfil.actualizarPerfil(this.usuario.identificacion, {
    contrasenaActual: this.edit.contrasenaActual || undefined,
    nuevaContrasena: this.edit.nuevaContrasena || undefined,
    celular: this.edit.celular || undefined,
    fotoFile: foto || undefined,
  }).subscribe({
    next: (usuarioActualizado) => {
      // Mostrar mensaje de éxito con la misma animación visual
      this.mensaje = 'Perfil actualizado correctamente';

      // Actualizar datos en localStorage y en el perfil
      if (usuarioActualizado) {
        localStorage.setItem('auth_user', JSON.stringify(usuarioActualizado));
        this.usuario = usuarioActualizado;
      }

      this.closeEdit();
      this.cargarPerfil();
      this.edit = { celular: '', contrasenaActual: '', nuevaContrasena: '' };

      // Ocultar el mensaje automáticamente después de 3 segundos
      setTimeout(() => this.mensaje = '', 3000);
    },
    error: (err) => {
      // Mostrar mensaje de error usando la misma clase CSS (roja)
      this.mensaje = err?.error?.message || err?.error?.mensaje || 'Error al actualizar el perfil';
      setTimeout(() => this.mensaje = '', 3000);
    }
  });
}

}