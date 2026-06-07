// Archivo: PuertoResultadoAnalisis.java
// Descripcion: Define el contrato para exportar resultados del analisis.
// Responsable: Pardo

package com.tfa.unidad2.puertos.salida;

import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;

public interface PuertoResultadoAnalisis {

    String exportar(ResultadoAnalisis resultado);
}

