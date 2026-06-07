// Archivo: Lexer.java
// Descripcion: Implementa el analisis lexico y genera la tabla token/lexema.
// Responsable: Justin

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import com.tfa.unidad2.dominio.gramatica.Gramatica;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final Gramatica gramatica;

    public Lexer() {
        this(new Gramatica());
    }

    public Lexer(Gramatica gramatica) {
        this.gramatica = gramatica;
    }

    public String limpiarOracion(String oracion) {
        if (oracion == null) {
            return "";
        }

        String texto = Normalizer.normalize(oracion, Normalizer.Form.NFC)
                .strip()
                .replaceAll("\\s+", " ");
        return texto
                .replaceAll("^[\\s.!?¡¿]+", "")
                .replaceAll("[\\s.!?¡¿]+$", "");
    }

    public List<TokenAnalisis> tokenizar(String oracion) {
        String texto = limpiarOracion(oracion);
        List<TokenAnalisis> tokens = new ArrayList<>();

        if (texto.isBlank()) {
            return tokens;
        }

        String[] palabras = texto.split(" ");
        for (int posicion = 0; posicion < palabras.length; posicion++) {
            String lexema = palabras[posicion].replaceAll("^[,;:!?¡¿\"'()\\[\\]{}]+|[,;:!?¡¿\"'()\\[\\]{}]+$", "");
            tokens.add(new TokenAnalisis(lexema, gramatica.clasificar(lexema), posicion));
        }

        return tokens;
    }
}

