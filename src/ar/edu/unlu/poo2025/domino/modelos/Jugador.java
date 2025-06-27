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

    //interactivo
    public boolean tieneJugadaValida(Tablero tablero) {
        for (FichaDomino f : this.fichas) {
            if (tablero.fichaValida(f)) return true;//tiene al menos una combinacion
        }
        return false;
    }

    // Verifica si puede jugar sin modificar el tablero
    public ArrayList<FichaDomino> obtenerFichasJugables(Tablero tablero) {
        ArrayList<FichaDomino> jugables = new ArrayList<>();
        for (FichaDomino f : this.fichas) {
            if (tablero.fichaValida(f)) {
                jugables.add(f);
            }
        }
        return jugables;
    }

    public boolean jugarFichaElegida(int indice, Tablero tablero){
        if (indice >= 0 && indice < fichas.size()) {
            FichaDomino ficha = fichas.get(indice);
            boolean pudo = tablero.agregaFichaTablero(ficha);

            if (pudo) {
                fichas.remove(indice); // remueve la ficha jugada
                return true;
            }
        }
        return false;
    }
    public void agregarFicha(FichaDomino f){// agrega a su lista de fichas
        fichas.add(f);
    }
    public int getIndiceFicha(FichaDomino ficha) {
        for (int i = 0; i < fichas.size(); i++) {
            if (fichas.get(i).equals(ficha)) {
                return i;
            }
        }
        return -1;
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
