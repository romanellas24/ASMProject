import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocalsPageComponent } from './locals-page.component';

describe('LocalsPageComponent', () => {
  let component: LocalsPageComponent;
  let fixture: ComponentFixture<LocalsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LocalsPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LocalsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
