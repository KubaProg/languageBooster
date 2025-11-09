import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CollectionResponseDto } from '../../types/aliases';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-collection-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './collection-card.component.html',
  styleUrl: './collection-card.component.scss'
})
export class CollectionCardComponent {
  @Input() collection!: CollectionResponseDto;
  @Output() onDelete = new EventEmitter<string>();

  delete(): void {
    this.onDelete.emit(this.collection.id);
  }
}
