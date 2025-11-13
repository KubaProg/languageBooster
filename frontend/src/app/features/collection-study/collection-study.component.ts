import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CollectionApiService } from '../../services/collection-api.service';
import { CardDto, CollectionDetailsDto } from '../../types/aliases';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-collection-study',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './collection-study.component.html',
  styleUrl: './collection-study.component.scss'
})
export class CollectionStudyComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private collectionApiService = inject(CollectionApiService);

  collectionDetails: CollectionDetailsDto | null = null;
  cardsToReview: CardDto[] = [];
  currentCardIndex: number = 0;
  isCardFlipped: boolean = false;
  isEditMode: boolean = false;

  // Properties for editing
  editedFront: string = '';
  editedBack: string = '';

  ngOnInit(): void {
    const collectionId = this.route.snapshot.paramMap.get('id');
    if (collectionId) {
      this.collectionApiService.getCollectionDetails(collectionId).pipe(
        tap(collection => {
          this.collectionDetails = collection;
          this.cardsToReview = [...collection.cards]; // Initialize with all cards
        })
      ).subscribe();
    }
  }

  get currentCard(): CardDto | null {
    return this.cardsToReview.length > 0 ? this.cardsToReview[this.currentCardIndex] : null;
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
    if (this.isEditMode && this.currentCard) {
      this.editedFront = this.currentCard.front;
      this.editedBack = this.currentCard.back;
    }
  }

  saveChanges(): void {
    if (this.currentCard && this.isEditMode) {
      this.collectionApiService.updateCardContent(this.currentCard.id, this.editedFront, this.editedBack).subscribe({
        next: () => {
          // Update local data
          if (this.currentCard) {
            this.currentCard.front = this.editedFront;
            this.currentCard.back = this.editedBack;
          }
          this.toggleEditMode();
        },
        error: (err) => {
          console.error('Failed to update card content:', err);
          // Optionally, handle error in UI
        }
      });
    }
  }

  deleteCard(): void {
    if (this.currentCard && this.isEditMode) {
      if (confirm('Are you sure you want to delete this card?')) {
        this.collectionApiService.deleteCard(this.currentCard.id).subscribe({
          next: () => {
            this.cardsToReview.splice(this.currentCardIndex, 1);
            this.isEditMode = false;
            this.nextCard();
          },
          error: (err) => {
            console.error('Failed to delete card:', err);
            // Optionally, handle error in UI
          }
        });
      }
    }
  }

  markAsKnown(): void {
    if (this.currentCard) {
      this.collectionApiService.updateCardKnownStatus(this.currentCard.id, true).subscribe({
        next: () => {
          this.cardsToReview.splice(this.currentCardIndex, 1);
          this.nextCard();
        },
        error: (err) => {
          console.error('Failed to update card status:', err);
          // Optionally, handle error in UI
        }
      });
    }
  }

  markAsUnknown(): void {
    if (this.currentCard) {
      this.nextCard();
    }
  }

  private nextCard(): void {
    this.isCardFlipped = false;
    if (this.cardsToReview.length === 0) {
      this.currentCardIndex = 0; // No cards left, reset or show completion message
      return;
    }
    this.currentCardIndex = (this.currentCardIndex + 1) % this.cardsToReview.length;
  }
}
