package ar.edu.unlu.poo2025.domino.modelos;

import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Partida extends ObservableRemoto implements IPartida, Serializable {
    private static final long serialVersionUID = 1L;
    private Jugador jugadorActual;
    private Jugador ganadorPartido;
    private int cantJugadoresActuales;
    private int jugadoresQueNoPuedieronJugar=0;
    private int maxjugadores;
    private Tablero tablero;
    private Mazo mazo;
    private int turnoActual;
    private int puntajeMax;
    private ArrayList<Jugador> jugadores;
    private ArrayList<FichaDomino> fichasJugadores;
    private ManejadorTurnos manejadorTurnos;


    public Partida( Tablero tablero, Mazo mazo, int puntajeMax, ArrayList<Jugador> jugadores) throws RemoteException{
        //this.jugadorActual=jugadorActual; no lo pongo aca porque se define logicamente en los metodos.
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
    public Jugador jugadorInicial() throws RemoteException{
        FichaDomino mejorFicha = null;
        int mejorValor = -1;

        // Paso 1: buscar el doble más alto entre todos los jugadores
        for (Jugador jugador : jugadores) {
            FichaDomino doble = jugador.dobleMasAlto();
            if (doble != null) {//tiene doble
                int valor = doble.getExtremoIZQ();//el izq o der es lo mismo pq son =
                if (valor > mejorValor) {
                    mejorValor = valor;
                    jugadorActual = jugador; //defino el primer jugador
                    mejorFicha = doble;
                }
            }
        }
        // Paso 2: si nadie tenía dobles, buscar la ficha de mayor suma
        if (mejorFicha==null) {
            for (Jugador jugador : jugadores) {
                FichaDomino ficha = jugador.getFichaConMayorSuma(); //devuelve ficha mayor suma del jugador
                int suma = ficha.getExtremoIZQ() + ficha.getExtremoDER();//vuelve a sumar para guardar el valor
                if (suma > mejorValor) {
                    mejorValor = suma;
                    jugadorActual = jugador;
                    mejorFicha = ficha;
                }
            }
        }
        // colocar ficha en el tablero
        if (mejorFicha != null) {
            jugadaInicial(mejorFicha,jugadorActual);
        }

        return this.jugadorActual;//si devuelve null, algo paso
    }
    //solo agrega la primer ficha al tablero
    public boolean jugadaInicial(FichaDomino fichaInicial, Jugador jugadorInicial) throws RemoteException{
        boolean rta=false;
        if(tablero.agregaFichaTablero(fichaInicial)) {
            jugadorInicial.getFichas().remove(fichaInicial);
            rta=true;
        }
        else {
            rta=false;
        }
        return rta;
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
    @Override
    public boolean iniciarPartida() throws RemoteException{
        if (this.tablero == null) {
            this.tablero = new Tablero();
        }
        else {
            return false; //si da falso pudo haber fallado la inicializacion del tablero
        }

        this.mazo.inicializarFichas();
        this.mazo.repartirFichas(jugadores);

        this.manejadorTurnos = new ManejadorTurnos(jugadores);
        //tengo el jugador que arranca en jugadorActual, solo tengo que llamar a la funcion
        jugadorInicial();//ya asigna la primer ficha al tablero y establece en jugadorActual, quien inicia la partida
        this.manejadorTurnos.setTurnoActual(jugadores.indexOf(jugadorActual)); // seteo del que arranca
        this.notificarObservadores(Eventos.PARTIDA_INICIADA);
        this.notificarObservadores(Eventos.JUGADA_INICIAL);

        return true;
    }

    public void avanzarTurno() throws RemoteException{
        manejadorTurnos.siguienteTurno();
        jugadorActual = manejadorTurnos.getJugadorActual();
        this.notificarObservadores(Eventos.CAMBIO_TURNO);
    }

    //manejara la logica de los turnos
    public boolean ejecutarTurno(Tablero tablero, Mazo mazo) throws RemoteException{
        jugadorActual=manejadorTurnos.getJugadorActual();//actualizo por las dudas
        if (jugadorActual.tieneFichas()) {
            boolean pudoJugar = jugadorActual.realizarTurno(tablero, mazo);

            if (pudoJugar) {
                this.notificarObservadores(Eventos.JUGADOR_JUGO_FICHA);
                jugadoresQueNoPuedieronJugar = 0;

                if (!jugadorActual.tieneFichas()) {
                    // Ganó la mano
                    terminoRonda(jugadorActual);
                    return true;
                }

            } else {
                jugadoresQueNoPuedieronJugar++;
            }
        } else {
            jugadoresQueNoPuedieronJugar++;
        }

        // Verificar si el juego está bloqueado
        if (jugadoresQueNoPuedieronJugar == getCantJugadoresActuales() && mazo.estaVacio()) {
            bloqueoJuego();
            return false;
        }

        avanzarTurno(); // Avanzar siempre que no se haya terminado la mano
        return true;
    }

    private void terminoRonda(Jugador ganadorMano) throws RemoteException{
        int puntosGanados = recuentoPuntosMano(ganadorMano);
        ganadorMano.sumarPuntos(puntosGanados);
        this.notificarObservadores(Eventos.MANO_TERMINADA);

        if (ganadorMano.getPuntaje() >= puntajeMax) {
            hayGanadorPartido(ganadorMano);
        } else {
            nuevaMano(); // Reinicia la mano, incluyendo el manejador de turnos
        }
    }

    @Override
    public void nuevaMano() throws RemoteException{
        // Reinicio del tablero y del mazo
        tablero.limpiezaTablero();
        mazo.inicializarFichas();

        // Limpieza de fichas anteriores y nueva repartición
        for (Jugador j : jugadores) {
            j.getFichas().clear();
        }
        mazo.repartirFichas(jugadores);

        jugadoresQueNoPuedieronJugar = 0;

        this.manejadorTurnos = new ManejadorTurnos(jugadores);
        //tengo el jugador que arranca en jugadorActual, solo tengo que llamar a la funcion
        jugadorInicial();//ya asigna la primer ficha al tablero y establece en jugadorActual, quien inicia la partida
        this.manejadorTurnos.setTurnoActual(jugadores.indexOf(jugadorActual)); // seteo del que arranca
        this.notificarObservadores(Eventos.NUEVA_MANO);

        ejecutarTurno(tablero,mazo);

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

    public Map<String, Integer> obtenerPuntajesJugadores() {
        Map<String, Integer> puntajes = new HashMap<>();
        for (Jugador j : jugadores) {
            puntajes.put(j.getNombre(), j.getPuntaje());
        }
        return puntajes;
    }


    @Override
    public Jugador hayGanadorPartido(Jugador ganador) throws RemoteException{
        this.ganadorPartido = ganador; // ← acá se guarda el ganador
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

    //hago esto para en caso de que se termine la partida, devolver el jugador con mayor puntaje
    //pero el cual no ncesariamente es el ganador (porque no llego a los puntos)
    public Jugador getJugadorConMayorPuntaje() {
        Jugador mejor = null;
        int max = -1;
        for (Jugador j : jugadores) {
            if (j.getPuntaje() > max) {
                max = j.getPuntaje();
                mejor = j;
            }
        }
        return mejor;
    }

    @Override
    public void cerrar(IObservadorRemoto controlador, Jugador jugador) throws RemoteException {
        removerObservador(controlador);

        jugadores.remove(jugador); // o marcar como desconectado

        if (jugadores.size() < 2) {
            // Si ya no se puede seguir jugando
            this.notificarObservadores(Eventos.PARTIDA_TERMINADA);
        } else {
            this.notificarObservadores(Eventos.JUGADOR_DESCONECTADO);
        }
    }
    @Override
    public ManejadorTurnos getManejadorTurnos() throws RemoteException {
        return manejadorTurnos;
    }

    /*
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
    */

    // Getters
    public Jugador getJugadorActual() throws RemoteException{
        return manejadorTurnos.getJugadorActual();
    }

    public Jugador getGanadorPartido() throws RemoteException{
        return ganadorPartido;
    }
    public int getCantJugadoresActuales() throws RemoteException{
        return cantJugadoresActuales;
    }
    public Tablero getTablero() throws RemoteException{
        return tablero;
    }
    public Mazo getMazo() throws RemoteException{
        return mazo;
    }
    public int getTurnoActual() throws RemoteException{
        return turnoActual;
    }
    public int getPuntajeMax() throws RemoteException{
        return puntajeMax;
    }

    // Setters
    public void setJugadorActual(Jugador jugadorActual) throws RemoteException{
        this.jugadorActual = jugadorActual;
    }
    public void setCantJugadoresActuales(int cantJugadoresActuales) throws RemoteException{
        this.cantJugadoresActuales = cantJugadoresActuales;
    }
    public void setTablero(Tablero tablero) throws RemoteException{
        this.tablero = tablero;
    }
    public void setMazo(Mazo mazo) throws RemoteException{
        this.mazo = mazo;
    }
    public void setTurnoActual(int turnoActual) throws RemoteException{
        this.turnoActual = turnoActual;
    }
    public void setPuntajeMax(int puntajeMax) throws RemoteException{
        this.puntajeMax = puntajeMax;
    }
    public void setMaxjugadores(int maxjugadores)throws RemoteException{
        this.maxjugadores=maxjugadores;
    }
}
