// Archivo: DetectorAmbiguedad.java
// Descripcion: Detecta y explica el caso obligatorio de ambiguedad sintactica.
// Responsable: Santiago

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.AmbiguedadSintactica;
import com.tfa.unidad2.dominio.entidades.InterpretacionAmbiguedad;
import com.tfa.unidad2.dominio.entidades.NodoArbol;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import java.util.List;

public class DetectorAmbiguedad {

    public AmbiguedadSintactica detectar(List<TokenAnalisis> tokens) {
        List<String> lexemas = tokens.stream().map(token -> token.lexema().toLowerCase()).toList();
        if (!lexemas.equals(List.of("el", "perro", "mordió", "al", "hombre", "en", "el", "parque"))) {
            return null;
        }

        return new AmbiguedadSintactica(
                "La oración es ambigua porque la frase \"en el parque\" puede pegarse a dos partes distintas: "
                        + "a la acción de morder o al hombre mordido. El sistema no decide el significado; "
                        + "solo muestra las dos estructuras posibles.",
                List.of(
                        new InterpretacionAmbiguedad(
                                "Interpretación 1: la mordida ocurrió en el parque",
                                "Aquí \"en el parque\" indica el lugar donde el perro realizó la acción de morder.",
                                arbolLugarAccion()
                        ),
                        new InterpretacionAmbiguedad(
                                "Interpretación 2: el hombre estaba en el parque",
                                "Aquí \"en el parque\" describe al hombre: el hombre mordido era el que estaba en ese lugar.",
                                arbolModificaHombre()
                        )
                )
        );
    }

    private NodoArbol arbolLugarAccion() {
        return new NodoArbol("S", List.of(
                new NodoArbol("SN", List.of(new NodoArbol("ART: El"), new NodoArbol("SUST: perro"))),
                new NodoArbol("SV", List.of(
                        new NodoArbol("V: mordió"),
                        new NodoArbol("C", List.of(
                                new NodoArbol("PREP: al"),
                                new NodoArbol("SN", List.of(new NodoArbol("SUST: hombre")))
                        )),
                        new NodoArbol("C: lugar de la acción", List.of(
                                new NodoArbol("PREP: en"),
                                new NodoArbol("SN", List.of(
                                        new NodoArbol("ART: el"),
                                        new NodoArbol("SUST: parque")
                                ))
                        ))
                ))
        ));
    }

    private NodoArbol arbolModificaHombre() {
        return new NodoArbol("S", List.of(
                new NodoArbol("SN", List.of(new NodoArbol("ART: El"), new NodoArbol("SUST: perro"))),
                new NodoArbol("SV", List.of(
                        new NodoArbol("V: mordió"),
                        new NodoArbol("C", List.of(
                                new NodoArbol("PREP: al"),
                                new NodoArbol("SN", List.of(
                                        new NodoArbol("SUST: hombre"),
                                        new NodoArbol("PP: lugar del hombre", List.of(
                                                new NodoArbol("PREP: en"),
                                                new NodoArbol("SN", List.of(
                                                        new NodoArbol("ART: el"),
                                                        new NodoArbol("SUST: parque")
                                                ))
                                        ))
                                ))
                        ))
                ))
        ));
    }
}
