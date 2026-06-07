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
                "La oracion presenta ambiguedad sintactica porque \"en el parque\" "
                        + "puede modificar la accion de morder o al sintagma nominal hombre.",
                List.of(
                        new InterpretacionAmbiguedad(
                                "El perro mordio estando en el parque",
                                "El complemento \"en el parque\" funciona como lugar de la accion.",
                                arbolLugarAccion()
                        ),
                        new InterpretacionAmbiguedad(
                                "El hombre estaba en el parque",
                                "El complemento \"en el parque\" modifica al hombre mordido.",
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
                        new NodoArbol("CC", List.of(
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
                                        new NodoArbol("PP", List.of(
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

