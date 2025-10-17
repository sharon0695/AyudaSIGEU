export const API_BASE_URL = 'http://localhost:8080';

export const API_PATHS = {
  eventos: '/eventos',
  organizacionesExternas: '/organizacionExterna',
  usuarios: '/usuarios',
  programas: '/programas',
  facultad: '/facultad',
  unidad: '/unidad',
  espacio: '/espacio',
  reservacion: '/reservacion',
  colaboracion: '/colaboracion',
  responsable: '/responsable',
  notificacion: '/rutaNot',
  evaluacion: '/rutaEva',
} as const;

export function buildApiUrl(path: string): string {
  return `${API_BASE_URL}${path}`;
}