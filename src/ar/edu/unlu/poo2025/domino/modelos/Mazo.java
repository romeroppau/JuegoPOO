package ar.edu.unlu.poo2025.domino.modelos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

public class Mazo {
    private ArrayList<FichaDomino> fichasARepartir;//funcionara como pozo despues

    public void inicializarFichas() {
        fichasARepartir = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                fichasARepartir.add(new FichaDomino(i, j));
            }
        }
        Collections.shuffle(fichasARepartir); // Mezcla aleatoria
    }
    //para hacerle cambios a las fichas de los jugadores
    public void repartirFichas(ArrayList<Jugador> jugadores) {
        int cantidad = 7;

        for (Jugador j : jugadores) {
            ArrayList<FichaDomino> mano = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                mano.add(fichasARepartir.removeFirst());
            }
            j.setFichas(mano); // usa el atributo interno del jugador
        }
    }
    //solamente devuelve una ficha, no es su responsabilidad asignarla al jugador
    //tampoco verifica si esta vacia
    public FichaDomino robarFicha() {
        ArrayList<FichaDomino> fichasDisponibles= getFichasARepartir();
        return fichasDisponibles.removeFirst();
    }

    public boolean estaVacio() {
        return fichasARepartir.isEmpty();
    }

    public ArrayList<FichaDomino> getFichasARepartir() {
        return fichasARepartir;
    }
}
