package ar.edu.unlu.poo2025.domino.modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class ManejadorTurnos implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Jugador> jugadores;
    private int turnoActual;//guarda el indice del jugador que tiene el turno

    public ManejadorTurnos(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
        this.turnoActual = -1; // Aún no comenzó
    }

    public void siguienteTurno() {
        if (jugadores != null && !jugadores.isEmpty()) {
            turnoActual = (turnoActual + 1) % jugadores.size();
        } // Cola circular
    }

    public int getTurnoActual() {
        return turnoActual;//devuelve el indice
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    public Jugador getJugadorActual() {
        if (turnoActual >= 0 && turnoActual < jugadores.size()) {
            return jugadores.get(turnoActual);
        }
        return null;
    }
}
