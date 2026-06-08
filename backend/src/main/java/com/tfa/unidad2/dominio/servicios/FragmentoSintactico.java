// Archivo: FragmentoSintactico.java
// Descripcion: Transporta tokens, patron y subarboles creados por las reglas de CUP.
// Responsable: Viviana

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.NodoArbol;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import java.util.List;

public record FragmentoSintactico(NodoArbol nodo, List<TokenAnalisis> tokens, String patron) {
}
