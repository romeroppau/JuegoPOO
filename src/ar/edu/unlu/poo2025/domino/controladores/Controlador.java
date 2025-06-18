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
    public void actualizar(IObservableRemoto iObservableRemoto, Object evento) throws RemoteException {
        if (evento instanceof Eventos) {
            switch ((Eventos) evento) {
                //vistainiciacion:Mostrar jugadores conectados, permitir ingresar nombre e iniciar partida
                //vistajuego: Mostrar tablero, fichas del jugador, turnos, jugadas
                //vistaFinPartida: Mostrar quién ganó, puntos finales, opción de reiniciar o salir

                case JUGADOR_AGREGADO:
                    //bloque de código se ejecuta cuando el modelo notifica que se ha agregado un jugador
                    this.vista.vistaIniciacion((Jugador[]) this.modelo.getJugadores());
                    //se llama a la vista para que actualice graficamente lo que corresponde
                    //y le paso por parametro: La lista de jugadores actuales,
                    //casteando a arreglo de tipo Jugador[] para transformar en array para la vista
                    break;
                case PARTIDA_INICIADA:
                    this.vista.vistaIniciacion( this.modelo.iniciarPartida());
                    break;
                case AGREGA_FICHA_TABLERO:
                    this.vista.vistaJuego( this.modelo.iniciarPartida());
                    break;
                case JUGADOR_JUGO_FICHA:
                    this.vista.vistaJuego((boolean) this.modelo.iniciarPartida());
                    break;
                case CAMBIO_TURNO:
                    this.vista.vistaJuego( this.modelo.iniciarPartida());
                    break;
                case MANO_TERMINADA:
                    this.vista.vistaJuego( this.modelo.iniciarPartida());
                    break;
                case JUEGO_BLOQUEADO:
                    this.vista.vistaJuego( this.modelo.iniciarPartida());
                    break;
                case PARTIDA_TERMINADA:
                    this.vista.vistaFinPartida( this.modelo.iniciarPartida());
                    break;
                default:
                    break;

            }
        }
    }
}
