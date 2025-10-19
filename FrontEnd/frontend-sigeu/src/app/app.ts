import { Component } from '@angular/core';
import { RouterOutlet, provideRouter } from '@angular/router';
import { routes } from './app.routes';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <router-outlet></router-outlet>`
})
export class App {}

export const appConfig = {
  providers: [provideRouter(routes)]
};