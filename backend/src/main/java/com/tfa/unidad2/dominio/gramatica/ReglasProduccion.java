// Archivo: ReglasProduccion.java
// Descripcion: Lista las producciones de la Gramatica Libre de Contexto.
// Responsable: Santiago

package com.tfa.unidad2.dominio.gramatica;

import java.util.List;

public final class ReglasProduccion {

    public static final List<String> REGLAS = List.of(
            "S -> SN SV",
            "SN -> ART SUST",
            "SN -> PRON",
            "SN -> SUST",
            "SV -> V",
            "SV -> V C",
            "C -> ADV",
            "C -> SN",
            "C -> PREP SN",
            "C -> ADV C",
            "C -> SN C",
            "C -> PREP SN C"
    );

    private ReglasProduccion() {
    }
}
