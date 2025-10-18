import { Component } from '@angular/core';
import { Api, UsuarioRegistroDto } from '../services/usuarios.service';
import { Validators, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-crear-cuenta',
  imports: [CommonModule, FormsModule, RouterLink],
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
    idFacultad: '',
    codigoPrograma: '',
    codigoUnidad: '',
  };

  mensaje: string = '';

  constructor(private apiService: Api) {}

  onRolChange(nuevoRol: string) {
    this.usuario.rol = nuevoRol;
    this.usuario.codigo = '';
    this.usuario.codigoPrograma = '';
    this.usuario.codigoUnidad = '';
    this.usuario.idFacultad = '';
  }

  getSelectValue(event: Event): string {
    return (event.target as HTMLSelectElement).value;
  }

  registrar() {
    // Validaciones en cliente alineadas con backend
    if (!this.usuario.correo.endsWith('@uao.edu.co')) {
      this.mensaje = 'Debes usar tu correo institucional @uao.edu.co';
      return;
    }
    if (this.usuario.contrasena !== this.usuario.confirmar_contrasena) {
      this.mensaje = 'Las contraseñas no coinciden.';
      return;
    }
    // Reglas según rol
    const rol = this.mapRol(this.usuario.rol);
    if (rol === 'estudiante' && (!this.usuario.codigo || !this.usuario.codigoPrograma)) {
      this.mensaje = 'Para estudiante, código e ID de programa son obligatorios';
      return;
    }
    if (rol === 'docente' && !this.usuario.codigoUnidad) {
      this.mensaje = 'Para docente, el ID de unidad académica es obligatorio';
      return;
    }
    if (rol === 'secretaria_academica' && !this.usuario.idFacultad) {
      this.mensaje = 'Para secretaría académica, el ID de facultad es obligatorio';
      return;
    }

    const { confirmar_contrasena, ...formValues } = this.usuario;

    const toNum = (v: any) => (v === null || v === undefined || v === '' ? undefined : Number(v));
    const payload: UsuarioRegistroDto = {
      identificacion: Number(formValues.identificacion),
      nombre: formValues.nombre,
      apellido: formValues.apellido,
      correoInstitucional: formValues.correo,
      contrasena: formValues.contrasena,
      rol: this.mapRol(formValues.rol),
      codigo: toNum(formValues.codigo),
      codigoPrograma: formValues.codigoPrograma,   
      codigoUnidad: formValues.codigoUnidad,       
      idFacultad: formValues.idFacultad,           
    };
    console.log(payload);
    this.apiService.registrarUsuario(payload).subscribe({
      next: () => {
        this.mensaje = 'Usuario registrado exitosamente';
      },
      error: (err) => {
        const backendMsg = err?.error?.mensaje || err?.error?.message || 'Error al registrar usuario';
        this.mensaje = backendMsg;
      }
    });
  }

  private mapRol(rolUi: string): string {
    // Backend enum: estudiante, docente, secretaria_academica, administrador
    if (rolUi === 'estudiante') return 'estudiante';
    if (rolUi === 'docente') return 'docente';
    if (rolUi === 'secretaria') return 'secretaria_academica';
    return rolUi;
  }
}