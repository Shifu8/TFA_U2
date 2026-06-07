// Archivo: Gramatica.java
// Descripcion: Define vocabulario, tokens y reglas base del analizador.
// Responsable: Santiago

package com.tfa.unidad2.dominio.gramatica;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gramatica {

    private final Map<String, Set<String>> vocabulario = Map.of(
            "ART", Set.of("el", "la", "los", "las", "un", "una"),
            "PRON", Set.of("yo", "tu", "tú", "él", "ella", "nosotros", "ellos"),
            "SUST", Set.of(
                    "perro", "niña", "nina", "estudiante", "maría", "maria", "juan",
                    "pedro", "profesor", "profesora", "gato", "hombre", "parque",
                    "carta", "libros", "arroz", "matemáticas", "matematicas",
                    "fútbol", "futbol", "pan", "televisión", "television", "clase"
            ),
            "V", Set.of(
                    "corre", "estudia", "come", "lee", "escribe", "duerme",
                    "compra", "mira", "miran", "mordió", "mordio", "explica", "jugamos"
            ),
            "PREP", Set.of("en", "al", "con", "de", "para"),
            "ADV", Set.of("rápido", "rapido", "tranquilo", "lentamente")
    );

    public String normalizarPalabra(String palabra) {
        return Normalizer.normalize(palabra.strip().toLowerCase(), Normalizer.Form.NFC);
    }

    public String clasificar(String palabra) {
        String normalizada = normalizarPalabra(palabra);
        for (Map.Entry<String, Set<String>> entrada : vocabulario.entrySet()) {
            if (entrada.getValue().contains(normalizada)) {
                return entrada.getKey();
            }
        }
        return "ERROR";
    }

    public List<String> obtenerReglas() {
        return ReglasProduccion.REGLAS;
    }
}
