import { HttpErrorResponse, HttpEvent, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: token } });
  }
  return next(req).pipe(
    tap((event: HttpEvent<any>) => {
      // Aquí puedes hacer algo con la respuesta si quieres
    }),
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401 || err.status === 403) {
        auth.logout();
      }
      return throwError(() => err);
    })
  );
};