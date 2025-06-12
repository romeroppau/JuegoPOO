package ar.edu.unlu.poo2025.domino.modelos;
import java.util.ArrayList;
import java.util.Iterator;

public class Jugador {
    private String nombre;
    private int puntaje;
    private ArrayList<FichaDomino> fichas;

    // Constructor
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.fichas = new ArrayList<>();
    }

    public boolean tieneFichaDoble() {
        for (FichaDomino f : fichas) {
            if (f.esfichaDoble()) {
                return true;
            }
        }
        return false;
    }
    /*
    // devuelve las fichas dobles
    public ArrayList<FichaDomino> verificarFichaDoble() {
        ArrayList<FichaDomino> fichasDobles = new ArrayList<>();
        for (FichaDomino f : fichas) {
            if (f.esfichaDoble()) {
                fichasDobles.add(f);
            }
        }
        return fichasDobles;
    }*/

    // Devuelve la ficha doble más alta que tenga el jugador
    public FichaDomino dobleMasAlto() {
        FichaDomino mejor = null;
        int max = -1;

        for (FichaDomino f : fichas) {
            if (f.esfichaDoble()) {
                int valor = f.getExtremoIZQ(); // o suma
                if (valor > max) {
                    max = valor;
                    mejor = f;
                }
            }
        }

        return mejor; // puede ser null si no tiene ninguna ficha doble
    }

    public FichaDomino getFichaConMayorSuma() {
        FichaDomino mejor = null;
        int maxSuma = -1;
        for (FichaDomino f : fichas) {
            int suma = f.getExtremoDER() + f.getExtremoIZQ();
            if (suma > maxSuma) {
                maxSuma = suma;
                mejor = f;
            }
        }
        return mejor;
    }

    // Verifica el jugador si puede jugar según los extremos del tablero
    public boolean puedeJugar(Tablero tablero,Mazo mazo) {
        boolean jugo = false;

        // Primero intento jugar con las fichas que ya tengo
        Iterator<FichaDomino> it = fichas.iterator();
        while (it.hasNext()) {
            FichaDomino ficha = it.next();
            if (tablero.agregaFichaTablero(ficha)) {
                it.remove();
                jugo = true;
                return true;
            }
        }

        // Si no pude jugar, intento robar hasta que pueda o se acabe el mazo
        while (!mazo.estaVacio()) {
            FichaDomino robada = tieneQueRobar(mazo);
            fichas.add(robada);

            if (tablero.agregaFichaTablero(robada)) {
                fichas.remove(robada);
                jugo = true;
                return true;
            }
        }

        // Si llegué hasta acá, no pude jugar y el mazo está vacío
        return false;
        //si puedeJugar = false, el juego se bloqueo
    }

    public FichaDomino tieneQueRobar(Mazo mazo){
        FichaDomino fichaRobada=null;
        fichaRobada= mazo.robarFicha();
        return fichaRobada;
    }

    // Verifica si el jugador tiene fichas restantes
    public boolean tieneFichas() {
        return !fichas.isEmpty(); // Verifica si la lista de fichas no está vacía
    }

    // Getters y setters
    public ArrayList<FichaDomino> getFichas() {
        return this.fichas;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void setFichas(ArrayList<FichaDomino> fichas) {
        this.fichas = fichas;
    }
}
