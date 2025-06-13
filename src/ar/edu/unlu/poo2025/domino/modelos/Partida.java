package ar.edu.unlu.poo2025.domino.modelos;

import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import java.io.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Partida extends ObservableRemoto implements IPartida, Serializable {
    private static final long serialVersionUID = 1L;
    private Jugador jugadorActual;
    private Jugador ganadorPartido;
    private boolean partidaTerminada;
    private int cantJugadoresActuales;
    private int jugadoresQueNoPuedieronJugar=0;
    private int maxjugadores;
    private Tablero tablero;
    private Mazo mazo;
    private int turnoActual;
    private int puntajeMax;
    private ArrayList<Jugador> jugadores;
    private ArrayList<FichaDomino> fichasJugadores;


    public Partida(Jugador jugadorActual, Tablero tablero, Mazo mazo, int puntajeMax, ArrayList<Jugador> jugadores) throws RemoteException{
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
    public void determinarMaxJugadores(int num) throws RemoteException{
        maxjugadores=num;
    }

    public void asignarFichas(ArrayList<FichaDomino> fichasPorJugador) throws RemoteException{
        this.fichasJugadores = fichasPorJugador;
    }

    //verifica que jugador arranca y juega la primer ficha
    public Jugador jugadorYjugadaInicial() throws RemoteException{
        Jugador jugadorInicial = null;
        FichaDomino mejorFicha = null;
        int mejorValor = -1;

        // Paso 1: buscar el doble más alto entre todos los jugadores
        for (Jugador jugador : jugadores) {
            FichaDomino doble = jugador.dobleMasAlto();
            if (doble != null) {//tiene doble
                int valor = doble.getExtremoIZQ();//el izq o der es lo mismo pq son =
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
                FichaDomino ficha = jugador.getFichaConMayorSuma(); //devuelve ficha mayor suma del jugador
                int suma = ficha.getExtremoIZQ() + ficha.getExtremoDER();//vuelve a sumar para guardar el valor
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
            this.notificarObservadores(Eventos.AGREGA_FICHA_TABLERO);
            jugadorInicial.getFichas().remove(mejorFicha);
        }

        return jugadorInicial;
    }


    @Override
    public boolean agregarJugador(String nombre) throws RemoteException{
        if(cantJugadoresActuales< maxjugadores){
            Jugador newjugador= new Jugador(nombre);
            newjugador.setPuntaje(0);
            newjugador.setFichas(new ArrayList<>());
            jugadores.add(newjugador);
            cantJugadoresActuales++;

            this.notificarObservadores(Eventos.JUGADOR_AGREGADO);

            return true;
        }else {
            return false;//esta en el maximo de los jugadores permitidos
        }
    }

    //devuelve solo el estado inicial de la partida
    //raro que devuelva un bool, todavia no valido en que caso es false
    @Override
    public boolean iniciarPartida() throws RemoteException{
        tablero= new Tablero();
        mazo.inicializarFichas();
        mazo.repartirFichas(jugadores);
        Jugador jugadorqueArranca = jugadorYjugadaInicial();//ya asigna la primer ficha al tablero
        turnoActual =jugadores.indexOf(jugadorqueArranca);
        //turnoActual= posicion del array en el que esta 'jugadorqueArranca'

        this.notificarObservadores(Eventos.PARTIDA_INICIADA);
        return true;
    }

    public void avanzarTurno() throws RemoteException{
        turnoActual = (turnoActual + 1) % jugadores.size(); //% hace que se comporte como una cola circular
        this.notificarObservadores(Eventos.CAMBIO_TURNO);
    }

    //manejara la logica de los turnos
    public boolean ejecutarTurno(Tablero tablero, Mazo mazo) throws RemoteException{
        Jugador jugadorActual= jugadores.get(turnoActual);//tengo a jugadorActual como atrib.local para mas seguridad

        if(jugadorActual.tieneFichas()){
            boolean pudojugar=jugadorActual.puedeJugar(tablero,mazo);
            if(pudojugar){
                this.notificarObservadores(Eventos.JUGADOR_JUGO_FICHA);
                jugadoresQueNoPuedieronJugar=0;//reinicio
                if (!jugadorActual.tieneFichas()) {//el jugador jugo y se quedo sin fichas(ganador mano)
                    terminoRonda(jugadorActual); // este jugador ganó la ronda
                    return true;
                } else {
                    avanzarTurno();
                    return true;
                }
            }else{
                jugadoresQueNoPuedieronJugar++;
                avanzarTurno();
            }
        }else {
            avanzarTurno();
            return false;
        }
        // Verificación de cierre por bloqueo
        if (jugadoresQueNoPuedieronJugar == jugadores.size() && mazo.estaVacio()) {
            bloqueoJuego();
            return false;
        }
        return false;
    }

    private void terminoRonda(Jugador ganadorMano) throws RemoteException{
        int puntosGanados = recuentoPuntosMano(ganadorMano); // ← cuenta los puntos de los demás jugadores
        ganadorMano.sumarPuntos(puntosGanados);             // ← los suma al jugador ganador
        this.notificarObservadores(Eventos.MANO_TERMINADA);

        if (ganadorMano.getPuntaje() >= puntajeMax) {
            hayGanadorPartido(ganadorMano); // ← termina el juego
        } else {
            nuevaMano();         // ← empieza una nueva mano
        }
    }

    private void nuevaMano() throws RemoteException{
        // Reinicio del tablero y el mazo
        tablero.limpiezaTablero();
        mazo.inicializarFichas();

        // Limpiar y repartir nuevas fichas
        for (Jugador j : jugadores) {
            j.getFichas().clear(); // limpieza explícita
        }
        mazo.repartirFichas(jugadores);

        // El jugador que estaba en turno actual arranca
        Jugador jugadornuevamano = jugadores.get(turnoActual);

        // Que juegue cualquier ficha de su mano (por ejemplo la primera)
        FichaDomino fichaInicio = jugadornuevamano.nuevaMano();
        tablero.agregaFichaTablero(fichaInicio);
        jugadornuevamano.getFichas().remove(fichaInicio);
    }

    //antes de avanzar, deben sumarse los puntos de cada jugador
    @Override
    public int recuentoPuntosMano(Jugador ganador) throws RemoteException {
        int puntos = 0;
        for (Jugador j : jugadores) {
            if (!j.equals(ganador)) {
                puntos += j.recuentoPuntosJugador();
            }
        }
        return puntos;
    }

    @Override
    public Jugador hayGanadorPartido(Jugador ganador) throws RemoteException{
        this.ganadorPartido = ganador; // ← acá se guarda el ganador
        this.partidaTerminada = true;

        this.notificarObservadores(Eventos.PARTIDA_TERMINADA);
        return ganador;
    }

    public void bloqueoJuego() throws RemoteException{
        Jugador ganador = null;
        int menorPuntaje = Integer.MAX_VALUE;
        ArrayList<Jugador> empatados = new ArrayList<>();
        this.notificarObservadores(Eventos.JUEGO_BLOQUEADO);
        for (Jugador j : jugadores) {
            int puntos = j.recuentoPuntosJugador();
            if (puntos < menorPuntaje) {
                menorPuntaje = puntos;
                ganador = j;
                empatados.clear();
                empatados.add(j);
            } else if (puntos == menorPuntaje) {
                empatados.add(j);
            }
        }

        // En caso de empate, gana el que esté más cerca del jugador que abrió la mano
        if (empatados.size() > 1) {
            for (int i = 0; i < jugadores.size(); i++) {
                if (empatados.contains(jugadores.get(i))) {
                    ganador = jugadores.get(i);
                    break;
                }
            }
        }

        // Sumar puntos del resto
        int puntosGanados = 0;
        for (Jugador j : jugadores) {
            if (!j.equals(ganador)) {
                puntosGanados += j.recuentoPuntosJugador();
            }
        }
        if(ganador != null){
            ganador.sumarPuntos(puntosGanados);
        }

        // Verificás si ganó la partida
        if ((ganador != null ? ganador.getPuntaje() : 0) >= puntajeMax) {
            hayGanadorPartido(ganador); // ← esto asigna también a ganadorPartido
        } else {
            nuevaMano(); // sigue jugando
        }

        jugadoresQueNoPuedieronJugar = 0; // reinicio el contador
    }

    @Override
    public Jugador[] getJugadores() throws RemoteException {
        Jugador[] jugadoresArray = new Jugador[this.jugadores.size()];
        return this.jugadores.toArray(jugadoresArray);
    }

    //ACOMODAR
    @Override
    public void cerrar(IObservadorRemoto controlador, Jugador jugador) throws RemoteException {
        // Primero, removemos al observador para que no reciba más notificaciones
        removerObservador(controlador);
        // Luego, desconectamos al usuario
        this.hayGanadorPartido(jugador);
    }

    //persistencia ACOMODAR
    @Override
    public boolean guardarMensajes() throws RemoteException {
        try {
            FileOutputStream fos = new FileOutputStream("data/mensajes.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.mensajes);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cargarMensajes() throws RemoteException {
        try {
            FileInputStream fis = new FileInputStream("data/mensajes.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.mensajes = (ArrayList<Mensaje>) ois.readObject();
            this.notificarObservadores(Eventos.NUEVO_MENSAJE);
            ois.close();
            fis.close();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Getters
    public Jugador getJugadorActual() {
        return jugadores.get(turnoActual);
    }

    public Jugador getGanadorPartido() {
        return ganadorPartido;
    }
    public boolean getpartidaTerminada(){
        return partidaTerminada;
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
