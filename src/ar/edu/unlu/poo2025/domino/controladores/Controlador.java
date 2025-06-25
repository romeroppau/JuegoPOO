package ar.edu.unlu.poo2025.domino.controladores;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import ar.edu.unlu.poo2025.domino.modelos.Eventos;
import ar.edu.unlu.poo2025.domino.modelos.*;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;
import ar.edu.unlu.poo2025.domino.vistas.EstadoVista;
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
            if (this.modelo.getPuntajeMax() <= 50) {
                this.vista.mostrarError("Primero debe establecer un puntaje objetivo mayor a 50.");
                return;
            }
            if (this.modelo.getCantJugadoresActuales() < 2 || this.modelo.getCantJugadoresActuales() >4) {
                this.vista.mostrarError("Debe haber al menos dos jugadores y menos de 4 para iniciar.");
                return;
            }
            this.modelo.iniciarPartida();
            //ACA SOLO INICIALIZO MAZO, TABLERO, JUGADORINICIAL, JUGADA INICIAL, TURNO_ACTUAL,
        } catch (RemoteException e) {
            this.vista.mostrarError("Error al iniciar partida: " + e.getMessage());
        }
    }

    public void ejecutarTurno() {
        try {//tiene que pasar el turno cuando termina
            boolean jugo = this.modelo.ejecutarTurno();
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
            return null;//hubo error
        }
    }

    public Map<String, Integer> getPuntajesJugadores() {
        try {
            return modelo.obtenerPuntajesJugadores();
        } catch (RemoteException e) {
            vista.mostrarError("No se pudo obtener el puntaje de los jugadores.");
            return new HashMap<>();
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

    public void nuevaMano(){
        try {
            this.modelo.nuevaMano();
        }catch (RemoteException e){
            this.vista.mostrarError("No se ha podido concretar una nueva mano");
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
            this.modelo.cerrar(this); // ya no importa qué jugador es
            this.vista.mostrarMensaje("Has salido de la partida.");
            this.vista.setEstadoActual(EstadoVista.FIN_PARTIDA);
        } catch (RemoteException e) {
            vista.mostrarError("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public Jugador getGanadorMano() {
        try {
            return this.modelo.getUltimoGanadorMano();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public int getPuntosMano() {
        try {
            return this.modelo.getPuntosUltimaMano();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public Jugador getJugadorInicial() {
        try {
            return this.modelo.getJugadorInicial();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public FichaDomino getFichaInicial() {
        try {
            return this.modelo.getFichaInicial();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
                        this.vista.actualizar(Eventos.JUGADOR_JUGO_FICHA); // puede usarse para actualizar tablero
                        break;
                    case CAMBIO_TURNO:
                        this.vista.actualizar(Eventos.CAMBIO_TURNO);
                        break;
                    case MANO_TERMINADA:
                        this.vista.actualizar(Eventos.MANO_TERMINADA);
                        break;
                    case NUEVA_MANO:
                        this.vista.actualizar(Eventos.NUEVA_MANO);
                        break;
                    case JUEGO_BLOQUEADO:
                        this.vista.actualizar(Eventos.JUEGO_BLOQUEADO);
                        break;
                    case PARTIDA_TERMINADA:
                        Jugador ganador = ((IPartida) origen).getGanadorPartido();
                        this.vista.mostrarGanador(ganador);
                        this.vista.actualizar(Eventos.PARTIDA_TERMINADA);
                        break;
                    default:
                        break;
                }
            }catch (Exception e) {
                this.vista.mostrarError("Error al procesar evento: " + e.getMessage());
            }

        }
    }

}