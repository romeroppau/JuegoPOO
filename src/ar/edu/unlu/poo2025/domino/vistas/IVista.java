package ar.edu.unlu.poo2025.domino.vistas;

import ar.edu.unlu.poo2025.domino.modelos.FichaDomino;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;

public interface IVista {
    public void mostrarListaJugadores(Jugador[] jugadores);
    void mostrarEstadoTablero(FichaDomino[] fichas);
    void notificarTurno(String nombreJugador);
    void mostrarGanador(Jugador ganador);
}
