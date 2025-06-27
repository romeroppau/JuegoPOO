package ar.edu.unlu.poo2025.domino.modelos;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface IPartida  extends IObservableRemoto, Remote {
    //metodos que el controlador necesita para actualizarse
    //Métodos que gestionan la lógica de la interacción, como la asignación de fichas, el avance de turno, y el control de jugadas.
    //logica entre jugadores-vista / eventos y acciones del jugador

    //cualquier metodo que extienda de Iobservable tiene que poder lanzar excepciones
    boolean agregarJugador(String nombre) throws RemoteException;
    public boolean iniciarPartida() throws RemoteException;
    public Jugador hayGanadorPartido(Jugador ganadorPartido) throws RemoteException;
    public void nuevaMano() throws RemoteException;
    public Map<String, Integer> obtenerPuntajesJugadores() throws RemoteException;
    public int recuentoPuntosMano(Jugador ganadorMano) throws RemoteException;
    public Jugador[] getJugadores() throws RemoteException;
    public Tablero getTablero() throws RemoteException;
    public Jugador getJugadorActual() throws RemoteException;
    public Jugador getGanadorPartido() throws RemoteException;
    public Mazo getMazo() throws RemoteException;
    public boolean ejecutarTurno() throws RemoteException;
    public int getPuntajeMax()throws RemoteException;
    public int getCantJugadoresActuales() throws RemoteException;
    public void setPuntajeMax(int puntajeMax) throws RemoteException;
    public void setMaxjugadores(int maxjugadores)throws RemoteException;

    //para mostrar
    public Jugador getUltimoGanadorMano() throws RemoteException;
    public int getPuntosUltimaMano() throws RemoteException;
    public Jugador getJugadorInicial() throws RemoteException;
    public FichaDomino getFichaInicial() throws RemoteException;

    // Este metodo es nuevo y específico para la desconexión remota segura
    public void cerrar(IObservadorRemoto controlador) throws RemoteException;

    //interactivo
    public boolean ejecutarTurnoInteractivo(int indice) throws RemoteException;
    public ArrayList<FichaDomino> verFichasJugables() throws RemoteException;
    public void sacarHastaTenerJugada() throws RemoteException;

    public int getIndiceFichaJugadorActual(FichaDomino ficha) throws RemoteException;

    //persistencia ACOMODAR
    //boolean guardarMensajes() throws RemoteException;
    //boolean cargarMensajes() throws RemoteException;

}
