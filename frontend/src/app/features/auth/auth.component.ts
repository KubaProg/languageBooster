import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  isLoginMode = true;
  errorMessage: string | null = null;

  authForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = null;
    this.authForm.reset();
  }

  async onSubmit() {
    if (this.authForm.invalid) {
      return;
    }

    const { email, password } = this.authForm.value;

    try {
      let response;
      if (this.isLoginMode) {
        response = await this.authService.signIn({ email: email!, password: password! });
      } else {
        response = await this.authService.signUp({ email: email!, password: password! });
      }

      if (response.error) {
        this.errorMessage = response.error.message;
      } else {
        this.router.navigate(['/app']);
      }
    } catch (error: any) {
      this.errorMessage = error.message;
    }
  }
}
