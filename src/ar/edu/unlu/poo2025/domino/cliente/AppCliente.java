package ar.edu.unlu.poo2025.domino.cliente;

import ar.edu.unlu.poo2025.domino.controladores.Controlador;
import ar.edu.unlu.poo2025.domino.vistas.IVista;
import ar.edu.unlu.poo2025.domino.vistas.VistaConsola;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;

public class AppCliente {
    public static void main(String[] args) {
        //Pedir la IP y el puerto del servidor al que se quiere conectar.
        //Instanciar la Vista y el Controlador.
        //Usar la clase Cliente de nuestra librería para conectar el Controlador con el Modelo remoto.
        String ip = "127.0.0.1";
        String ipServidor = "127.0.0.1";
        String portServidor = "8888";
        String port = "9999";


        IVista vista = new VistaConsola();
        Controlador controlador = new Controlador();
        controlador.setVista(vista);
        vista.setControlador(controlador);


        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));

        try {
            c.iniciar(controlador); // primero conecta con el servidor
            System.out.println("Cliente corriendo con éxito en " + "(" + ip + ":" + port + ")");

            vista.iniciar();        // después arrancás la vista (ya con controlador funcional)
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}