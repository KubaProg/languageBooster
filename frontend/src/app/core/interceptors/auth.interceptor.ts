import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const session = authService.currentSession;

  if (session) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${session.access_token}`
      }
    });
    return next(clonedReq);
  }

  return next(req);
};
