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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FichaDomino otra = (FichaDomino) obj;

        // Comparación directa
        boolean igualesNormales = this.extremoIZQ == otra.getExtremoIZQ() && this.extremoDER == otra.getExtremoDER();

        // Comparación invertida (por si la ficha está rotada)
        boolean igualesInvertidas = this.extremoIZQ == otra.getExtremoDER() && this.extremoDER == otra.getExtremoIZQ();

        return igualesNormales || igualesInvertidas;
    }

    @Override
    public int hashCode() {
        // Suma y mínimo-máximo para hacer que [2|5] y [5|2] tengan el mismo hash
        int menor = Math.min(extremoIZQ, extremoDER);
        int mayor = Math.max(extremoIZQ, extremoDER);
        return 31 * menor + mayor;
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
