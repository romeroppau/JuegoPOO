package ar.edu.unlu.poo2025.domino.modelos;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.observer.IObservadorRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IPartida  extends IObservableRemoto {
    //metodos que el controlador necesita para actualizarse
    //Métodos que gestionan la lógica de la interacción, como la asignación de fichas, el avance de turno, y el control de jugadas.
    //logica entre jugadores-vista / eventos y acciones del jugador

    //cualquier metodo que extienda de Iobservable tiene que poder lanzar excepciones
    boolean agregarJugador(String nombre) throws RemoteException;
    public boolean iniciarPartida() throws RemoteException;
    public Jugador hayGanadorPartido(Jugador ganadorPartido) throws RemoteException;
    public int recuentoPuntosMano(Jugador ganadorMano) throws RemoteException;
    public Jugador[] getJugadores() throws RemoteException;
    // Este metodo es nuevo y específico para la desconexión remota segura
    void cerrar(IObservadorRemoto controlador, Jugador jugador) throws RemoteException;

    //persistencia ACOMODAR
    boolean guardarMensajes() throws RemoteException;
    boolean cargarMensajes() throws RemoteException;

}
