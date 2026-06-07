// Archivo: Parser.java
// Descripcion: Implementa el analisis sintactico segun la GLC definida.
// Responsable: Brandon

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.ErrorAnalisis;
import com.tfa.unidad2.dominio.entidades.NodoArbol;
import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Parser {

    private static final Set<String> INICIOS_SN = Set.of("ART", "PRON", "SUST");
    private List<TokenAnalisis> tokens = List.of();
    private int posicion;

    public ResultadoAnalisis analizar(List<TokenAnalisis> tokens) {
        this.tokens = tokens;
        this.posicion = 0;

        if (tokens.isEmpty()) {
            ResultadoAnalisis resultado = baseInvalida("La oracion no puede estar vacia.", "ORACION_VACIA", 0);
            resultado.setTokens(tokens);
            return resultado;
        }

        for (TokenAnalisis token : tokens) {
            if ("ERROR".equals(token.token())) {
                String codigo = codigoErrorLexico(token.lexema());
                ResultadoAnalisis resultado = baseInvalida(
                        mensajeErrorLexico(token.lexema(), codigo),
                        codigo,
                        token.posicion()
                );
                resultado.setTokens(tokens);
                resultado.setError(new ErrorAnalisis(codigo, token.posicion(), token.lexema()));
                resultado.setProcesadosHasta(token.posicion());
                return resultado;
            }
        }

        try {
            Fragmento sujeto = parsearSn("sujeto");

            if (fin()) {
                throw new ErrorSintactico("Falta el verbo despues del sujeto.", posicion, "FALTA_VERBO");
            }

            if (!"V".equals(actual().token())) {
                throw new ErrorSintactico(
                        "Orden incorrecto: despues del sujeto debe ir un verbo.",
                        posicion,
                        "ORDEN_INCORRECTO"
                );
            }

            TokenAnalisis verbo = consumir("V");

            if (fin()) {
                throw new ErrorSintactico("Falta el complemento despues del verbo.", posicion, "FALTA_COMPLEMENTO");
            }

            Fragmento complemento = parsearComplemento();

            if (!fin()) {
                throw new ErrorSintactico(
                        "Token inesperado \"" + actual().lexema() + "\" despues del complemento.",
                        posicion,
                        "TOKEN_INESPERADO"
                );
            }

            NodoArbol arbol = new NodoArbol("S", List.of(
                    sujeto.nodo(),
                    new NodoArbol("SV", List.of(
                            hoja(verbo),
                            complemento.nodo()
                    ))
            ));

            ResultadoAnalisis resultado = new ResultadoAnalisis();
            resultado.setValida(true);
            resultado.setMensaje("Oración válida");
            resultado.setSujeto(unirLexemas(sujeto.tokens()));
            resultado.setVerbo(verbo.lexema());
            resultado.setComplemento(unirLexemas(complemento.tokens()));
            resultado.setTokens(tokens);
            resultado.setDerivacion(generarDerivacion(sujeto.patron(), complemento.patron()));
            resultado.setArbol(arbol);
            resultado.setProcesadosHasta(tokens.size());
            return resultado;
        } catch (ErrorSintactico error) {
            ResultadoAnalisis resultado = baseInvalida(error.getMessage(), error.getCodigo(), error.getPosicion());
            resultado.setTokens(tokens);
            resultado.setProcesadosHasta(error.getPosicion());
            return resultado;
        }
    }

    private Fragmento parsearSn(String contexto) {
        if (fin()) {
            throw new ErrorSintactico(
                    "Falta un sintagma nominal para el " + contexto + ".",
                    posicion,
                    "FALTA_SN"
            );
        }

        TokenAnalisis token = actual();
        if ("ART".equals(token.token())) {
            TokenAnalisis articulo = consumir("ART");
            if (fin() || !"SUST".equals(actual().token())) {
                throw new ErrorSintactico(
                        "Falta un sustantivo despues del articulo del " + contexto + ".",
                        posicion,
                        "FALTA_SUSTANTIVO"
                );
            }
            TokenAnalisis sustantivo = consumir("SUST");
            return new Fragmento(
                    new NodoArbol("SN", List.of(hoja(articulo), hoja(sustantivo))),
                    List.of(articulo, sustantivo),
                    "ART SUST"
            );
        }

        if ("PRON".equals(token.token())) {
            TokenAnalisis pronombre = consumir("PRON");
            return new Fragmento(new NodoArbol("SN", List.of(hoja(pronombre))), List.of(pronombre), "PRON");
        }

        if ("SUST".equals(token.token())) {
            TokenAnalisis sustantivo = consumir("SUST");
            return new Fragmento(new NodoArbol("SN", List.of(hoja(sustantivo))), List.of(sustantivo), "SUST");
        }

        String codigo = "sujeto".equals(contexto) && "V".equals(token.token()) ? "FALTA_SUJETO" : "SN_INVALIDO";
        String mensaje = "FALTA_SUJETO".equals(codigo)
                ? "Falta el sujeto antes del verbo."
                : "No se encontro un sintagma nominal valido para el " + contexto + ".";
        throw new ErrorSintactico(mensaje, posicion, codigo);
    }

    private Fragmento parsearComplemento() {
        if (fin()) {
            throw new ErrorSintactico("Falta el complemento despues del verbo.", posicion, "FALTA_COMPLEMENTO");
        }

        TokenAnalisis token = actual();
        if ("ADV".equals(token.token())) {
            TokenAnalisis adverbio = consumir("ADV");
            return new Fragmento(new NodoArbol("C", List.of(hoja(adverbio))), List.of(adverbio), "ADV");
        }

        if ("PREP".equals(token.token())) {
            TokenAnalisis primeraPrep = consumir("PREP");
            Fragmento primerSn = parsearSn("complemento");
            List<TokenAnalisis> tokensFragmento = new ArrayList<>();
            tokensFragmento.add(primeraPrep);
            tokensFragmento.addAll(primerSn.tokens());

            List<NodoArbol> hijos = new ArrayList<>();
            hijos.add(hoja(primeraPrep));
            hijos.add(primerSn.nodo());
            String patron = "PREP " + primerSn.patron();

            if (!fin() && "PREP".equals(actual().token())) {
                TokenAnalisis segundaPrep = consumir("PREP");
                Fragmento segundoSn = parsearSn("complemento preposicional");
                tokensFragmento.add(segundaPrep);
                tokensFragmento.addAll(segundoSn.tokens());
                hijos.add(new NodoArbol("PP", List.of(hoja(segundaPrep), segundoSn.nodo())));
                patron = patron + " PREP " + segundoSn.patron();
            }

            return new Fragmento(new NodoArbol("C", hijos), tokensFragmento, patron);
        }

        if (INICIOS_SN.contains(token.token())) {
            Fragmento sn = parsearSn("complemento");
            List<TokenAnalisis> tokensFragmento = new ArrayList<>(sn.tokens());
            List<NodoArbol> hijos = new ArrayList<>();
            hijos.add(sn.nodo());
            String patron = sn.patron();

            if (!fin() && "PREP".equals(actual().token())) {
                TokenAnalisis prep = consumir("PREP");
                Fragmento segundoSn = parsearSn("complemento preposicional");
                tokensFragmento.add(prep);
                tokensFragmento.addAll(segundoSn.tokens());
                hijos.add(hoja(prep));
                hijos.add(segundoSn.nodo());
                patron = patron + " PREP " + segundoSn.patron();
            }

            return new Fragmento(new NodoArbol("C", hijos), tokensFragmento, patron);
        }

        throw new ErrorSintactico(
                "Falta un complemento valido despues del verbo.",
                posicion,
                "COMPLEMENTO_INVALIDO"
        );
    }

    private List<String> generarDerivacion(String patronSujeto, String patronComplemento) {
        Set<String> pasos = new LinkedHashSet<>();
        pasos.add("S");
        pasos.add("SN SV");
        pasos.add(patronSujeto + " SV");
        pasos.add(patronSujeto + " V C");
        pasos.add(patronSujeto + " V " + patronComplemento);
        return new ArrayList<>(pasos);
    }

    private String codigoErrorLexico(String lexema) {
        if (lexema.matches(".*\\d.*")) {
            return "NUMERO_NO_PERMITIDO";
        }
        if (!lexema.matches("\\p{L}+")) {
            return "SIGNO_NO_PERMITIDO";
        }
        return "PALABRA_NO_RECONOCIDA";
    }

    private String mensajeErrorLexico(String lexema, String codigo) {
        if ("NUMERO_NO_PERMITIDO".equals(codigo)) {
            return "No se permiten números en la oración: \"" + lexema + "\".";
        }
        if ("SIGNO_NO_PERMITIDO".equals(codigo)) {
            return "Solo se permite punto, signo de pregunta o exclamación al final. Revisa: \"" + lexema + "\".";
        }
        return "Palabra no reconocida: \"" + lexema + "\".";
    }

    private ResultadoAnalisis baseInvalida(String mensaje, String codigo, int posicionError) {
        ResultadoAnalisis resultado = new ResultadoAnalisis();
        resultado.setValida(false);
        resultado.setMensaje(mensaje);
        resultado.setError(new ErrorAnalisis(codigo, posicionError));
        resultado.setProcesadosHasta(posicionError);
        return resultado;
    }

    private NodoArbol hoja(TokenAnalisis token) {
        return new NodoArbol(token.token() + ": " + token.lexema());
    }

    private TokenAnalisis actual() {
        return tokens.get(posicion);
    }

    private boolean fin() {
        return posicion >= tokens.size();
    }

    private TokenAnalisis consumir(String tokenEsperado) {
        if (fin() || !tokenEsperado.equals(actual().token())) {
            String encontrado = fin() ? "fin de oracion" : actual().token();
            throw new ErrorSintactico(
                    "Se esperaba " + tokenEsperado + ", pero se encontro " + encontrado + ".",
                    posicion,
                    "TOKEN_NO_ESPERADO"
            );
        }

        TokenAnalisis token = actual();
        posicion++;
        return token;
    }

    private String unirLexemas(List<TokenAnalisis> tokens) {
        return tokens.stream().map(TokenAnalisis::lexema).reduce((a, b) -> a + " " + b).orElse("");
    }
}
