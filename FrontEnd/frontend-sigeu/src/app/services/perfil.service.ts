import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

@Injectable({ providedIn: 'root' })
export class PerfilService {
  private baseUrl = buildApiUrl(API_PATHS.usuarios);
  constructor(private http: HttpClient) {}

  actualizarPerfil(
    identificacion: number,
    changes: { contrasenaActual?: string; nuevaContrasena?: string; celular?: string; fotoFile?: File }
  ): Observable<any> {
    const form = new FormData();
    form.append('identificacion', String(identificacion));
    if (changes.contrasenaActual) form.append('contrasenaActual', changes.contrasenaActual);
    if (changes.nuevaContrasena) form.append('contrasenaNueva', changes.nuevaContrasena);
    if (changes.celular) form.append('celular', changes.celular);
    if (changes.fotoFile) form.append('fotoPerfil', changes.fotoFile);
    return this.http.put(`${this.baseUrl}/editarPerfil`, form);
  }
}