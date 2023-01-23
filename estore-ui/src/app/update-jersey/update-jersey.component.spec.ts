import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateJerseyComponent } from './update-jersey.component';

describe('UpdateJerseyComponent', () => {
  let component: UpdateJerseyComponent;
  let fixture: ComponentFixture<UpdateJerseyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateJerseyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateJerseyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
