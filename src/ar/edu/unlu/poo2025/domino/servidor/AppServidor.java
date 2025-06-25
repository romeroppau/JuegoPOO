package ar.edu.unlu.poo2025.domino.servidor;

import ar.edu.unlu.poo2025.domino.modelos.*;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ar.edu.unlu.rmimvc.RMIMVCException;

public class AppServidor {
    public static void main(String[] args) {
        //Pedir la IP y el puerto para el servidor.
        //Instanciar el modelo (new Chat()).
        //Usar la clase Servidor de nuestra librería para poner el modelo a la escucha.
        // Código para obtener IP y puerto (se puede simplificar para la clase)

        String ip = "127.0.0.1";
        int puerto = 8888;

        System.out.println("[SERVER] Iniciando servidor...");
        System.out.println("[SERVER] Dirección: " + ip + ", Puerto: " + puerto);
        System.out.println("[SERVER] Instanciando modelo Partida...");
        System.out.println("[SERVER] Servidor listo para aceptar clientes.");


        try {
            // 1. Crear modelo de dominio (Partida)
            Partida partida = new Partida( new Tablero(), new Mazo(), 0, new ArrayList<>());

            // 2. Crear servidor y vincular modelo
            Servidor servidor = new Servidor(ip, puerto);
            servidor.iniciar(partida);

            System.out.println("Servidor iniciado correctamente en " + ip + ":" + puerto);
        } catch (RemoteException e) {
            System.err.println("Error de comunicación remota: " + e.getMessage());
            e.printStackTrace();
        } catch (RMIMVCException e) {
            System.err.println("Error del framework RMIMVC: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
