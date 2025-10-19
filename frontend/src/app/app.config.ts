import { ApplicationConfig } from '@angular/core';
import { provideRouter, withHashLocation } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from "@angular/common/http";
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withHashLocation()),
    provideClientHydration(),
    provideHttpClient(withInterceptors([authInterceptor])),
    { provide: LocationStrategy, useClass: HashLocationStrategy }
  ]
};
