import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecuperarContrasena } from './recuperar-contrasena';

describe('RecuperarContrasena', () => {
  let component: RecuperarContrasena;
  let fixture: ComponentFixture<RecuperarContrasena>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecuperarContrasena]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecuperarContrasena);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
