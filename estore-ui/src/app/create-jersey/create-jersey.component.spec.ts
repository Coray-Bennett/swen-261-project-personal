import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateJerseyComponent } from './create-jersey.component';

describe('CreateJerseyComponent', () => {
  let component: CreateJerseyComponent;
  let fixture: ComponentFixture<CreateJerseyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateJerseyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateJerseyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
