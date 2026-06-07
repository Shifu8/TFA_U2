// Archivo: TokenAnalisis.java
// Descripcion: Representa un lexema de la oracion y su token gramatical.
// Responsable: Justin

package com.tfa.unidad2.dominio.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TokenAnalisis(String lexema, String token, @JsonIgnore int posicion) {
}

