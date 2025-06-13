package ar.edu.unlu.poo2025.domino.modelos;

import java.io.Serializable;

public class FichaDomino implements Serializable {
    private static final long serialVersionUID = 1L;
    private int extremoIZQ;
    private int extremoDER;

    public FichaDomino(int extremoIZQ,int extremoDER){
        this.extremoIZQ=extremoIZQ;
        this.extremoDER=extremoDER;
    }

    public boolean esfichaDoble(){
        return this.extremoIZQ==this.extremoDER;
    }
    @Override
    public String toString() {
        if (esfichaDoble()) {
            return "|" + extremoIZQ + "|" + extremoDER + "|"; // o "↕" + extremos
        } else {
            return "[" + extremoIZQ + "|" + extremoDER + "]";
        }
    }

    public void rotarFicha(){
        int aux=this.extremoIZQ;
        this.extremoIZQ=this.extremoDER;
        this.extremoDER=aux;
    }

    //Getters
    public int getExtremoDER() {
        return extremoDER;
    }
    public int getExtremoIZQ() {
        return extremoIZQ;
    }
}
