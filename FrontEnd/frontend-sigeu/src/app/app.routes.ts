import { Routes } from '@angular/router';
import { CrearCuenta } from './crear-cuenta/crear-cuenta';
//import { LoginComponent } from './login/login.component';
// importa más componentes según necesites

export const routes: Routes = [
  //{ path: 'login', component: LoginComponent },
  { path: '', component: CrearCuenta},
  { path: 'crearCuenta', component: CrearCuenta },
  { path: '**', redirectTo: ''}
];