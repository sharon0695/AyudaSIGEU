import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Api } from '../services/usuarios.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-recuperar-contrasena',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './recuperar-contrasena.html',
  styleUrl: './css/main.css'
})
export class RecuperarContrasena {
  correo = '';
  mensaje = '';

  constructor(private api: Api) {}

  onSubmit(event: Event) {
    event.preventDefault();
    console.log('Intentando enviar...')
    console.log('Correo actual:', this.correo);
    console.log('API base:', this.api['baseUrl']);
    this.api.recuperarContrasena(this.correo).subscribe({
      next: (res: any) => (this.mensaje = res || 'Correo enviado'),
      error: (err) => { console.error('Error', err);
        this.mensaje = 'No se pudo  enviar el correo';      
      },
    });
  }
}