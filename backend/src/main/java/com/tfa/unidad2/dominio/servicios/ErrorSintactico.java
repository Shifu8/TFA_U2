// Archivo: ErrorSintactico.java
// Descripcion: Excepcion interna para cortar el parser con mensajes claros.
// Responsable: Brandon

package com.tfa.unidad2.dominio.servicios;

public class ErrorSintactico extends RuntimeException {

    private final int posicion;
    private final String codigo;
    private final String lexema;

    public ErrorSintactico(String mensaje, int posicion, String codigo) {
        this(mensaje, posicion, codigo, null);
    }

    public ErrorSintactico(String mensaje, int posicion, String codigo, String lexema) {
        super(mensaje);
        this.posicion = posicion;
        this.codigo = codigo;
        this.lexema = lexema;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLexema() {
        return lexema;
    }
}
