import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Organizaciones } from './organizaciones';

describe('Organizaciones', () => {
  let component: Organizaciones;
  let fixture: ComponentFixture<Organizaciones>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Organizaciones]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Organizaciones);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
