package ar.edu.unlu.poo2025.domino.modelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList<FichaDomino> fichasJugadas;
    private int extremoActualIzq;
    private int extremoActualDer;

    public Tablero(){
        this.extremoActualIzq=-1;//significa que no tiene ficha
        this.extremoActualDer=-1;
        this.fichasJugadas = new LinkedList<>();
    }

    public boolean agregaFichaTablero(FichaDomino ficha){
        boolean esvalida=fichaValida(ficha);
        // Si el tablero está vacío, simplemente agrego la ficha y seteo ambos extremos
        if (extremoActualIzq == -1 && extremoActualDer == -1) {
            fichasJugadas.add(ficha);
            extremoActualIzq = ficha.getExtremoIZQ();
            extremoActualDer = ficha.getExtremoDER();
            return true;
        }

        if(!esvalida){
            return false;
        }
        //si ficha va a la izquierda-> se agrega al principio de la lista
        //si ficha va a la derecha-> se agrega al final de la lista
        //verifico en que extremo agregarla
        if(ficha.getExtremoDER()==extremoActualIzq){
            //no necesita rotar
            fichasJugadas.addFirst(ficha);
            extremoActualIzq= ficha.getExtremoIZQ();
            return true;
        } else if (ficha.getExtremoDER()==extremoActualDer) {
            //necesita rotar
            ficha.rotarFicha();
            fichasJugadas.addLast(ficha);
            extremoActualDer=ficha.getExtremoDER();
            return true;
        }else if (ficha.getExtremoIZQ() == extremoActualIzq) {
            ficha.rotarFicha(); // ahora el nuevo extremoIzq es el correcto
            fichasJugadas.addFirst(ficha);
            extremoActualIzq = ficha.getExtremoIZQ();
            return true;
        } else if (ficha.getExtremoIZQ() == extremoActualDer) {
            // No necesita rotar, va al final
            fichasJugadas.addLast(ficha);
            extremoActualDer = ficha.getExtremoDER();
            return true;
        }
        return false;
    }

    //valida que coincida con alguno de los extremos actuales
    public boolean fichaValida(FichaDomino ficha){
        // Si el tablero está vacío, cualquier ficha es válida
        if (extremoActualIzq == -1 && extremoActualDer == -1) {
            return true;
        }
        return ficha.getExtremoIZQ() == extremoActualIzq ||
                ficha.getExtremoIZQ() == extremoActualDer ||
                ficha.getExtremoDER() == extremoActualIzq ||
                ficha.getExtremoDER() == extremoActualDer;
    }

    public void actualizarExtremos(int nuevoExtIzq, int nuevoExtDer){
        setExtremoActualIzq(nuevoExtIzq);
        setExtremoActualDer(nuevoExtDer);
    }
    public void limpiezaTablero() {
        fichasJugadas.clear();
        extremoActualIzq = -1;
        extremoActualDer = -1;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fichas en el tablero:\n");

        for (FichaDomino ficha : fichasJugadas) {
            sb.append(ficha.toString()).append(" ");
        }

        return sb.toString();
    }

    //Getters
    public LinkedList<FichaDomino> getFichasJugadas() {
        return fichasJugadas;
    }
    public int getExtremoActualIzq() {
        return extremoActualIzq;
    }
    public int getextremoActualDer(){
        return extremoActualDer;
    }
    //setters
    public void setExtremoActualIzq(int extremoActualIzq) {
        this.extremoActualIzq = extremoActualIzq;
    }
    public void setExtremoActualDer(int extremoActualDer) {
        this.extremoActualDer = extremoActualDer;
    }

    public void setFichasJugadas(LinkedList<FichaDomino> fichasJugadas) {
        this.fichasJugadas = fichasJugadas;
    }
}
