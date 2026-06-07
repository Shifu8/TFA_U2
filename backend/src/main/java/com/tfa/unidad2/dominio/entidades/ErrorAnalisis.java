// Archivo: ErrorAnalisis.java
// Descripcion: Describe errores lexicos o sintacticos detectados.
// Responsable: Pardo

package com.tfa.unidad2.dominio.entidades;

public class ErrorAnalisis {

    private String codigo;
    private int posicion;
    private String lexema;

    public ErrorAnalisis(String codigo, int posicion) {
        this(codigo, posicion, null);
    }

    public ErrorAnalisis(String codigo, int posicion, String lexema) {
        this.codigo = codigo;
        this.posicion = posicion;
        this.lexema = lexema;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
}

