package ar.edu.unlu.poo2025.domino.vistas;

import ar.edu.unlu.poo2025.domino.controladores.Controlador;
import ar.edu.unlu.poo2025.domino.modelos.Eventos;
import ar.edu.unlu.poo2025.domino.modelos.Jugador;

public interface IVista {
    public void iniciar();
    public void iniciarJuego();
    public Controlador getControlador();
    public void setControlador(Controlador controlador);
    public void actualizar(Eventos evento);
    public void mostrarGanador(Jugador jugador);
    public void mostrarMensaje(String mensaje);
    public void mostrarError(String error);
}
