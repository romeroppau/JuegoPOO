package ar.edu.unlu.poo2025.domino.modelos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Jugador implements Serializable {
    private String nombre;
    private int puntaje;
    private ArrayList<FichaDomino> fichas;
    private static final long serialVersionUID = 1L;

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
        return mejor;//devuelve la ficha mas alta
    }

    // Verifica si puede jugar sin modificar el tablero
    public boolean tieneJugadaValida(Tablero tablero) {
        for (FichaDomino ficha : fichas) {
            if (tablero.fichaValida(ficha)) {
                return true;
            }
        }
        return false;
    }

    // Intenta jugar si puede ( sin robar)
    public boolean jugarFicha(Tablero tablero) {
        Iterator<FichaDomino> it = fichas.iterator();
        while (it.hasNext()) {
            FichaDomino ficha = it.next();
            if (tablero.agregaFichaTablero(ficha)) {
                it.remove();
                return true;
            }
        }
        return false; // no pudo jugar
    }

    // Si no puede jugar, roba hasta que pueda (o se quede sin fichas)
    public boolean intentarRobarYJugar(Tablero tablero, Mazo mazo) {
        while (!mazo.estaVacio()) {
            FichaDomino robada = mazo.robarFicha();//saca la primera de la lista
            if (tablero.agregaFichaTablero(robada)) {
                // No se agrega a la mano porque se jugó directamente
                return true;
            } else {
                fichas.add(robada); // No se pudo jugar, se guarda
            }
        }
        return false;
    }

    // Este metodo es el que se llama desde el controlador
    public boolean realizarTurno(Tablero tablero, Mazo mazo) {
        // Intenta jugar con sus fichas primero
        if (jugarFicha(tablero)) {
            return true;
        }

        // Si no pudo, intenta robar y jugar
        return intentarRobarYJugar(tablero, mazo);
    }

    // Verifica si el jugador tiene fichas restantes
    public boolean tieneFichas() {
        return !fichas.isEmpty(); // Verifica si la lista de fichas no está vacía
    }

    public FichaDomino nuevaMano() {
        if (fichas.isEmpty()) return null;
        return fichas.removeFirst();
    }

    public int recuentoPuntosJugador() {//metodo cuando se necesitan sumar los ptos cuando se termina una ronda
        int suma = 0;
        for (FichaDomino f : fichas) {
            suma += (f.getExtremoIZQ() + f.getExtremoDER());
        }
        return suma;
    }

    public void sumarPuntos(int puntos) {
        this.puntaje += puntos;
    }

    // Getters y setters
    public ArrayList<FichaDomino> getFichas() {
        return this.fichas;
    }

    public String getNombre() {
        return this.nombre;
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
