# Supabase Angular Initialization

This document provides a reproducible guide to create the necessary file structure for integrating Supabase with your Angular project.

## Prerequisites

- An Angular project.
- The `@supabase/supabase-js` package is installed.
- A `database.types.ts` file with your database definitions exists (e.g., at `src/app/types/database.types.ts`).

## File Structure and Setup

### 1. Environment Configuration

Create the files `src/environments/environment.ts` and `src/environments/environment.prod.ts`.

**`src/environments/environment.ts`**
```ts
export const environment = {
  production: false,
  supabaseUrl: 'http://127.0.0.1:54321',
  supabaseKey: 'sb_publishable_ACJWlzQHlZjBrEguHvfOxg_3BJgxAaH'
};
```

**`src/environments/environment.prod.ts`**
```ts
export const environment = {
  production: true,
  supabaseUrl: 'YOUR_PROD_SUPABASE_URL',
  supabaseKey: 'YOUR_PROD_SUPABASE_KEY'
};
```

Then, configure `angular.json` to replace the environment file for production builds by adding the `fileReplacements` array to the `production` configuration.

### 2. Supabase Service

Create a service at `src/app/services/supabase.service.ts` to initialize and provide the Supabase client as a singleton throughout your app.

```ts
import { Injectable } from '@angular/core';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { environment } from '../../environments/environment';
import { Database } from '../types/database.types';

@Injectable({
  providedIn: 'root'
})
export class SupabaseService {
  public supabase: SupabaseClient<Database>;

  constructor() {
    this.supabase = createClient<Database>(
      environment.supabaseUrl,
      environment.supabaseKey
    );
  }
}
```

### 3. Usage

You can now inject the `SupabaseService` in your components and access the client like this:

```ts
import { Component } from '@angular/core';
import { SupabaseService } from './services/supabase.service';

@Component({
  selector: 'app-some-component',
  template: `<button (click)="handleLogin()">Login with GitHub</button>`
})
export class SomeComponent {
  constructor(private readonly supabaseService: SupabaseService) {}

  async handleLogin() {
    await this.supabaseService.supabase.auth.signInWithOAuth({
      provider: 'github'
    });
  }
}
```
