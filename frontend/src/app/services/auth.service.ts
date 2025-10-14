import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { SupabaseClient, createClient, Session } from '@supabase/supabase-js';
import { BehaviorSubject, of } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private supabase: SupabaseClient;
  private router = inject(Router);
  private platformId = inject(PLATFORM_ID);

  private _isAuthenticated = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this._isAuthenticated.asObservable();

  private _session = new BehaviorSubject<Session | null>(null);

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      this.supabase = createClient(environment.supabaseUrl, environment.supabaseKey);
      this.supabase.auth.onAuthStateChange((event, session) => {
        this._session.next(session);
        this._isAuthenticated.next(!!session);
      });
    } else {
      // Create a dummy client for the server
      this.supabase = {
        auth: {
          onAuthStateChange: () => ({ data: { subscription: { unsubscribe: () => {} } } }),
          signInWithPassword: () => Promise.resolve({ data: { session: null }, error: null }),
          signUp: () => Promise.resolve({ data: { session: null }, error: null }),
          signOut: () => Promise.resolve({ error: null }),
          getSession: () => Promise.resolve({ data: { session: null }, error: null }),
        },
      } as any;
    }
  }

  get currentSession(): Session | null {
    return this._session.getValue();
  }

  async signIn(credentials: { email: string; password: string }) {
    if (!isPlatformBrowser(this.platformId)) return { data: { session: null }, error: null };
    return this.supabase.auth.signInWithPassword(credentials);
  }

  async signUp(credentials: { email: string; password: string }) {
    if (!isPlatformBrowser(this.platformId)) return { data: { session: null }, error: null };
    return this.supabase.auth.signUp(credentials);
  }

  async signOut() {
    if (!isPlatformBrowser(this.platformId)) return;
    await this.supabase.auth.signOut();
    this.router.navigate(['/auth']);
  }

  async getSession() {
    if (!isPlatformBrowser(this.platformId)) return null;
    const { data, error } = await this.supabase.auth.getSession();
    if (data.session) {
      this._isAuthenticated.next(true);
    }
    return data.session;
  }
}
