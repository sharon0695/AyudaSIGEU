import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Api } from '../services/usuarios.service';
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
  edit = { celular: '', contrasena: '' };

  constructor(private auth: AuthService, private router: Router, private api: Api) {}

  ngOnInit() { this.cargarPerfil(); }

  private cargarPerfil() {
    const raw = localStorage.getItem('auth_user');
    this.usuario = raw ? JSON.parse(raw) : null;
  }

  openEdit() { this.showEdit = true; }
  closeEdit() { this.showEdit = false; }

  onLogout() {
    this.auth.logoutRemote().subscribe({
      next: () => { this.auth.logout(); this.mensaje = 'Sesión cerrada'; this.router.navigateByUrl('/login'); },
      error: () => { this.auth.logout(); this.router.navigateByUrl('/login'); }
    });
  }
}