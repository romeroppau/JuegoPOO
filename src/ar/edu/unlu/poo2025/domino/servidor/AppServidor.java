package ar.edu.unlu.poo2025.domino.servidor;

import ar.edu.unlu.poo2025.domino.modelos.Partida;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class AppServidor {
    public static void main(String[] args) {
        //Pedir la IP y el puerto para el servidor.
        //Instanciar el modelo (new Chat()).
        //Usar la clase Servidor de nuestra librería para poner el modelo a la escucha.
        // Código para obtener IP y puerto (se puede simplificar para la clase)
        String port = "8888"; // Hardcodeado para simplicidad
        String ip = "127.0.0.1";
        System.out.println("Iniciando servidor en " + ip + ":" + port);

        try {
            Partida modelo = new Partida();
            Servidor servidor = new Servidor(ip, Integer.parseInt(port));
            servidor.iniciar(modelo);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RMIMVCException e) {
            e.printStackTrace();
        }

    }
}
