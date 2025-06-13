package ar.edu.unlu.poo2025.domino.cliente;

import ar.edu.unlu.poo2025.domino.controladores.Controlador;
import ar.edu.unlu.poo2025.domino.vistas.IVista;
import ar.edu.unlu.poo2025.domino.vistas.grafica.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;

public class AppCliente {
    public static void main(String[] args) {
        //Pedir la IP y el puerto del servidor al que se quiere conectar.
        //Instanciar la Vista y el Controlador.
        //Usar la clase Cliente de nuestra librería para conectar el Controlador con el Modelo remoto.

        String ip_servidor = "127.0.0.1";
        String puerto_servidor= "8888";

        String ip_cliente = "127.0.0.1";
        int puerto_cliente= 9001;
        //para abrir dos ventanas, tengo que ejecutar uno con un puerto y luego ejecutarlo una ve que cambio el puerto


        Controlador c= new Controlador();
        IVista vista = new VistaGrafica(c);

        // La IP y puerto del cliente son para que el servidor pueda devolverle llamadas (callbacks)
        Cliente cli= new Cliente(ip_cliente, puerto_cliente, ip_servidor,Integer.parseInt(puerto_servidor));
        vista.iniciar();
        try {
            cli.iniciar(c);
        } catch (RMIMVCException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
