// Archivo: Fragmento.java
// Descripcion: Mantiene un subarbol parseado junto a sus tokens y patron.
// Responsable: Brandon

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.NodoArbol;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import java.util.List;

record Fragmento(NodoArbol nodo, List<TokenAnalisis> tokens, String patron) {
}

