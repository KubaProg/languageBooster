import { TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GenerationFormComponent } from './generation-form.component';
import { CollectionService } from '../collection/services/collection.service';
import { NotificationService } from '../../shared/services/notification.service';

// --- Mock Services ---
const mockCollectionService = {
  generateFromText: jest.fn(),
  crOeate: jest.fn(),
};

const mockNotificationService = {
  show: jest.fn(),
};

const mockRouter = {
  navigate: jest.fn(),
};

describe('GenerationFormComponent (Simplified Logic Tests)', () => {
  let component: GenerationFormComponent;

  // Use TestBed to setup the component class instance with its dependencies
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GenerationFormComponent, ReactiveFormsModule],
      providers: [
        { provide: CollectionService, useValue: mockCollectionService },
        { provide: NotificationService, useValue: mockNotificationService },
        { provide: Router, useValue: mockRouter },
      ],
    });

    // Create an instance of the component class without rendering its HTML template
    component = TestBed.createComponent(GenerationFormComponent).componentInstance;

    jest.clearAllMocks();
    // Mock the service to return a simple observable for logic tests
    mockCollectionService.generateFromText.mockReturnValue(of({ id: '123' }));
    mockCollectionService.crOeate.mockReturnValue(of({ id: '456' }));
  });

  // Test 1: Basic creation
  test('should create the component instance', () => {
    expect(component).toBeTruthy();
  });

  // Test 2: Form initialization
  test('should initialize the form with default values', () => {
    expect(component.generationForm).toBeDefined();
    expect(component.generationForm.get('name')?.value).toBe('');
    expect(component.generationForm.get('cardsToGenerate')?.value).toBe(10);
  });

  // Test 3: Invalid submission
  test('should not call any service when onSubmit is called with an invalid form', () => {
    // The form is invalid by default, so this tests the guard clause
    component.onSubmit();
    expect(mockCollectionService.generateFromText).not.toHaveBeenCalled();
    expect(mockCollectionService.crOeate).not.toHaveBeenCalled();
  });

});
