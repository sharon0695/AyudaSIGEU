import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { Router, RouterLink } from "@angular/router";

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './css/main.css'
})
export class Login {
  form = { correoInstitucional: '', contrasena: '' };
  mensaje = '';

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit(event: Event) {
    event.preventDefault();
    this.auth.login(this.form).subscribe({
      next: () => {this.mensaje = 'Inicio de sesión exitoso';
        this.router.navigateByUrl('/home');
      },
      error: () => (this.mensaje = 'Credenciales inválidas'),
    });
  }
}