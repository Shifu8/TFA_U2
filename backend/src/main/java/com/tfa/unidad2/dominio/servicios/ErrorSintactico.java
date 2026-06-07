// Archivo: ErrorSintactico.java
// Descripcion: Excepcion interna para cortar el parser con mensajes claros.
// Responsable: Brandon

package com.tfa.unidad2.dominio.servicios;

public class ErrorSintactico extends RuntimeException {

    private final int posicion;
    private final String codigo;

    public ErrorSintactico(String mensaje, int posicion, String codigo) {
        super(mensaje);
        this.posicion = posicion;
        this.codigo = codigo;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getCodigo() {
        return codigo;
    }
}

