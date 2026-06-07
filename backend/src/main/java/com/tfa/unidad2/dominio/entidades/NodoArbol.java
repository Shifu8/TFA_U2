// Archivo: NodoArbol.java
// Descripcion: Define los nodos usados para construir el arbol de derivacion.
// Responsable: Viviana

package com.tfa.unidad2.dominio.entidades;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NodoArbol {

    private String nombre;
    private List<NodoArbol> hijos;

    public NodoArbol(String nombre) {
        this(nombre, new ArrayList<>());
    }

    public NodoArbol(String nombre, List<NodoArbol> hijos) {
        this.nombre = nombre;
        this.hijos = hijos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<NodoArbol> getHijos() {
        return hijos;
    }

    public void setHijos(List<NodoArbol> hijos) {
        this.hijos = hijos;
    }
}
