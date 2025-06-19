package ar.edu.unlu.poo2025.domino.modelos;
import java.io.Serializable;
public enum Eventos implements Serializable{
    //definir un conjunto de constantes que representan eventos clave que pueden ocurrir en el juego
    //cambios significativos en el estado del juego que la vista o
    //el controlador deben conocer para actualizarse
    JUGADOR_AGREGADO,
    PARTIDA_INICIADA,//el reparto de fichas esta aca
    JUGADA_INICIAL,
    //duda: este evento solo representa la primer ficha que se agrega
    JUGADOR_JUGO_FICHA,
    CAMBIO_TURNO,
    MANO_TERMINADA,
    PARTIDA_TERMINADA,
    JUEGO_BLOQUEADO;

}
