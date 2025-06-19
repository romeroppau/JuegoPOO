package ar.edu.unlu.poo2025.domino;

import ar.edu.unlu.poo2025.domino.modelos.Jugador;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainConsola {
    public static void main(String[] args) throws RemoteException {
        /*
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- JUEGO DE DOMINÓ (consola) ----");

        // 1. Crear jugadores
        ArrayList<Jugador> jugadores = new ArrayList<>();
        int cantidad;
        do {
            System.out.print("Ingrese cantidad de jugadores (2 a 4): ");
            cantidad = Integer.parseInt(scanner.nextLine());
        } while (cantidad < 2 || cantidad > 4);

        for (int i = 0; i < cantidad; i++) {
            System.out.print("Nombre del jugador " + (i + 1) + ": ");
            String nombre = scanner.nextLine();
            jugadores.add(new Jugador(nombre));
        }

        // 2. Crear tablero, mazo, partida
        Tablero tablero = new Tablero();
        Mazo mazo = new Mazo();
        int puntajeMax = 50;
        Partida partida = new Partida(null, tablero, mazo, puntajeMax, jugadores);
        partida.determinarMaxJugadores(cantidad);
        partida.iniciarPartida();

        // 3. Bucle principal del juego
        while (!partida.getpartidaTerminada()) {
            Jugador actual = partida.getJugadorActual();
            System.out.println("\nTurno de: " + actual.getNombre());
            System.out.println("Fichas disponibles de:" + actual.getNombre());
            for (int i = 0; i < actual.getFichas().size(); i++) {
                System.out.println(i + ": " + actual.getFichas().get(i));
            }
            System.out.println("Extremos del tablero actuales: " + tablero.getExtremoActualIzq() + " y " + tablero.getextremoActualDer());

            // El jugador juega automáticamente con su metodo puedeJugar()
            boolean jugo = actual.puedeJugar(tablero, mazo);
            if (jugo) {
                System.out.println(actual.getNombre() + " jugó una ficha.");
            } else {
                System.out.println(actual.getNombre() + " no pudo jugar.");
            }
            partida.ejecutarTurno(tablero, mazo);
        }

        System.out.println("\n--- PARTIDA TERMINADA ---");
        System.out.println("Ganador: " + partida.getGanadorPartido().getNombre());
        System.out.println("\nPuntajes finales:");
        for (Jugador j : partida.getJugadores()) {
            System.out.println(j.getNombre() + ": " + j.getPuntaje() + " puntos");
        }*/
    }
}
