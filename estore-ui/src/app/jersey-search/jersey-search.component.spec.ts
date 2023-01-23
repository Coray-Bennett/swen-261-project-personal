import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JerseySearchComponent } from './jersey-search.component';

describe('JerseySearchComponent', () => {
  let component: JerseySearchComponent;
  let fixture: ComponentFixture<JerseySearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JerseySearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JerseySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
