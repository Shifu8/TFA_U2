// Archivo: ExportadorJson.java
// Descripcion: Exporta el resultado del analisis en formato JSON.
// Responsable: Pardo

package com.tfa.unidad2.adaptadores.salida;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.puertos.salida.PuertoResultadoAnalisis;

public class ExportadorJson implements PuertoResultadoAnalisis {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String exportar(ResultadoAnalisis resultado) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultado);
        } catch (JsonProcessingException error) {
            throw new IllegalStateException("No se pudo exportar el resultado a JSON.", error);
        }
    }
}

