import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CollectionResponseDto } from '../../types/aliases';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-collection-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './collection-card.component.html',
  styleUrl: './collection-card.component.scss'
})
export class CollectionCardComponent {
  @Input() collection!: CollectionResponseDto;
  @Output() onDelete = new EventEmitter<string>();

  delete(event: MouseEvent): void {
    event.stopPropagation();
    event.preventDefault();
    this.onDelete.emit(this.collection.id);
  }
}
