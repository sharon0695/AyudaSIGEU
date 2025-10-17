import { Component } from '@angular/core';
import { Api, UsuarioRegistroDto } from '../services/usuarios.service';
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
    if (rol === 'estudiante' && (!this.usuario.codigo || !this.usuario.codigo_programa)) {
      this.mensaje = 'Para estudiante, código e ID de programa son obligatorios';
      return;
    }
    if (rol === 'docente' && !this.usuario.codigo_unidad) {
      this.mensaje = 'Para docente, el ID de unidad académica es obligatorio';
      return;
    }
    if (rol === 'secretaria_academica' && !this.usuario.id_Facultad) {
      this.mensaje = 'Para secretaría académica, el ID de facultad es obligatorio';
      return;
    }

    const { confirmar_contrasena, ...formValues } = this.usuario;

    const payload: UsuarioRegistroDto = {
      identificacion: Number(formValues.identificacion),
      nombre: formValues.nombre,
      apellido: formValues.apellido,
      correoInstitucional: formValues.correo,
      contrasena: formValues.contrasena,
      // Mapear rol UI -> enum backend exacto
      rol: this.mapRol(formValues.rol),
      codigo: formValues.codigo ? Number(formValues.codigo) : undefined,
      codigoPrograma: formValues.codigo_programa ? Number(formValues.codigo_programa) : undefined,
      codigoUnidad: formValues.codigo_unidad ? Number(formValues.codigo_unidad) : undefined,
      idFacultad: formValues.id_Facultad ? Number(formValues.id_Facultad) : undefined,
    };

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