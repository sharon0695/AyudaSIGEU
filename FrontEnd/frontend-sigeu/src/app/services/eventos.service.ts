import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

export interface Evento {
  id?: number;
  nombre?: string;
}

@Injectable({ providedIn: 'root' })
export class EventosService {
  private baseUrl = buildApiUrl(API_PATHS.eventos);
  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
    return this.http.get(`${this.baseUrl}/listar`);
  }

  registrar(evento: Evento): Observable<any> {
    return this.http.post(`${this.baseUrl}/registrar`, evento);
  }

  editar(form: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/editar`, form);
  }

  obtenerDetalles(codigo: number): Observable<{ organizaciones: { nit: string; representanteAlterno: string; certificadoUrl: string }[], responsables: { idUsuario: number; documentoAvalUrl: string }[], reservaciones: {codigoEspacio: string; horaInicio: string; horaFin:string}[]}> {
    return this.http.get<{ organizaciones:{ nit: string; representanteAlterno: string; certificadoUrl: string }[], responsables: { idUsuario: number; documentoAvalUrl: string }[], reservaciones: {codigoEspacio: string; horaInicio: string; horaFin:string}[] }>(`${this.baseUrl}/detalles/${codigo}`);
  }
}