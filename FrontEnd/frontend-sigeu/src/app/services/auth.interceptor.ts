import { HttpErrorResponse, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: token } });
  }
  return next(req).pipe({
    next: (event: HttpEvent<any>) => event,
    error: (err: HttpErrorResponse) => {
      if (err.status === 401 || err.status === 403) {
        auth.logout();
      }
      throw err;
    },
  } as any);
};