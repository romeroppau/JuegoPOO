package ar.edu.unlu.poo2025.domino.vistas;

import ar.edu.unlu.poo2025.domino.controladores.Controlador;
import ar.edu.unlu.poo2025.domino.modelos.Eventos;
import ar.edu.unlu.poo2025.domino.modelos.FichaDomino;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;


public class VistaConsola implements IVista {
    private static final long serialVersionUID = 1L;

    private Controlador controlador;
    //transient indica que el campo no debe serializarse ni enviarse x red
    private Scanner scanner;
    private String nombreJugador;
    private EstadoVista estadoActual;
    private boolean juegoYaIniciado=false;

    public VistaConsola() {
        this.scanner=new Scanner(System.in);;
        this.estadoActual=EstadoVista.ESTADO_INICIAL;
    }

    @Override
    public void iniciarJuego() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        mostrarMensaje("Es tu turno. Presioná ENTER para jugar.");
        scanner.nextLine();
        controlador.ejecutarTurno();
        estadoActual = EstadoVista.ESPERANDO_TURNO;
    }

    public void iniciar(){
        estadoActual = EstadoVista.ESPERANDO_JUGADORES;
        System.out.println("       JUEGO DOMINÓ        ");
        while (estadoActual != EstadoVista.FIN_PARTIDA) {
            MenuGeneral();

            // Evitás pedir entrada si la lógica no depende del jugador
            if (estadoActual == EstadoVista.ESPERANDO_JUGADORES || estadoActual == EstadoVista.JUGANDO) {
                Opciones();
            }

            // Pequeña pausa para permitir al hilo de eventos actuar
            try {
                Thread.sleep(100); // 100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mostrarMensaje("Juego finalizado. Gracias por jugar.");
    }

    //menues iniciales
    private void MenuGeneral() {
        System.out.println("\n--- ESTADO: " + estadoActual.name() + " ---");
        switch (estadoActual) {
            case ESTADO_INICIAL:
            case ESPERANDO_JUGADORES:
                menuConfiguracionInicial();
                break;
            case JUGANDO:
                menuJugadorTurno();
                break;
            case FIN_PARTIDA:
                resultadoFinal();
                break;
            default:
                System.out.println("Esperando acciones del juego...");
                break;
        }
    }

    private void menuConfiguracionInicial() {
        System.out.println("===== CONFIGURACIÓN DE PARTIDA =====");
        System.out.println("1. Establecer cantidad de jugadores");
        System.out.println("2. Establecer puntaje para ganar");
        System.out.println("3. Agregar jugador");
        System.out.println("4. Ver jugadores");
        System.out.println("5. Iniciar partida");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    private void menuJugadorTurno() {
        Jugador jugador = controlador.getJugadorActual();
        System.out.println("Turno de: " + jugador.getNombre());
        System.out.println("Tus fichas:");
        ArrayList<FichaDomino> fichas_jugador= jugador.getFichas();
        for (int i = 0; i < fichas_jugador.size(); i++) {
            System.out.println(i + ". " + fichas_jugador.get(i));
        }

        System.out.println("1. Intentar jugar");
        System.out.println("2. Ver tablero");
        System.out.println("0. Salir");
        System.out.print("Elegí una opción: ");
    }

    private void resultadoFinal() {
        System.out.println("Fin de la partida.");
        Jugador ganador = controlador.getGanadorPartido();
        if (ganador != null) {
            mostrarGanador(ganador);
        }
    }

    private void Opciones() {
        String opcion = scanner.nextLine();

        switch (estadoActual) {
            case ESPERANDO_JUGADORES:
                procesamientoConfig(opcion);
                break;
            case JUGANDO:

            case ESPERANDO_TURNO:
                procesamientoJuego(opcion);
                break;
            default:
                break;
        }
    }

    private void procesamientoConfig(String opcion) {
        switch (opcion) {
            case "1":
                try {
                    System.out.print("Ingrese cantidad de jugadores (2 a 4): ");
                    int cantidad = Integer.parseInt(scanner.nextLine());
                    if (cantidad < 2 || cantidad > 4) {
                        mostrarError("Debe ser entre 2 y 4.");
                    } else {
                        controlador.determinarMaxJugadores(cantidad);
                        mostrarMensaje("Cantidad de jugadores establecida: " + cantidad);
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Ingrese un número válido.");
                }
                break;
            case "2":
                System.out.print("Ingrese puntaje objetivo: ");
                int puntos = Integer.parseInt(scanner.nextLine());
                controlador.establecerPuntajeMaximo(puntos);
                mostrarMensaje("Puntaje máximo establecido: " + puntos);
                break;
            case "3":
                System.out.print("Ingrese nombre del jugador: ");
                String nombre = scanner.nextLine();
                if (!controlador.agregarJugador(nombre)) {
                    mostrarError("No se pudo agregar el jugador.");
                }
                break;
            case "4":
                for (Jugador j : controlador.getJugadores()) {
                    System.out.println("- " + j.getNombre());
                }
                break;
            case "5":
                controlador.iniciarPartida();
                break;
            case "0":
                controlador.cerrarConexion();
                estadoActual = EstadoVista.FIN_PARTIDA;
                break;
            default:
                mostrarError("Opción inválida.");
        }
    }

    private void procesamientoJuego(String opcion) {
        switch (opcion) {
            case "1":
                controlador.ejecutarTurno();
                //mostrar estado de tablero automaticamente
                System.out.println("Tablero:");
                System.out.println(controlador.getTablero());
                break;
            case "2":
                System.out.println("Tablero:");
                System.out.println(controlador.getTablero());
                break;
            case "0":
                controlador.cerrarConexion();
                estadoActual = EstadoVista.FIN_PARTIDA;
                break;
            default:
                mostrarError("Opción inválida.");
        }
    }

    private boolean soyElJugadorActual() {
        Jugador actual = controlador.getJugadorActual();
        System.out.println("[DEBUG] Soy el jugador actual? " + soyElJugadorActual());
        return actual != null && actual.getNombre().equals(nombreJugador);
    }

    @Override
    public void actualizar(Eventos evento) {
        System.out.println("[DEBUG] Evento recibido: " + evento);
        switch (evento) {
            case JUGADOR_AGREGADO:
                this.estadoActual = EstadoVista.ESPERANDO_JUGADORES;
                break;
            case PARTIDA_INICIADA:
                this.estadoActual= EstadoVista.JUGANDO;
                break;
            case JUGADA_INICIAL:
                this.estadoActual = EstadoVista.JUGANDO;
                if (soyElJugadorActual()) {
                    iniciarJuego();
                }
                break;
            case JUGADOR_JUGO_FICHA:
                //VERIFICO SI SOY YO O NO
                this.estadoActual = soyElJugadorActual() ? EstadoVista.JUGANDO : EstadoVista.ESPERANDO_TURNO;
                break;
            case CAMBIO_TURNO:
                this.estadoActual = EstadoVista.JUGANDO;
                break;
            case MANO_TERMINADA:
                this.estadoActual = EstadoVista.JUGANDO;
                mostrarMensaje("La mano terminó. Se reinicia una nueva mano.");
                if (soyElJugadorActual()) {
                    iniciarJuego();
                }
                break;

            case JUEGO_BLOQUEADO:
                this.estadoActual = EstadoVista.JUGANDO;
                mostrarMensaje("El juego se bloqueó (ningún jugador puede jugar). Se reinicia ronda o finaliza.");
                if (soyElJugadorActual()) {
                    iniciarJuego();
                }
                break;
            case PARTIDA_TERMINADA:
                this.estadoActual = EstadoVista.FIN_PARTIDA;
                break;
            default:
                this.estadoActual = EstadoVista.ESTADO_INICIAL;
                break;
        }
    }
    @Override
    public Controlador getControlador() {
        return this.controlador;
    }

    @Override
    public void mostrarGanador(Jugador jugador) {
        System.out.println("\n--- FIN DEL JUEGO ---");
        System.out.println("Ganador: " + jugador.getNombre());
        estadoActual = EstadoVista.FIN_PARTIDA;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println("[MENSAJE] " + mensaje);
    }

    @Override
    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }

    @Override
    public void setControlador(Controlador controlador){
        this.controlador=controlador;
    }

}
