import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

@Injectable({ providedIn: 'root' })
export class NotificacionService {
  private baseUrl = buildApiUrl(API_PATHS.notificacion);
  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
    return this.http.get(`${this.baseUrl}/ruta2`);
  }

  crear(body: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/ruta1`, body);
  }
}