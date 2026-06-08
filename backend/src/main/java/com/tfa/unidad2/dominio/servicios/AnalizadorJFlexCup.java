// Archivo: AnalizadorJFlexCup.java
// Descripcion: Orquesta el lexer generado con JFlex y el parser generado con CUP.
// Responsable: Brandon

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.ErrorAnalisis;
import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import com.tfa.unidad2.dominio.gramatica.Gramatica;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorJFlexCup {

    private static final String PAQUETE_GENERADO = "com.tfa.unidad2.dominio.generado.";

    private final Gramatica gramatica;

    public AnalizadorJFlexCup() {
        this(new Gramatica());
    }

    public AnalizadorJFlexCup(Gramatica gramatica) {
        this.gramatica = gramatica;
    }

    public ResultadoAnalisis analizar(String oracion) {
        String texto = NormalizadorOracion.limpiar(oracion);
        if (texto.isBlank()) {
            return ConstructorResultadoCup.baseInvalida("La oracion no puede estar vacia.", "ORACION_VACIA", 0);
        }

        try {
            List<TokenAnalisis> tokens = tokenizar(texto);
            ResultadoAnalisis errorLexico = validarErroresLexicos(tokens);
            if (errorLexico != null) {
                return errorLexico;
            }

            Object lexer = crearLexer(texto);
            Object parser = crearParser(lexer);
            parser.getClass()
                    .getMethod("configurar", Gramatica.class, List.class)
                    .invoke(parser, gramatica, tokens);

            Object simboloResultado = invocar(parser.getClass().getMethod("parse"), parser);
            ResultadoAnalisis resultado = (ResultadoAnalisis) valorDeSimbolo(simboloResultado);
            resultado.setTokens(tokens);
            return resultado;
        } catch (ErrorSintactico error) {
            return resultadoDesdeErrorSintactico(error, tokenizarSeguro(texto));
        } catch (Exception error) {
            ResultadoAnalisis resultado = ConstructorResultadoCup.baseInvalida(
                    "No se pudo completar el analisis sintactico.",
                    "ERROR_ANALISIS",
                    0
            );
            resultado.setTokens(tokenizarSeguro(texto));
            return resultado;
        }
    }

    private List<TokenAnalisis> tokenizar(String texto) throws Exception {
        Object lexer = crearLexer(texto);
        Method siguienteToken = lexer.getClass().getMethod("next_token");
        List<TokenAnalisis> tokens = new ArrayList<>();
        Object simbolo;
        int finArchivo = valorTokenCup("EOF");

        while (true) {
            simbolo = invocar(siguienteToken, lexer);
            if (numeroDeSimbolo(simbolo) == finArchivo) {
                break;
            }
            if (valorDeSimbolo(simbolo) instanceof TokenAnalisis token) {
                tokens.add(token);
            }
        }

        return List.copyOf(tokens);
    }

    private Object crearLexer(String texto) throws Exception {
        Class<?> claseLexer = Class.forName(PAQUETE_GENERADO + "LexerCup");
        Object lexer = claseLexer.getConstructor(Reader.class).newInstance(new StringReader(texto));
        claseLexer.getMethod("configurar", Gramatica.class).invoke(lexer, gramatica);
        return lexer;
    }

    private Object crearParser(Object lexer) throws Exception {
        Class<?> claseScanner = Class.forName("java_cup.runtime.Scanner");
        Class<?> claseParser = Class.forName(PAQUETE_GENERADO + "ParserCup");
        return claseParser.getConstructor(claseScanner).newInstance(lexer);
    }

    private ResultadoAnalisis validarErroresLexicos(List<TokenAnalisis> tokens) {
        for (TokenAnalisis token : tokens) {
            if ("ERROR".equals(token.token())) {
                String codigo = ConstructorResultadoCup.codigoErrorLexico(token.lexema());
                ResultadoAnalisis resultado = ConstructorResultadoCup.baseInvalida(
                        ConstructorResultadoCup.mensajeErrorLexico(token.lexema(), codigo),
                        codigo,
                        token.posicion()
                );
                resultado.setTokens(tokens);
                resultado.setError(new ErrorAnalisis(codigo, token.posicion(), token.lexema()));
                resultado.setProcesadosHasta(token.posicion());
                return resultado;
            }
        }

        return null;
    }

    private ResultadoAnalisis resultadoDesdeErrorSintactico(ErrorSintactico error, List<TokenAnalisis> tokens) {
        ResultadoAnalisis resultado = ConstructorResultadoCup.baseInvalida(
                error.getMessage(),
                error.getCodigo(),
                error.getPosicion()
        );
        resultado.setTokens(tokens);
        resultado.setError(new ErrorAnalisis(error.getCodigo(), error.getPosicion(), error.getLexema()));
        resultado.setProcesadosHasta(error.getPosicion());
        return resultado;
    }

    private List<TokenAnalisis> tokenizarSeguro(String texto) {
        try {
            return tokenizar(texto);
        } catch (Exception error) {
            return List.of();
        }
    }

    private Object invocar(Method metodo, Object instancia, Object... argumentos) throws Exception {
        try {
            return metodo.invoke(instancia, argumentos);
        } catch (InvocationTargetException error) {
            Throwable causa = error.getCause();
            if (causa instanceof Exception excepcion) {
                throw excepcion;
            }
            if (causa instanceof Error errorReal) {
                throw errorReal;
            }
            throw new IllegalStateException(causa);
        }
    }

    private int valorTokenCup(String nombre) throws Exception {
        return Class.forName(PAQUETE_GENERADO + "TokensCup").getField(nombre).getInt(null);
    }

    private int numeroDeSimbolo(Object simbolo) throws ReflectiveOperationException {
        return simbolo.getClass().getField("sym").getInt(simbolo);
    }

    private Object valorDeSimbolo(Object simbolo) throws ReflectiveOperationException {
        return simbolo.getClass().getField("value").get(simbolo);
    }
}
