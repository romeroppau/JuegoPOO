package ar.edu.unlu.poo2025.domino.modelos;
import java.io.Serializable;
public enum Eventos implements Serializable{
    //definir un conjunto de constantes que representan eventos clave que pueden ocurrir en el juego
    JUGADOR_AGREGADO,
    FICHAS_REPARTIDAS,
    FICHA_JUGADA,
    TURNO_CAMBIADO,
    JUGADOR_PASO,
    FICHA_ROBADA,
    MANO_TERMINADA,
    JUEGO_BLOQUEADO,
    JUGADOR_GANO_MANO,
    JUGADOR_GANO_PARTIDA,
    TABLERO_ACTUALIZADO,
    ESTADO_CARGADO,
    ESTADO_GUARDADO,
    ERROR,
    PARTIDA_INICIADA,
    PARTIDA_FINALIZADA
}
