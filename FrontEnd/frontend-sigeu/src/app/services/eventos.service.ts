import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

export interface Evento {
  // Completar según modelo del backend si se requiere
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
}