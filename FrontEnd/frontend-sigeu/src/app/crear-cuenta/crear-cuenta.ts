import { Component } from '@angular/core';
import { Api } from '../services/api';
import { Validators, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-crear-cuenta',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-cuenta.html',
  styleUrl: './crear-cuenta.css'
})
export class CrearCuenta {
  usuario = {
    identificacion: '',
    nombre: '',
    apellido: '',
    correo: '',
    contrasena: '',
    confirmar_contrasena: '',
    rol: '',
    codigo: '',
    id_Facultad: '',
    codigo_programa: '',
    codigo_unidad: '',
  };

  mensaje: string = '';

  constructor(private apiService: Api) {}

  onRolChange(nuevoRol: string) {
    this.usuario.rol = nuevoRol;
    this.usuario.codigo = '';
    this.usuario.codigo_programa = '';
    this.usuario.codigo_unidad = '';
    this.usuario.id_Facultad = '';
  }

  getSelectValue(event: Event): string {
    return (event.target as HTMLSelectElement).value;
  }

  registrar() {
  
    if (this.usuario.contrasena !== this.usuario.confirmar_contrasena) {
      this.mensaje = 'Las contraseñas no coinciden.';
      return;
    }
 
    const { confirmar_contrasena, ...usuarioAEnviar } = this.usuario;

    this.apiService.registrarUsuario(usuarioAEnviar).subscribe({
      next: (respuesta) => {
        this.mensaje = 'Usuario registrado exitosamente';
      },
      error: (err) => {
        this.mensaje = 'Error al registrar usuario';
      }
    });
  }
}
