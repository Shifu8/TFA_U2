// Archivo: InterpretacionAmbiguedad.java
// Descripcion: Representa una posible lectura estructural de una oracion ambigua.
// Responsable: Santiago

package com.tfa.unidad2.dominio.entidades;

public class InterpretacionAmbiguedad {

    private String titulo;
    private String descripcion;
    private NodoArbol arbol;

    public InterpretacionAmbiguedad(String titulo, String descripcion, NodoArbol arbol) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.arbol = arbol;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NodoArbol getArbol() {
        return arbol;
    }

    public void setArbol(NodoArbol arbol) {
        this.arbol = arbol;
    }
}

