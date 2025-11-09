import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CollectionService } from '../collection/services/collection.service';
import { CollectionResponseDto } from '../../types/aliases';
import { CommonModule } from '@angular/common';
import { CollectionCardComponent } from '../collection-card/collection-card.component';
import { ConfirmationModalComponent } from '../../shared/components/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-collection-list',
  standalone: true,
  imports: [CommonModule, CollectionCardComponent, ConfirmationModalComponent],
  templateUrl: './collection-list.component.html',
  styleUrl: './collection-list.component.scss'
})
export class CollectionListComponent implements OnInit {
  collections: CollectionResponseDto[] = [];
  isModalVisible = false;
  collectionIdToDelete: string | null = null;

  constructor(
    private collectionService: CollectionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getCollections();
  }

  getCollections(): void {
    this.collectionService.getCollections().subscribe(collections => {
      this.collections = collections;
    });
  }

  deleteCollection(id: string): void {
    this.collectionIdToDelete = id;
    this.isModalVisible = true;
  }

  confirmDelete(): void {
    if (this.collectionIdToDelete) {
      this.collectionService.deleteCollection(this.collectionIdToDelete).subscribe(() => {
        this.collections = this.collections.filter(collection => collection.id !== this.collectionIdToDelete);
        this.collectionIdToDelete = null;
      });
    }
    this.isModalVisible = false;
  }

  cancelDelete(): void {
    this.isModalVisible = false;
    this.collectionIdToDelete = null;
  }

  navigateToCreateCollection(): void {
    this.router.navigate(['/app/collections/new']);
  }
}
