import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewJerseyComponent } from './view-jersey.component';

describe('ViewJerseyComponent', () => {
  let component: ViewJerseyComponent;
  let fixture: ComponentFixture<ViewJerseyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewJerseyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewJerseyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
