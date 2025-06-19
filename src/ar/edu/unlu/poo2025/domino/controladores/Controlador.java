package ar.edu.unlu.poo2025.domino.controladores;

import java.io.Serializable;
import java.rmi.RemoteException;
import ar.edu.unlu.poo2025.domino.modelos.Eventos;
import ar.edu.unlu.poo2025.domino.modelos.*;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;
import ar.edu.unlu.poo2025.domino.vistas.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public class Controlador implements IControladorRemoto {
    private IPartida modelo;
    private  IVista vista;

    public Controlador(){
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    public boolean agregarJugador(String nombre){
        try {
            return this.modelo.agregarJugador(nombre);
        } catch (RemoteException e) {
            vista.mostrarError("Error al agregar jugador: " + e.getMessage());
            return false;
        }
    }

    public void iniciarPartida() {
        try {
            if (modelo.getPuntajeMax() <= 0) {
                this.vista.mostrarError("Primero debe establecer un puntaje objetivo mayor a 0.");
                return;
            }
            if (modelo.getCantJugadoresActuales() < 2) {
                this.vista.mostrarError("Debe haber al menos dos jugadores para iniciar.");
                return;
            }
            this.modelo.iniciarPartida();
        } catch (RemoteException e) {
            this.vista.mostrarError("Error al iniciar partida: " + e.getMessage());
        }
    }

    public void ejecutarTurno() {
        try {//tiene que pasar el turno cuando termina
            boolean jugo = this.modelo.ejecutarTurno(modelo.getTablero(), modelo.getMazo());
            if (!jugo) {
                vista.mostrarMensaje("No se pudo jugar en este turno.");
            }
        } catch (RemoteException e) {
            vista.mostrarError("Error al ejecutar turno: " + e.getMessage());
        }
    }

    public Jugador[] getJugadores() {
        try {
            return this.modelo.getJugadores();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener jugador actual: " + e.getMessage());
            return new Jugador[0];
        }
    }

    public Jugador getJugadorActual() {
        try {
            return this.modelo.getJugadorActual();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener el jugador actual: " + e.getMessage());
            return null;
        }
    }

    public Tablero getTablero() {
        try {
            return this.modelo.getTablero();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener el tablero: " + e.getMessage());
            return null;
        }
    }

    public Jugador getGanadorPartido() {
        try {
            return this.modelo.getGanadorPartido();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener el ganador del partido: " + e.getMessage());
            return null;
        }
    }

    public Mazo getMazo() {
        try {
            return this.modelo.getMazo();
        } catch (RemoteException e) {
            vista.mostrarError("Error al obtener el mazo: " + e.getMessage());
            return null;
        }
    }

    public void cerrarConexion() {
        try {
            this.modelo.cerrar(this, this.modelo.getJugadorActual());
        } catch (RemoteException e) {
            vista.mostrarError("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    public <T extends IObservableRemoto> Controlador(T modelo) {
        try {
            this.setModeloRemoto(modelo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modeloRemoto) throws RemoteException {
        this.modelo = (IPartida) modeloRemoto;
    }

    @Override
    public void actualizar(IObservableRemoto origen, Object evento) throws RemoteException {
        if (evento instanceof Eventos) {
            try {
                switch ((Eventos) evento) {
                    //vistainiciacion:Mostrar jugadores conectados, permitir ingresar nombre e iniciarJuego partida
                    //vistajuego: Mostrar tablero, fichas del jugador, turnos, jugadas
                    //vistaFinPartida: Mostrar quién ganó, puntos finales, opción de reiniciar o salir

                    case JUGADOR_AGREGADO:
                        this.vista.mostrarMensaje("Se agregó un nuevo jugador.");
                        this.vista.actualizar(Eventos.JUGADOR_AGREGADO);
                        break;
                    case PARTIDA_INICIADA:
                        this.vista.mostrarMensaje("La partida ha comenzado.");
                        this.vista.actualizar(Eventos.PARTIDA_INICIADA); // activa el flujo de turno
                        break;
                    case JUGADA_INICIAL:
                        this.vista.mostrarMensaje("Se colocó la ficha inicial.");
                        this.vista.actualizar(Eventos.JUGADA_INICIAL); // actualiza estado gráfico o consola
                        break;
                    case JUGADOR_JUGO_FICHA:
                        this.vista.mostrarMensaje("Un jugador jugó una ficha.");
                        this.vista.actualizar(Eventos.JUGADOR_JUGO_FICHA); // puede usarse para actualizar tablero
                        break;
                    case CAMBIO_TURNO:
                        this.vista.mostrarMensaje("Cambio de turno.");
                        this.vista.actualizar(Eventos.CAMBIO_TURNO);
                        break;
                    case MANO_TERMINADA:
                        this.vista.mostrarMensaje("Fin de la mano.");
                        this.vista.actualizar(Eventos.MANO_TERMINADA);
                        break;
                    case JUEGO_BLOQUEADO:
                        this.vista.mostrarMensaje(" El juego se ha bloqueado.");
                        this.vista.actualizar(Eventos.JUEGO_BLOQUEADO);
                        break;
                    case PARTIDA_TERMINADA:
                        Jugador ganador = ((IPartida) origen).getGanadorPartido();
                        this.vista.mostrarGanador(ganador);
                        break;
                    default:
                        break;
                }
            }catch (Exception e) {
                this.vista.mostrarError("Error al procesar evento: " + e.getMessage());
            }

        }
    }
    public void establecerPuntajeMaximo(int puntos) {
        try {
            modelo.setPuntajeMax(puntos);
            vista.mostrarMensaje("Puntaje máximo establecido correctamente: " + puntos);
        } catch (Exception e) {
            vista.mostrarError("Error al establecer el puntaje máximo: " + e.getMessage());
        }
    }

    public void determinarMaxJugadores(int cantidad) {
        try {
            modelo.setMaxjugadores(cantidad);
            vista.mostrarMensaje("Cantidad máxima de jugadores configurada en: " + cantidad);
        } catch (Exception e) {
            vista.mostrarError("Error al establecer la cantidad de jugadores: " + e.getMessage());
        }
    }

}