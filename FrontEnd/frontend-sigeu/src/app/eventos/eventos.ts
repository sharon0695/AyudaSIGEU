import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventosService, Evento } from '../services/eventos.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-eventos',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './eventos.html',
  styleUrl: './eventos.css'
})
export class Eventos {
  eventos: any[] = [];
  mensaje = '';
  nuevoEvento: any = {
    nombre: '',
    descripcion: '',
    tipo: '',
    fecha: '',
    hora_inicio: '',
    hora_fin: '',
    codigo_lugar: '',
    nit_organizacion: ''
  };

  constructor(private events: EventosService) {}

  ngOnInit() { this.listar(); }
  listar() { this.events.listar().subscribe({
      next: (data) => (this.eventos = data || []),
      error: () => (this.mensaje = 'No fue posible cargar organizaciones'),
    }); }

  crear(evento: Evento) {
    this.events.registrar(evento).subscribe({ next: () => this.listar(), error: () => (this.mensaje = 'No fue posible registrar el evento') });
  }

  onSubmitCrearEvento(event: Event) {
    event.preventDefault();
    this.crear(this.nuevoEvento);
  }
}