package ar.edu.unlu.poo2025.domino.modelos;

import java.util.LinkedList;

public class Tablero {
    private LinkedList<FichaDomino> fichasJugadas;
    private int extremoActualIzq;
    private int extremoActualDer;

    public Tablero(){
        this.extremoActualIzq=-1;//significa que no tiene ficha
        this.extremoActualDer=-1;
    }

    public boolean agregaFichaTablero(FichaDomino ficha){
        boolean esvalida=fichaValida(ficha);
        if(!esvalida){
            return false;
        }
        //si ficha va a la izquierda-> se agrega al principio de la lista
        //si ficha va a la derecha-> se agrega al final de la lista
        //verifico en que extremo agregarla
        if(ficha.getExtremoDER()==extremoActualIzq){
            //no necesita rotar
            extremoActualIzq= ficha.getExtremoDER();
            fichasJugadas.addFirst(ficha);
        } else if (ficha.getExtremoDER()==extremoActualDer) {
            //necesita rotar
            ficha.rotarFicha();
            extremoActualDer=ficha.getExtremoIZQ();
            fichasJugadas.addLast(ficha);
        }else if (ficha.getExtremoIZQ() == extremoActualIzq) {
            ficha.rotarFicha(); // ahora el nuevo extremoIzq es el correcto
            fichasJugadas.addFirst(ficha);
            extremoActualIzq = ficha.getExtremoDER();
        } else if (ficha.getExtremoIZQ() == extremoActualDer) {
            // No necesita rotar, va al final
            fichasJugadas.addLast(ficha);
            extremoActualDer = ficha.getExtremoIZQ();
        }
        return true;
    }

    //valida que coincida con alguno de los extremos actuales
    public boolean fichaValida(FichaDomino ficha){
        boolean rta;
        if(ficha.getExtremoIZQ()== extremoActualIzq || ficha.getExtremoIZQ()== extremoActualDer){
            rta=true;
        } else if (ficha.getExtremoDER()== extremoActualIzq || ficha.getExtremoDER()== extremoActualDer) {
            rta=true;
        }
        else{
            rta=false;
        }
        return rta;
    }

    public void actualizarExtremos(int nuevoExtIzq, int nuevoExtDer){
        setExtremoActualIzq(nuevoExtIzq);
        setExtremoActualDer(nuevoExtDer);
    }
    //Getters
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
