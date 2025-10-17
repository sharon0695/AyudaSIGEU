import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

@Injectable({ providedIn: 'root' })
export class FacultadService {
  private baseUrl = buildApiUrl(API_PATHS.facultad);
  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
    return this.http.get(`${this.baseUrl}/listar`);
  }

  registrar(body: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/registrar`, body);
  }
}