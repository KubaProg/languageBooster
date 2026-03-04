import { ApplicationConfig } from '@angular/core';
import { provideRouter, withHashLocation } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient } from "@angular/common/http";
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { MessageService, ConfirmationService } from 'primeng/api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withHashLocation()),
    provideClientHydration(),
    provideHttpClient(),
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    MessageService,
    ConfirmationService
  ]
};
