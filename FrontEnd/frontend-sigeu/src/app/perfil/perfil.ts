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
  edit = { celular: '', contrasenaActual: '', nuevaContrasena: '' };
  
  // Modal de mensajes
  showMessageModal = false;
  messageType: 'success' | 'error' = 'success';
  messageText = '';
  messageTitle = '';

  constructor(private auth: AuthService, private router: Router, private api: Api, private perfil: PerfilService) {}

  ngOnInit() {
    this.cargarPerfil();
  }

  private cargarPerfil() {
    const raw = localStorage.getItem('auth_user');
    this.usuario = raw ? JSON.parse(raw) : null;
    // Actualizar la vista previa de la imagen actual
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
    // Limpiar campos de edición
    this.edit = { celular: this.usuario?.celular || '', contrasenaActual: '', nuevaContrasena: '' };
    // Actualizar vista previa de imagen
    this.actualizarVistaPreviaImagen();
  }
  closeEdit() { 
    this.showEdit = false; 
    this.mensaje = '';
    // Limpiar campos de edición
    this.edit = { celular: '', contrasenaActual: '', nuevaContrasena: '' };
  }

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

  onLogout() {
    if (!confirm('¿Seguro que quieres cerrar sesión?')) return;
    this.auth.logoutRemote().subscribe({
      next: () => {
        this.auth.logout();
        this.showMessage('success', 'Sesión Cerrada', 'Has cerrado sesión correctamente');
        setTimeout(() => {
          this.router.navigateByUrl('/login');
        }, 1500);
      },
      error: () => {
        this.auth.logout();
        this.showMessage('success', 'Sesión Cerrada', 'Has cerrado sesión correctamente');
        setTimeout(() => {
          this.router.navigateByUrl('/login');
        }, 1500);
      }
    });
  }

  onSaveEdit() {
    if (!this.usuario?.identificacion) { 
      this.showMessage('error', 'Error de Usuario', 'No hay usuario cargado'); 
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
        this.showMessage('success', '¡Éxito!', 'Perfil actualizado correctamente'); 
        // Actualizar el usuario en localStorage con los datos actualizados
        if (usuarioActualizado) {
          localStorage.setItem('auth_user', JSON.stringify(usuarioActualizado));
          this.usuario = usuarioActualizado;
        }
        this.closeEdit(); 
        this.cargarPerfil();
        // Limpiar campos de edición
        this.edit = { celular: '', contrasenaActual: '', nuevaContrasena: '' };
      },
      error: (err) => { 
        console.error('Error', err); 
        this.showMessage('error', 'Error al Actualizar', err?.error?.message || err?.error?.mensaje || 'No se pudo actualizar el perfil'); 
      }
    });
  }
}