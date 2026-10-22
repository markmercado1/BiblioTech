import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Prestamo } from '../../services/prestamo';

@Component({
  selector: 'app-prestamos',
  imports: [FormsModule],
  templateUrl: './prestamos.html',
  styleUrl: './prestamos.css'
})
export class Prestamos {

  estudianteId: number = 1;
  libroId: number = 1;

  mensaje: string = '';
  disponible: boolean | null = null;

  constructor(private prestamoService: Prestamo) {}

  validarPrestamo(): void {
    this.prestamoService
      .validarPrestamo(this.estudianteId, this.libroId)
      .subscribe({
        next: (resultado) => {
          this.disponible = resultado;

          this.mensaje = resultado ? 'El préstamo está disponible.' : 'El préstamo no puede realizarse.';
        },
        error: () => {
          this.disponible = false;
          this.mensaje = 'Error al validar el préstamo.';
        }
      });
  }

  registrarPrestamo(): void {
    this.prestamoService
      .registrarPrestamo(this.estudianteId, this.libroId)
      .subscribe({
        next: () => {
          this.disponible = true;
          this.mensaje = 'Préstamo registrado correctamente.';
        },
        error: () => {
          this.disponible = false;
          this.mensaje = 'No se pudo registrar el préstamo.';
        }
      });
  }
}