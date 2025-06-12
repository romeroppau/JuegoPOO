package ar.edu.unlu.poo2025.domino.modelos;

public interface IModelo {
    //metodos que el controlador necesia para actualizarse
    //Métodos que gestionan la lógica de la interacción, como la asignación de fichas, el avance de turno, y el control de jugadas.
    //logica entre jugadores-vista / eventos y acciones del jugador
    boolean agregarJugador(String nombre);
    public boolean iniciarPartida();
    public Jugador hayGanadorPartido();
    public int recuentoPuntos();
}
