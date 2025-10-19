import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

@Injectable({ providedIn: 'root' })
export class OrganizacionesService {
  private baseUrl = buildApiUrl(API_PATHS.organizacionesExternas);
  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
    return this.http.get(`${this.baseUrl}/listar`);
  }

  registrar(body: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/registrar`, body);
  }

  buscarPorNombre(nombre: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/buscar/${encodeURIComponent(nombre)}`);
  }

  editar(nit: string, idUsuarioEditor: number, body: any): Observable<any> {
    const params = new HttpParams().set('idUsuarioEditor', String(idUsuarioEditor));
    return this.http.put(`${this.baseUrl}/editar/${encodeURIComponent(nit)}`, body, { params });
  }

  obtenerPorNit(nit: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/${encodeURIComponent(nit)}`);
  }

  eliminar(nit: string, solicitanteId: number): Observable<any> {
    const params = new HttpParams().set('solicitanteId', String(solicitanteId));
    return this.http.delete(`${this.baseUrl}/eliminar/${encodeURIComponent(nit)}`, { params });
  }
}