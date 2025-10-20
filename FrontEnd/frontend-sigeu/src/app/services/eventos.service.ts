import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_PATHS, buildApiUrl } from '../config/config';

export interface EventoRegistroCompleto {
  nombre: string;
  descripcion?: string;
  tipo: string;
  fecha: string;
  hora_inicio: string;
  hora_fin: string;
  id_usuario_registra: number;
  organizaciones: OrganizacionDTO[];
  responsables: ResponsableDTO[];
  reservaciones: ReservacionDTO[];
}

export interface OrganizacionDTO {
  nit: string;
  nombre?: string;
  representante_legal?: string;
  ubicacion?: string;
  telefono?: string;
  sector_economico?: string;
  actividad_principal?: string;
  certificado_participacion?: string;
  representante_alterno?: string;
}

export interface ResponsableDTO {
  id_usuario: number;
  documentoAval?: string;
  tipoAval?: string;
}

export interface ReservacionDTO {
  codigo_espacio: string;
  hora_inicio: string;
  hora_fin: string;
}

@Injectable({ providedIn: 'root' })
export class EventosService {
  private baseUrl = buildApiUrl(API_PATHS.eventos);
  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
    return this.http.get(`${this.baseUrl}/listar`);
  }

  registrar(evento: EventoRegistroCompleto): Observable<any> {
    return this.http.post(`${this.baseUrl}/registrar`, evento);
  }

  editar(form: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/editar`, form);
  }

  obtenerDetalles(codigo: number): Observable<{ organizaciones: { nit: string; representanteAlterno: string; certificadoUrl: string }[], responsables: { idUsuario: number; documentoAvalUrl: string }[], reservaciones: {codigoEspacio: string; horaInicio: string; horaFin:string}[]}> {
    return this.http.get<{ organizaciones:{ nit: string; representanteAlterno: string; certificadoUrl: string }[], responsables: { idUsuario: number; documentoAvalUrl: string }[], reservaciones: {codigoEspacio: string; horaInicio: string; horaFin:string}[] }>(`${this.baseUrl}/detalles/${codigo}`);
  }
}