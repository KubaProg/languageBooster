import { Component, inject } from '@angular/core';
import { RouterModule } from "@angular/router";
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    RouterModule
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent {
  private authService = inject(AuthService);

  public logout(): void {
    this.authService.signOut();
  }
}
