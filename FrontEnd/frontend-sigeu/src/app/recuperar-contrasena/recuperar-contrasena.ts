import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Api } from '../services/usuarios.service';

@Component({
  selector: 'app-recuperar-contrasena',
  imports: [CommonModule, FormsModule],
  templateUrl: './recuperar-contrasena.html',
  styleUrl: './css/main.css'
})
export class RecuperarContrasena {
  correo = '';
  mensaje = '';

  constructor(private api: Api) {}

  onSubmit(event: Event) {
    event.preventDefault();
    this.api.recuperarContrasena(this.correo).subscribe({
      next: (res: any) => (this.mensaje = res || 'Correo enviado'),
      error: () => (this.mensaje = 'No se pudo enviar el correo'),
    });
  }
}