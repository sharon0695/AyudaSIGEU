import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { buildApiUrl, API_PATHS } from '../config/config';

export interface usuario{
  identificacion: number,
  nombre: string,
  apellido: string,
  contrasena: string,
  rol: string,
  codigo?: number,
  codigo_programa?: number,
  codigo_unidad?: number,
  id_facultad?: number
}

@Injectable({
  providedIn: 'root'
})
export class Api {
  private baseUrl = buildApiUrl(API_PATHS.usuarios); 

  constructor(private http: HttpClient) { }

  registrarUsuario(usuario: any): Observable<any>{
    return this.http.post(`${this.baseUrl}/registrar`, usuario);
  }

  getUsuarios(): Observable<any> {
    return this.http.get(`${this.baseUrl}/usuarios`);
  }
}
