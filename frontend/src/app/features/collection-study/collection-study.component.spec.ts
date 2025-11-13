import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollectionStudyComponent } from './collection-study.component';

describe('CollectionStudyComponent', () => {
  let component: CollectionStudyComponent;
  let fixture: ComponentFixture<CollectionStudyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CollectionStudyComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CollectionStudyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
