import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearCuenta } from './crear-cuenta';

describe('CrearCuenta', () => {
  let component: CrearCuenta;
  let fixture: ComponentFixture<CrearCuenta>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearCuenta]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearCuenta);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
