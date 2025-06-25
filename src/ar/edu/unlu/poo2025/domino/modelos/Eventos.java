package ar.edu.unlu.poo2025.domino.modelos;
import java.io.Serializable;
public enum Eventos implements Serializable{
    //definir un conjunto de constantes que representan eventos clave que pueden ocurrir en el juego
    //cambios significativos en el estado del juego que la vista o
    //el controlador deben conocer para actualizarse
    JUGADOR_AGREGADO,
    PARTIDA_INICIADA,//el reparto de fichas, inicializacion de mazo y tablero, creador de manejadorTurno
    //seteo turno actual, inicializo jugadorActual(el primero que juega) y  notifica PARTIDA_INICIADA
    JUGADA_INICIAL,//aca se define primer jugador y se agrega la primer ficha al tablero
    //duda: este evento solo representa la primer ficha que se agrega
    JUGADOR_JUGO_FICHA,//indica que el tablero cambio y el jugadorActual tiene una ficha menos
    CAMBIO_TURNO,
    MANO_TERMINADA,
    NUEVA_MANO,
    PARTIDA_TERMINADA,
    JUEGO_BLOQUEADO;

}
