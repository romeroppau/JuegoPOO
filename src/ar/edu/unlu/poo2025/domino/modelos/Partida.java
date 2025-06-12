package ar.edu.unlu.poo2025.domino.modelos;

import java.util.ArrayList;

public class Partida implements IModelo {
    private Jugador jugadorActual;
    private int cantJugadoresActuales;
    private int maxjugadores;
    private Tablero tablero;
    private Mazo mazo;
    private int turnoActual;
    private int puntajeMax;
    private ArrayList<Jugador> jugadores;
    private ArrayList<FichaDomino> fichasJugadores;


    public Partida(Jugador jugadorActual, Tablero tablero, Mazo mazo, int puntajeMax, ArrayList<Jugador> jugadores) {
        this.jugadorActual=jugadorActual;
        this.jugadores = jugadores;
        this.cantJugadoresActuales = 0;
        this.tablero=tablero;
        this.mazo=mazo;
        this.turnoActual = 0;
        this.puntajeMax=puntajeMax;
        this.fichasJugadores= new ArrayList<>();
    }
    //validar que sean 2, 3 o 4
    public void determinarMaxJugadores(int num){
        maxjugadores=num;
    }

    public void asignarFichas(ArrayList<FichaDomino> fichasPorJugador) {
        this.fichasJugadores = fichasPorJugador;
    }

    public Jugador jugadorYjugadaInicial() {
        Jugador jugadorInicial = null;
        FichaDomino mejorFicha = null;
        int mejorValor = -1;

        // Paso 1: buscar el doble más alto entre todos los jugadores
        for (Jugador jugador : jugadores) {
            FichaDomino doble = jugador.dobleMasAlto();
            if (doble != null) {
                int valor = doble.getExtremoIZQ();
                if (valor > mejorValor) {
                    mejorValor = valor;
                    jugadorInicial = jugador;
                    mejorFicha = doble;
                }
            }
        }
        // Paso 2: si nadie tenía dobles, buscar la ficha de mayor suma
        if (jugadorInicial == null) {
            for (Jugador jugador : jugadores) {
                FichaDomino ficha = jugador.getFichaConMayorSuma(); // también lo tenés en Jugador
                int suma = ficha.getExtremoIZQ() + ficha.getExtremoDER();
                if (suma > mejorValor) {
                    mejorValor = suma;
                    jugadorInicial = jugador;
                    mejorFicha = ficha;
                }
            }
        }
        // colocar ficha en el tablero
        if (mejorFicha != null) {
            tablero.agregaFichaTablero(mejorFicha);
            jugadorInicial.getFichas().remove(mejorFicha);
        }
        return jugadorInicial;
    }


    @Override
    public boolean agregarJugador(String nombre) {
        if(cantJugadoresActuales< maxjugadores){
            Jugador newjugador= new Jugador(nombre);
            newjugador.setPuntaje(0);
            newjugador.setFichas(new ArrayList<>());
            jugadores.add(newjugador);
            cantJugadoresActuales++;
            return true;
        }else {
            return false;//hay mas de los jugadores permitidos
        }
    }
    //devuelve solo el estado inicial de la partida
    //raro que devuelva un bool, todavia no valido en que caso es false
    @Override
    public boolean iniciarPartida() {
        Tablero tablero= new Tablero();
        mazo.inicializarFichas();
        mazo.repartirFichas(jugadores);
        Jugador jugadorqueArranca = jugadorYjugadaInicial();//ya asigna la primer ficha al tablero
        turnoActual =jugadores.indexOf(jugadorqueArranca);
        return true;
    }

    public void avanzarTurno() {
        turnoActual = (turnoActual + 1) % jugadores.size();
    }

    //manejara la logica de los turnos
    public boolean puedeJugarJugador_turno(Tablero tablero, Mazo mazo) {
        Jugador jugadorActual= jugadores.get(turnoActual);//tengo a jugadorActual como atrib.local para mas seguridad
        //verificar primero si tiene fichas
        ArrayList<FichaDomino> fichasJugador=jugadorActual.getFichas();
        if(!fichasJugador.isEmpty()){
            boolean pudojugar=jugadorActual.puedeJugar(tablero,mazo);
            if(pudojugar){
                avanzarTurno();//antes de avanzar, se deben sumar los puntos
                return true;
            }else {
                bloqueoJuego();
            }
        }else {
            //si no tiene fichas> Verificar si tiene 150 puntos (ganadorPartida)> Si no suma 150p, se inicia nueva mano
            if (jugadorActual.getPuntaje() >= 150) {
                hayGanadorPartido();
            }else {
                recuentoPuntosMano();
                terminoMano();
            }
        }

        return false;
    }

    public Jugador bloqueoJuego() {

        return null;
    }

    public void limpiezaTablero() {
        // TODO: implementar
    }

    public boolean terminoMano() {
        // TODO: implementar
        return false;
    }

    public int recuentoPuntosMano() {
        // TODO: implementar
        return 0;
    }

    //antes de avanzar, deben sumarse los puntos de cada jugador
    @Override
    public int recuentoPuntos() {

        return 0;
    }

    @Override
    public Jugador hayGanadorPartido() {
        Jugador jug = null;
        return jug;
    }

    public void persistirEstado() {
        // TODO: implementar
    }

    public void cargarEstado() {
        // TODO: implementar
    }



    // Getters
    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }
    public int getCantJugadoresActuales() {
        return cantJugadoresActuales;
    }
    public Tablero getTablero() {
        return tablero;
    }
    public Mazo getMazo() {
        return mazo;
    }
    public int getTurnoActual() {
        return turnoActual;
    }
    public int getPuntajeMax() {
        return puntajeMax;
    }

    // Setters
    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }
    public void setCantJugadoresActuales(int cantJugadoresActuales) {
        this.cantJugadoresActuales = cantJugadoresActuales;
    }
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }
    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }
    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }
    public void setPuntajeMax(int puntajeMax) {
        this.puntajeMax = puntajeMax;
    }
}
