package ar.edu.unlu.poo2025.domino.controladores;
import java.rmi.RemoteException;

import ar.edu.unlu.poo2025.domino.modelos.*;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;
import ar.edu.unlu.poo2025.domino.vistas.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControladorRemoto {
    private IPartida modelo;
    private IVista vista;

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public boolean C_agregarJugador(String nombre) {
        try {
            return this.modelo.agregarJugador(nombre);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    //persistencia ACOMODAR
    public void guardarChat() {
        try {
            this.modelo.guardarMensajes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cargarChat() {
        try {
            this.modelo.cargarMensajes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.modelo=(IPartida) t;
        this.modelo.agregarObservador(this);
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object arg1) throws RemoteException {
        if (arg1 instanceof Eventos) {
            switch ((Eventos) arg1) {
                case JUGADOR_AGREGADO:
                    this.vista.mostrarListaJugadores((Jugador[]) this.modelo.getJugadores());
                    break;
                case PARTIDA_INICIADA:
                    this.vista.mostrarChat((IMensaje[]) this.modelo.getMensajes());
                    break;
                default:
                    break;

            }
        }
    }
}
