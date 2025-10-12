import { Injectable } from '@angular/core';

export type NotificationType = 'success' | 'error' | 'info';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  public show(type: NotificationType, message: string): void {
    // In a real application, this would trigger a toast notification component.
    // console.log(`[Notification - ${type.toUpperCase()}]: ${message}`);
  console.log("NOTHIGN FOR NOW")
  }
}
