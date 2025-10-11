import { Component, inject } from '@angular/core';
import { Router, RouterModule } from "@angular/router";

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
  private router = inject(Router);

  // TODO: Inject a real AuthService
  public logout(): void {
    console.log('Logging out...');
    // authService.logout();
    this.router.navigate(['/login']);
  }
}
