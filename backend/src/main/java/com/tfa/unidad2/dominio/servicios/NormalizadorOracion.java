// Archivo: NormalizadorOracion.java
// Descripcion: Limpia espacios y signos externos antes del analisis con JFlex y CUP.
// Responsable: Justin

package com.tfa.unidad2.dominio.servicios;

import java.text.Normalizer;

public final class NormalizadorOracion {

    private NormalizadorOracion() {
    }

    public static String limpiar(String oracion) {
        if (oracion == null) {
            return "";
        }

        String texto = Normalizer.normalize(oracion, Normalizer.Form.NFC)
                .strip()
                .replaceAll("\\s+", " ");

        return texto
                .replaceAll("^[\\s.!?\\u00a1\\u00bf]+", "")
                .replaceAll("[\\s.!?\\u00a1\\u00bf]+$", "");
    }
}
