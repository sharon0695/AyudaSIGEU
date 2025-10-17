import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

export interface LoginRequest {
  correoInstitucional: string;
  contrasena: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresInMillis: number;
  identificacion: number;
  nombre: string;
  apellido: string;
  correoInstitucional: string;
  rol: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = buildApiUrl(API_PATHS.usuarios);
  private storageKey = 'auth_token';

  constructor(private http: HttpClient) {}

  login(body: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, body).pipe(
      tap((res) => {
        const token = `${res.tokenType} ${res.accessToken}`;
        localStorage.setItem(this.storageKey, token);
        localStorage.setItem('auth_user', JSON.stringify({
          identificacion: res.identificacion,
          nombre: res.nombre,
          apellido: res.apellido,
          correoInstitucional: res.correoInstitucional,
          rol: res.rol
        }));
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.storageKey);
  }

  getUserId(): number | null {
    try {
      const raw = localStorage.getItem('auth_user');
      if (!raw) return null;
      const obj = JSON.parse(raw);
      return obj?.identificacion ?? null;
    } catch {
      return null;
    }
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}