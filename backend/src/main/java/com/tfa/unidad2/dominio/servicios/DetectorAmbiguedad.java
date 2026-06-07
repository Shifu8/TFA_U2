// Archivo: DetectorAmbiguedad.java
// Descripcion: Detecta y explica casos de ambiguedad sintactica.
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

        if (lexemas.equals(List.of("el", "perro", "mordió", "al", "hombre", "en", "el", "parque"))
                || lexemas.equals(List.of("el", "perro", "mordio", "al", "hombre", "en", "el", "parque"))) {
            return ambiguedadPerroParque();
        }

        if (lexemas.equals(List.of("juan", "vio", "al", "hombre", "con", "el", "telescopio"))) {
            return ambiguedadTelescopio();
        }

        return null;
    }

    private AmbiguedadSintactica ambiguedadPerroParque() {
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

    private AmbiguedadSintactica ambiguedadTelescopio() {
        return new AmbiguedadSintactica(
                "La oración es ambigua porque la frase \"con el telescopio\" puede indicar el instrumento usado "
                        + "para ver o puede describir al hombre. El sistema muestra ambas estructuras posibles.",
                List.of(
                        new InterpretacionAmbiguedad(
                                "Interpretación 1: Juan usó el telescopio",
                                "Aquí \"con el telescopio\" indica el instrumento con el que Juan realizó la acción de ver.",
                                arbolInstrumentoAccion()
                        ),
                        new InterpretacionAmbiguedad(
                                "Interpretación 2: el hombre tenía el telescopio",
                                "Aquí \"con el telescopio\" describe al hombre observado.",
                                arbolTelescopioDelHombre()
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

    private NodoArbol arbolInstrumentoAccion() {
        return new NodoArbol("S", List.of(
                new NodoArbol("SN", List.of(new NodoArbol("SUST: Juan"))),
                new NodoArbol("SV", List.of(
                        new NodoArbol("V: vio"),
                        new NodoArbol("C", List.of(
                                new NodoArbol("PREP: al"),
                                new NodoArbol("SN", List.of(new NodoArbol("SUST: hombre")))
                        )),
                        new NodoArbol("C: instrumento de la acción", List.of(
                                new NodoArbol("PREP: con"),
                                new NodoArbol("SN", List.of(
                                        new NodoArbol("ART: el"),
                                        new NodoArbol("SUST: telescopio")
                                ))
                        ))
                ))
        ));
    }

    private NodoArbol arbolTelescopioDelHombre() {
        return new NodoArbol("S", List.of(
                new NodoArbol("SN", List.of(new NodoArbol("SUST: Juan"))),
                new NodoArbol("SV", List.of(
                        new NodoArbol("V: vio"),
                        new NodoArbol("C", List.of(
                                new NodoArbol("PREP: al"),
                                new NodoArbol("SN", List.of(
                                        new NodoArbol("SUST: hombre"),
                                        new NodoArbol("PP: instrumento del hombre", List.of(
                                                new NodoArbol("PREP: con"),
                                                new NodoArbol("SN", List.of(
                                                        new NodoArbol("ART: el"),
                                                        new NodoArbol("SUST: telescopio")
                                                ))
                                        ))
                                ))
                        ))
                ))
        ));
    }
}
