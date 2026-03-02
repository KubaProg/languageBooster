import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private _isAuthenticated = new BehaviorSubject<boolean>(true);
  public isAuthenticated$ = this._isAuthenticated.asObservable();

  private _session = new BehaviorSubject<any>({ access_token: 'mock-token' });

  constructor() {
    // Mocked authentication for development
  }

  get currentSession(): any {
    return this._session.getValue();
  }

  async signIn(credentials: { email: string; password: string }) {
    return { data: { session: this.currentSession }, error: null };
  }

  async signUp(credentials: { email: string; password: string }) {
    return { data: { session: this.currentSession }, error: null };
  }

  async signOut() {
    console.log('Mock signOut called');
  }

  async getSession() {
    return this.currentSession;
  }
}
