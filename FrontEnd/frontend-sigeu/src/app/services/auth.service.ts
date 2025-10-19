import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';
import { Router } from '@angular/router';

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
  private expiryKey = 'auth_expires_at';
  private expiryTimer: any;

  constructor(private http: HttpClient, private router: Router) {
    const exp = this.getExpiry();
    if (exp) this.scheduleExpiryLogout(exp);
  }
  private getExpiry(): number | null {
    const raw = localStorage.getItem(this.expiryKey);
    if (!raw) return null;
    const n = Number(raw);
    return Number.isFinite(n) ? n : null;
  }
  login(body: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, body).pipe(
      tap((res) => {
        const token = `${res.tokenType} ${res.accessToken}`;
        localStorage.setItem(this.storageKey, token);
        const expiresAt = Date.now() + (res.expiresInMillis || 0);
        localStorage.setItem(this.expiryKey, String(expiresAt));
        this.scheduleExpiryLogout(expiresAt);
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
    localStorage.removeItem(this.expiryKey);
    if (this.expiryTimer) { clearTimeout(this.expiryTimer); this.expiryTimer = null; }
  }
  logoutRemote() {
    return this.http.post<void>(`${this.baseUrl}/logout`, {});
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
    const token = this.getToken();
    if (!token) return false;
    const exp = this.getExpiry();
    if (exp && exp <= Date.now()) { this.logout(); return false; }
    return true;
  }

  private scheduleExpiryLogout(expiresAt: number) {
    if (this.expiryTimer) clearTimeout(this.expiryTimer);
    const delay = Math.max(0, expiresAt - Date.now());
    if (delay === 0) { this.logout(); this.router.navigateByUrl('/login'); return; }
    this.expiryTimer = setTimeout(() => { this.logout(); this.router.navigateByUrl('/login'); }, delay);
  }
}