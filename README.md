# 🎲 Juego de Dominó - POO en Java

## 📌 Objetivo

Diseñar un juego de dominó utilizando **programación orientada a objetos (POO)**, que permita partidas de **2 a 4 jugadores**, siguiendo las reglas tradicionales adaptadas con flujo de turnos, puntuación y manejo dinámico de rondas.

---

## 🧱 1. Estructura General

### 🀄 Fichas de Dominó
- Cada ficha posee dos valores enteros de 0 a 6.
- Ejemplo: `FichaDomino(6, 5)`

### 🔁 Pozo
- Conjunto de fichas no repartidas al inicio.
- Puede agotarse a medida que los jugadores roban fichas.

### 🧑 Jugador
- Tiene un **nombre o identificador único**.
- Posee una **mano de fichas**.
- Puede jugar, pasar turno o robar del pozo.

### 🧩 Tablero
- Contiene una lista de fichas jugadas.
- Se colocan fichas en ambos extremos.

### 🧠 Partida
- Controla el **flujo del juego**.
- Maneja los **turnos, cambios de jugador** y **verificación de ganadores**.
- Administra las rondas, puntuación y finalización.

---

## 📏 2. Reglas del Juego

### 📦 Distribución Inicial
- Total de **28 fichas**.
- Reparto: **7 fichas por jugador**.
- El resto se coloca en el **pozo (boca abajo)**.
- Soporta **modo individual o parejas**.

### 🎬 Inicio de Partida
- Se define la cantidad de puntos para ganar (ej. 150).
- Se determina el jugador inicial:
  - Quien tenga el **doble más alto (6-6)**.
  - Si no hay dobles: quien tenga la **ficha de mayor suma**.
- Sentido del juego: **antihorario**.
- La primera ficha se coloca en el tablero.

### 🔄 Turno de Juego
- Colocar una ficha que **coincida con algún extremo** del tablero.
- Si no puede jugar:
  - Roba del pozo (si hay fichas).
  - Si no puede jugar aún → **pasa el turno**.
- Las fichas dobles se colocan **perpendicularmente**.

---

## ♟️ 3. Lógica del Juego

### 🔁 Flujo del Turno
1. Verificar si el jugador tiene fichas → si no tiene, gana la ronda.
2. Si alguien supera los puntos necesarios (ej. 150) → fin del juego.
3. Si no:
   - Verifica si tiene combinaciones válidas.
   - Si puede: juega.
   - Si no puede: roba hasta poder jugar o pasa.
4. Cambia el jugador.

### 🎯 Dominó
- Un jugador **domina la ronda** al colocar su última ficha.
- En **modo individual**, suma los puntos de los demás.
- En **modo parejas**, se suman todas las fichas (incluyendo del compañero).

---

## 🏁 4. Fin de la Ronda

### Condiciones de Victoria
- Gana quien:
  - Se queda sin fichas (“dominó”).
  - O si se **bloquea el juego**, el jugador con menor puntaje.
- En caso de empate:
  - Gana el jugador que esté más cerca de la **mano inicial**.

### 🔁 Siguientes Rondas
- El que inicia puede elegir la ficha que desee.
- Se acumulan los puntos de cada ronda.
- Gana el jugador o pareja que alcance la **puntuación final**.

### 🧮 Puntuación
- Se suman las fichas restantes de los oponentes.
- Se redondea al múltiplo de 5 más cercano.
- Se acumula al puntaje del ganador.

---

## 📌 Diagrama UML

_A continuación se muestra el diagrama UML del sistema:_

![Juego - poo (2)](https://github.com/user-attachments/assets/2fb8d2ee-1069-4e1a-8814-bacf35b06684)


---

## 🚀 Tecnologías
- Lenguaje: **Java**
- Paradigma: **POO**
- Comunicación en red: **Java RMI (opcional para modo en red)**

