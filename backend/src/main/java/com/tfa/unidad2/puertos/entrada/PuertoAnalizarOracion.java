// Archivo: PuertoAnalizarOracion.java
// Descripcion: Define el contrato para analizar una oracion desde el exterior.
// Responsable: Brandon

package com.tfa.unidad2.puertos.entrada;

import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;

public interface PuertoAnalizarOracion {

    ResultadoAnalisis analizar(String oracion);
}

