package ar.edu.unlu.poo2025.domino.vistas;

import ar.edu.unlu.poo2025.domino.controladores.Controlador;
import ar.edu.unlu.poo2025.domino.modelos.Eventos;
import ar.edu.unlu.poo2025.domino.modelos.FichaDomino;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public class VistaConsola implements IVista {
    private static final long serialVersionUID = 1L;

    private Controlador controlador;
    private Scanner scanner;
    private String nombreJugador;
    private EstadoVista estadoActual;

    public VistaConsola() {
        this.scanner=new Scanner(System.in);;
        this.estadoActual=EstadoVista.ESTADO_INICIAL;
    }

    public void iniciar(){//setear la configuracion inicial de la vista
        estadoActual = EstadoVista.ESTADO_INICIAL;
        mostrarMensaje("       JUEGO DOMINÓ        ");
        while (estadoActual != EstadoVista.FIN_PARTIDA) {//maneja flujo de juego
            MenuGeneral();
            // Evitás pedir entrada si la lógica no depende del jugador
            if (estadoActual == EstadoVista.ESTADO_INICIAL || estadoActual == EstadoVista.JUGANDO) {
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
                menuConfiguracionInicial();
                break;
            case JUGANDO:
                menuJugadorTurno();
                break;
            case NUEVA_MANO:
                menuNuevaMano();
                break;
            case FIN_PARTIDA:
                resultadoFinal();
                break;
            default:
                mostrarMensaje("Esperando acciones del juego...");
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
        Jugador jugador = this.controlador.getJugadorActual();
        mostrarMensaje("Turno de: " + jugador.getNombre());
        mostrarMensaje("Tus fichas:");
        ArrayList<FichaDomino> fichas_jugador= jugador.getFichas();
        for (int i = 0; i < fichas_jugador.size(); i++) {
            System.out.println(i + ". " + fichas_jugador.get(i));
        }
        mostrarMensaje("Tablero actualizado:");
        System.out.println(this.controlador.getTablero());

        System.out.println("\n Opciones:");
        System.out.println("1. Ver combinaciones posibles"); // llama a verFichasJugables()
        System.out.println("2. Jugar una ficha (si hay combinaciones)");
        System.out.println("3. Sacar del mazo (si no tenés combinaciones)");
        System.out.println("0. Salir del juego");
        System.out.print("Elegí una opción: ");
    }

    private void menuNuevaMano(){//acomodar, no esta siendo usada
        System.out.println("===== NUEVA MANO =====");
        System.out.println("1. Ver Jugadores");
        System.out.println("2. Ver puntajes actual de los jugadores");
        System.out.println("3. Ver tablero");
        System.out.println("4. Continuar con la partida");
    }

    private void resultadoFinal() {
        mostrarMensaje("Fin de la partida.");
        Jugador ganador = controlador.getGanadorPartido();
        if (ganador != null) {
            mostrarGanador(ganador);
        }
    }

    private void Opciones() {
        String opcion = scanner.nextLine();

        switch (estadoActual) {
            case ESTADO_INICIAL:
                procesamientoConfig(opcion);
                break;
            case JUGANDO:
                procesamientoJuego(opcion);
                break;
            case NUEVA_MANO:
                procesamientoNuevaPartida(opcion);
                break;
            //no pongo case FIN_PARTIDA porque nunca va a entrar aca si esta en ese estado
            default:
                break;
        }
    }

    private void procesamientoConfig(String opcion) {
        //tod esto va a estar en ESTADO_INICIAL
        switch (opcion) {
            case "1":
                try {
                    mostrarMensaje("Ingrese cantidad de jugadores (2 a 4): ");
                    int cantidad = Integer.parseInt(scanner.nextLine());
                    if (cantidad < 2 || cantidad > 4) {
                        mostrarError("Debe ser entre 2 y 4.");
                    } else {
                        this.controlador.determinarMaxJugadores(cantidad);
                        mostrarMensaje("Cantidad de jugadores establecida: " + cantidad);
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Ingrese un número válido.");
                }
                break;
            case "2":
                mostrarMensaje("Ingrese puntaje objetivo (entre 20 y 150): ");
                try {
                    int puntos = Integer.parseInt(scanner.nextLine());
                    if (puntos >= 20 && puntos <= 150) {
                        this.controlador.establecerPuntajeMaximo(puntos);
                        mostrarMensaje("Puntaje máximo establecido: " + puntos);
                    } else {
                        mostrarError("Error: El puntaje debe estar entre 20 y 150.");
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Error: Debe ingresar un número entero válido.");
                }
                break;
            case "3":
                System.out.print("Ingrese nombre del jugador: ");
                String nombre = scanner.nextLine();
                if (!this.controlador.agregarJugador(nombre)) {
                    mostrarError("No se pudo agregar el jugador.");
                }
                break;
            case "4":
                for (Jugador j : this.controlador.getJugadores()) {
                    System.out.println("- " + j.getNombre());
                }
                break;
            case "5":
                this.controlador.iniciarPartida();
                //ACA SOLO INICIALIZO MAZO, TABLERO, JUGADORINICIAL, JUGADA INICIAL, TURNO_ACTUAL,
                //actualizo a JUGANDO cuando llama desde el controlador a PARTIDA_INICIADA()
                break;
            case "0":
                mostrarMensaje("Saliendo de la partida...");
                this.controlador.cerrarConexion();
                estadoActual = EstadoVista.FIN_PARTIDA;
                break;
            default:
                mostrarError("Opción inválida.");
        }
    }

    private void procesamientoJuego(String opcion) {
        switch (opcion) {
            case "1":
                // Mostrar combinaciones posibles
                ArrayList<FichaDomino> combinaciones = this.controlador.verFichasJugables();
                if (combinaciones.isEmpty()) {
                    mostrarError(" No hay combinaciones posibles --> Debés sacar del pozo.");
                } else {
                    mostrarMensaje("Tus fichas jugables:");
                    for (int i = 0; i < combinaciones.size(); i++) {
                        System.out.println("[" + i + "] " + combinaciones.get(i));
                    }
                }
                break;
            case "2":
                ArrayList<FichaDomino> jugables = controlador.verFichasJugables();
                if (jugables.isEmpty()) {
                    mostrarError(" No hay jugadas posibles. Usá la opción 3 para sacar del pozo.");
                } else {
                    mostrarMensaje("Elegí el número de la ficha que querés jugar: ");
                    try {
                        int indice = Integer.parseInt(scanner.nextLine());
                        if (indice >= 0 && indice < jugables.size()) {
                            FichaDomino fichaElegida = jugables.get(indice);
                            controlador.ejecutarTurnoInteractivo(fichaElegida);
                        } else {
                            mostrarError("Índice inválido.");
                        }
                    } catch (NumberFormatException e) {
                        mostrarError("Ingresá un número válido.");
                    }
                }
                break;
            case "3":
                this.controlador.sacarHastaTenerJugada(); // El modelo notificará si hay jugadas disponibles luego
                break;
            case "0":
                mostrarMensaje("Saliendo de la partida...");
                this.controlador.cerrarConexion();
                estadoActual = EstadoVista.FIN_PARTIDA;
                break;
            default:
                mostrarError(" Opción inválida. Intentá nuevamente.");
        }
    }

    private void procesamientoNuevaPartida(String opcion){
        switch (opcion){
            case "1":
                for (Jugador j : this.controlador.getJugadores()) {
                    System.out.println("- " + j.getNombre());
                }
                break;
            case "2":
                Map<String, Integer> puntajes = controlador.getPuntajesJugadores();
                System.out.println("PUNTAJES ACTUALES:");
                for (Map.Entry<String, Integer> entry : puntajes.entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " puntos");
                }
                break;
            case "3":
                this.controlador.nuevaMano();
                break;
            default:
                mostrarError("Opción inválida.");
        }
    }


    @Override
    public void actualizar(Eventos evento) {
        System.out.println("[DEBUG] Evento recibido: " + evento);
        switch (evento) {
            case JUGADOR_AGREGADO:
                this.estadoActual = EstadoVista.ESTADO_INICIAL;
                break;
            case PARTIDA_INICIADA:
                this.estadoActual = EstadoVista.JUGANDO;
                break;
            case JUGADA_INICIAL:
                //se coloco ficha inicial
                this.estadoActual = EstadoVista.JUGANDO;
                FichaDomino ficha = this.controlador.getFichaInicial();
                Jugador jugador = this.controlador.getJugadorInicial();
                mostrarMensaje(" La ficha o doble más alto fue " + ficha + " del jugador " + jugador.getNombre());
                mostrarMensaje("Tablero actualizado:");
                System.out.println(this.controlador.getTablero());
                break;
            case JUGADOR_JUGO_FICHA:
                //logica del juego, jugador ejecuta su turno
                //dentro de aca, se cambia el turno
                this.estadoActual = EstadoVista.JUGANDO;
                mostrarMensaje("Un jugador jugó una ficha.");
                break;
            case CAMBIO_TURNO:
                this.estadoActual = EstadoVista.JUGANDO;
                mostrarMensaje("Hubo un cambio de turno");
                break;
            case MOSTRAR_FICHAS_DISPONIBLES:
                this.estadoActual = EstadoVista.JUGANDO;
                ArrayList<FichaDomino> jugables = controlador.verFichasJugables();
                if (jugables.isEmpty()) {
                    mostrarError(" No hay combinaciones disponibles.");
                } else {
                    mostrarMensaje(" Fichas jugables:");
                    for (int i = 0; i < jugables.size(); i++) {
                        System.out.println("[" + i + "] " + jugables.get(i));
                    }
                }
                break;
            case SACA_FICHA_DEL_MAZO:
                mostrarMensaje("🃏 Sacaste una ficha del mazo.");
                break;
            case MANO_TERMINADA:
                this.estadoActual = EstadoVista.NUEVA_MANO;
                mostrarMensaje("TERMINO LA MANO.");

                Jugador ganador = this.controlador.getGanadorMano();
                int puntosGanados = this.controlador.getPuntosMano();

                mostrarGanadorMano(ganador,puntosGanados);

                System.out.println("[[[PUNTAJES TRAS LA MANO:]]]");
                Map<String, Integer> puntajes = controlador.getPuntajesJugadores();
                for (Map.Entry<String, Integer> entry : puntajes.entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " puntos");
                }

                mostrarMensaje("------> Preparando nueva mano...");
                break;
            case NUEVA_MANO:
                this.estadoActual = EstadoVista.JUGANDO;
                mostrarMensaje("Se reinició mano, jugando nuevamente:");
            case JUEGO_BLOQUEADO:
                this.estadoActual = EstadoVista.JUGANDO;
                mostrarMensaje("El juego se bloqueó (ningún jugador puede jugar). Se reinicia ronda o finaliza.");
                break;
            case PARTIDA_TERMINADA:
                this.estadoActual = EstadoVista.FIN_PARTIDA;
                mostrarMensaje("FIN PARTIDA.");
                break;
            default:
                break;
        }
    }
    @Override
    public Controlador getControlador() {
        return this.controlador;
    }

    @Override
    public void mostrarGanador(Jugador jugador) {
        mostrarMensaje("\n--- FIN DEL JUEGO ---");
        mostrarMensaje("Ganador: " + jugador.getNombre());
        estadoActual = EstadoVista.FIN_PARTIDA;
    }
    private void mostrarGanadorMano(Jugador ganador, int puntosGanados) {
        if (ganador != null) {
            mostrarMensaje("****** " + ganador.getNombre() + " ganó la mano y sumó " + puntosGanados + " puntos. ******");
        } else {
            mostrarMensaje("Empate en la mano. No se otorgaron puntos.");
        }
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

    public void setEstadoActual(EstadoVista estadoActual) {
        this.estadoActual = estadoActual;
    }


}
