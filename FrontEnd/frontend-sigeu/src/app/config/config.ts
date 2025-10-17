export const API_BASE_URL = 'http://localhost:8080';

export const API_PATHS = {
  eventos: '/eventos',
  organizacionesExternas: '/organizacionesExternas',
  usuarios: '/usuarios',
} as const;

export function buildApiUrl(path: string): string {
  return `${API_BASE_URL}${path}`;
}


