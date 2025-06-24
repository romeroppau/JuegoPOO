package ar.edu.unlu.poo2025.domino.vistas;

public enum EstadoVista {
    ESTADO_INICIAL, //cant_jugadores (valido), cant_puntos(valido), ingreso jugadores(valido),
    //inicio partida-> INICIALIZO MAZO, TABLERO, JUGADORINICIAL, JUGADA INICIAL, TURNO_ACTUAL
    JUGANDO,
    NUEVA_MANO,
    FIN_PARTIDA
}
