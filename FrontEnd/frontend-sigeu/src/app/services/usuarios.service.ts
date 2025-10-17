import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { buildApiUrl, API_PATHS } from '../config/config';

export interface UsuarioRegistroDto {
  identificacion: number;
  nombre: string;
  apellido: string;
  correoInstitucional: string;
  contrasena: string;
  rol: string; // Debe coincidir exactamente con enum backend
  codigo?: number;
  codigoPrograma?: number;
  codigoUnidad?: number;
  idFacultad?: number;
}

@Injectable({
  providedIn: 'root'
})
export class Api {
  private baseUrl = buildApiUrl(API_PATHS.usuarios); 

  constructor(private http: HttpClient) { }

  registrarUsuario(payload: UsuarioRegistroDto): Observable<any>{
    return this.http.post(`${this.baseUrl}/registrar`, payload);
  }

  getUsuarios(): Observable<any> {
    return this.http.get(`${this.baseUrl}/listar`);
  }

  recuperarContrasena(correo: string): Observable<any> {
    const params = new HttpParams().set('correo', correo);
    return this.http.post(`${this.baseUrl}/recuperar`, null, { params, responseType: 'text' as 'json' });
  }
}