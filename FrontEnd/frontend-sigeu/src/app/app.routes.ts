import { Routes } from '@angular/router';
import { CrearCuenta } from './crear-cuenta/crear-cuenta';
import { Login } from './login/login';
import { Organizaciones } from './organizaciones/organizaciones';
import { Perfil } from './perfil/perfil';
import { RecuperarContrasena } from './recuperar-contrasena/recuperar-contrasena';
import { Home } from './home/home';
import { Eventos } from './eventos/eventos';
//import { LoginComponent } from './login/login.component';
// importa más componentes según necesites

export const routes: Routes = [
  //{ path: 'login', component: LoginComponent },
  { path: '', component: Login},
  { path: 'crearCuenta', component: CrearCuenta },
  { path: 'login', component: Login},
  { path: 'home', component: Home},
  { path: 'eventos', component: Eventos},
  { path: 'organizacionExt', component: Organizaciones},
  { path: 'perfil', component: Perfil},
  { path: 'recuperar-contrasena', component: RecuperarContrasena},
  { path: '**', redirectTo: ''}
];