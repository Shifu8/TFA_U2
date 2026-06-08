// Archivo: SintagmaVerbalCup.java
// Descripcion: Agrupa el verbo y complemento que reduce el parser generado por CUP.
// Responsable: Santiago

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.NodoArbol;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import java.util.List;

public record SintagmaVerbalCup(
        TokenAnalisis verbo,
        FragmentoSintactico complemento,
        NodoArbol nodo,
        List<TokenAnalisis> tokens
) {
}
