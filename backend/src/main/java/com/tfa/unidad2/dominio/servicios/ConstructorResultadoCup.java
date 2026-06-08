// Archivo: ConstructorResultadoCup.java
// Descripcion: Construye resultados, derivaciones y errores desde las reducciones de CUP.
// Responsable: Brandon

package com.tfa.unidad2.dominio.servicios;

import com.tfa.unidad2.dominio.entidades.ErrorAnalisis;
import com.tfa.unidad2.dominio.entidades.NodoArbol;
import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import com.tfa.unidad2.dominio.gramatica.Gramatica;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ConstructorResultadoCup {

    private ConstructorResultadoCup() {
    }

    public static FragmentoSintactico snArticuloSustantivo(
            TokenAnalisis articulo,
            TokenAnalisis sustantivo,
            Gramatica gramatica
    ) {
        if (!gramatica.concuerdanArticuloSustantivo(articulo.lexema(), sustantivo.lexema())) {
            throw new ErrorSintactico(
                    "Concordancia invalida: el articulo \"" + articulo.lexema()
                            + "\" no concuerda con el sustantivo \"" + sustantivo.lexema() + "\".",
                    articulo.posicion(),
                    "CONCORDANCIA_INVALIDA",
                    articulo.lexema() + " " + sustantivo.lexema()
            );
        }

        return new FragmentoSintactico(
                new NodoArbol("SN", List.of(hoja(articulo), hoja(sustantivo))),
                List.of(articulo, sustantivo),
                "ART SUST"
        );
    }

    public static FragmentoSintactico snSimple(TokenAnalisis token) {
        return new FragmentoSintactico(new NodoArbol("SN", List.of(hoja(token))), List.of(token), token.token());
    }

    public static SintagmaVerbalCup svSoloVerbo(TokenAnalisis verbo) {
        return new SintagmaVerbalCup(verbo, null, new NodoArbol("SV", List.of(hoja(verbo))), List.of(verbo));
    }

    public static SintagmaVerbalCup svConComplemento(TokenAnalisis verbo, FragmentoSintactico complemento) {
        List<TokenAnalisis> tokens = new ArrayList<>();
        tokens.add(verbo);
        tokens.addAll(complemento.tokens());

        return new SintagmaVerbalCup(
                verbo,
                complemento,
                new NodoArbol("SV", List.of(hoja(verbo), complemento.nodo())),
                List.copyOf(tokens)
        );
    }

    public static FragmentoSintactico unidadAdverbio(TokenAnalisis adverbio) {
        return new FragmentoSintactico(hoja(adverbio), List.of(adverbio), "ADV");
    }

    public static FragmentoSintactico unidadPreposicional(TokenAnalisis preposicion, FragmentoSintactico sn) {
        List<TokenAnalisis> tokens = new ArrayList<>();
        tokens.add(preposicion);
        tokens.addAll(sn.tokens());

        return new FragmentoSintactico(
                new NodoArbol("PP", List.of(hoja(preposicion), sn.nodo())),
                List.copyOf(tokens),
                "PREP " + sn.patron()
        );
    }

    public static FragmentoSintactico complementoDeUnidad(FragmentoSintactico unidad) {
        return new FragmentoSintactico(new NodoArbol("C", List.of(unidad.nodo())), unidad.tokens(), unidad.patron());
    }

    public static FragmentoSintactico complementoSecuencia(
            FragmentoSintactico unidad,
            FragmentoSintactico complemento
    ) {
        List<NodoArbol> hijos = new ArrayList<>();
        hijos.add(unidad.nodo());
        hijos.addAll(complemento.nodo().getHijos());

        List<TokenAnalisis> tokens = new ArrayList<>();
        tokens.addAll(unidad.tokens());
        tokens.addAll(complemento.tokens());

        return new FragmentoSintactico(
                new NodoArbol("C", List.copyOf(hijos)),
                List.copyOf(tokens),
                unidad.patron() + " " + complemento.patron()
        );
    }

    public static ResultadoAnalisis resultadoValido(
            FragmentoSintactico sujeto,
            SintagmaVerbalCup sintagmaVerbal,
            List<TokenAnalisis> tokens
    ) {
        ResultadoAnalisis resultado = new ResultadoAnalisis();
        resultado.setValida(true);
        resultado.setMensaje("Oracion valida");
        resultado.setSujeto(unirLexemas(sujeto.tokens()));
        resultado.setVerbo(sintagmaVerbal.verbo().lexema());
        resultado.setComplemento(sintagmaVerbal.complemento() == null
                ? ""
                : unirLexemas(sintagmaVerbal.complemento().tokens()));
        resultado.setTokens(tokens);
        resultado.setDerivacion(generarDerivacion(
                sujeto.patron(),
                sintagmaVerbal.complemento() == null ? "" : sintagmaVerbal.complemento().patron()
        ));
        resultado.setArbol(new NodoArbol("S", List.of(sujeto.nodo(), sintagmaVerbal.nodo())));
        resultado.setProcesadosHasta(tokens.size());
        return resultado;
    }

    public static ResultadoAnalisis errorLexicoComoSintactico(TokenAnalisis token) {
        String codigo = codigoErrorLexico(token.lexema());
        throw new ErrorSintactico(mensajeErrorLexico(token.lexema(), codigo), token.posicion(), codigo, token.lexema());
    }

    public static ErrorSintactico errorSintactico(List<TokenAnalisis> tokens, Object simbolo) {
        int posicion = posicionActual(tokens, simbolo);

        if (tokens.isEmpty()) {
            return new ErrorSintactico("La oracion no puede estar vacia.", 0, "ORACION_VACIA");
        }

        TokenAnalisis primero = tokens.get(0);
        if ("V".equals(primero.token())) {
            return new ErrorSintactico("Falta el sujeto antes del verbo.", 0, "FALTA_SUJETO", primero.lexema());
        }

        if ("ART".equals(primero.token()) && (tokens.size() < 2 || !"SUST".equals(tokens.get(1).token()))) {
            return new ErrorSintactico(
                    "Falta un sustantivo despues del articulo del sujeto.",
                    Math.min(1, tokens.size()),
                    "FALTA_SUSTANTIVO",
                    primero.lexema()
            );
        }

        int finSujeto = finSintagmaNominal(tokens, 0);
        if (finSujeto < 0) {
            return new ErrorSintactico(
                    "No se encontro un sintagma nominal valido para el sujeto.",
                    posicion,
                    "SN_INVALIDO",
                    lexemaEn(tokens, posicion)
            );
        }

        if (finSujeto >= tokens.size()) {
            return new ErrorSintactico("Falta el verbo despues del sujeto.", posicion, "FALTA_VERBO");
        }

        if (!"V".equals(tokens.get(finSujeto).token())) {
            return new ErrorSintactico(
                    "Orden incorrecto: despues del sujeto debe ir un verbo.",
                    finSujeto,
                    "ORDEN_INCORRECTO",
                    tokens.get(finSujeto).lexema()
            );
        }

        return new ErrorSintactico(
                "Falta un complemento valido despues del verbo.",
                posicion,
                "COMPLEMENTO_INVALIDO",
                lexemaEn(tokens, posicion)
        );
    }

    public static String codigoErrorLexico(String lexema) {
        if (lexema.matches(".*\\d.*")) {
            return "NUMERO_NO_PERMITIDO";
        }
        if (!lexema.matches("\\p{L}+")) {
            return "SIGNO_NO_PERMITIDO";
        }
        return "PALABRA_NO_RECONOCIDA";
    }

    public static String mensajeErrorLexico(String lexema, String codigo) {
        if ("NUMERO_NO_PERMITIDO".equals(codigo)) {
            return "No se permiten numeros en la oracion: \"" + lexema + "\".";
        }
        if ("SIGNO_NO_PERMITIDO".equals(codigo)) {
            return "Solo se permite punto, signo de pregunta o exclamacion al final. Revisa: \""
                    + lexema + "\".";
        }
        return "Palabra no reconocida: \"" + lexema + "\".";
    }

    public static ResultadoAnalisis baseInvalida(String mensaje, String codigo, int posicionError) {
        ResultadoAnalisis resultado = new ResultadoAnalisis();
        resultado.setValida(false);
        resultado.setMensaje(mensaje);
        resultado.setError(new ErrorAnalisis(codigo, posicionError));
        resultado.setProcesadosHasta(posicionError);
        return resultado;
    }

    private static List<String> generarDerivacion(String patronSujeto, String patronComplemento) {
        Set<String> pasos = new LinkedHashSet<>();
        pasos.add("S");
        pasos.add("SN SV");
        pasos.add(patronSujeto + " SV");

        if (patronComplemento == null || patronComplemento.isBlank()) {
            pasos.add(patronSujeto + " V");
            return new ArrayList<>(pasos);
        }

        pasos.add(patronSujeto + " V C");
        pasos.addAll(generarDerivacionComplemento(patronSujeto + " V C", patronComplemento));
        return new ArrayList<>(pasos);
    }

    private static List<String> generarDerivacionComplemento(String pasoConComplemento, String patronComplemento) {
        List<String> pasos = new ArrayList<>();
        List<String> componentes = separarComponentesComplemento(patronComplemento);
        String pasoActual = pasoConComplemento;

        for (int indice = 0; indice < componentes.size(); indice++) {
            String componente = componentes.get(indice);
            boolean ultimo = indice == componentes.size() - 1;
            String produccion = produccionComponente(componente);
            String reemplazo = ultimo ? produccion : produccion + " C";

            pasoActual = reemplazarPrimerNoTerminal(pasoActual, "C", reemplazo);
            pasos.add(pasoActual);

            String patronSn = patronSnDeComponente(componente);
            if (!patronSn.isBlank()) {
                pasoActual = reemplazarPrimerNoTerminal(pasoActual, "SN", patronSn);
                pasos.add(pasoActual);
            }
        }

        return pasos;
    }

    private static List<String> separarComponentesComplemento(String patronComplemento) {
        List<String> tokensPatron = List.of(patronComplemento.split(" "));
        List<String> componentes = new ArrayList<>();
        int indice = 0;

        while (indice < tokensPatron.size()) {
            String token = tokensPatron.get(indice);

            if ("ADV".equals(token)) {
                componentes.add("ADV");
                indice++;
                continue;
            }

            if ("PREP".equals(token)) {
                int inicio = indice;
                indice++;
                int cantidadSn = contarTokensSn(tokensPatron, indice);
                indice += cantidadSn;
                componentes.add(String.join(" ", tokensPatron.subList(inicio, indice)));
                continue;
            }

            int cantidadSn = contarTokensSn(tokensPatron, indice);
            componentes.add(String.join(" ", tokensPatron.subList(indice, indice + cantidadSn)));
            indice += cantidadSn;
        }

        return componentes;
    }

    private static String produccionComponente(String componente) {
        if ("ADV".equals(componente)) {
            return "ADV";
        }
        if (componente.startsWith("PREP ")) {
            return "PREP SN";
        }
        return "SN";
    }

    private static String patronSnDeComponente(String componente) {
        if ("ADV".equals(componente)) {
            return "";
        }

        List<String> tokensComponente = List.of(componente.split(" "));
        if ("PREP".equals(tokensComponente.get(0))) {
            return String.join(" ", tokensComponente.subList(1, tokensComponente.size()));
        }
        return componente;
    }

    private static String reemplazarPrimerNoTerminal(String texto, String noTerminal, String reemplazo) {
        return texto.replaceFirst("\\b" + noTerminal + "\\b", reemplazo);
    }

    private static int contarTokensSn(List<String> tokensPatron, int indice) {
        if (indice < tokensPatron.size() && "ART".equals(tokensPatron.get(indice))) {
            return 2;
        }
        return 1;
    }

    private static int posicionActual(List<TokenAnalisis> tokens, Object simbolo) {
        if (valorDeSimbolo(simbolo) instanceof TokenAnalisis token) {
            return token.posicion();
        }
        return tokens.size();
    }

    private static Object valorDeSimbolo(Object simbolo) {
        if (simbolo == null) {
            return null;
        }

        try {
            return simbolo.getClass().getField("value").get(simbolo);
        } catch (ReflectiveOperationException error) {
            return null;
        }
    }

    private static int finSintagmaNominal(List<TokenAnalisis> tokens, int indice) {
        if (indice >= tokens.size()) {
            return -1;
        }

        String token = tokens.get(indice).token();
        if ("ART".equals(token)) {
            return indice + 1 < tokens.size() && "SUST".equals(tokens.get(indice + 1).token()) ? indice + 2 : -1;
        }
        if ("PRON".equals(token) || "SUST".equals(token)) {
            return indice + 1;
        }
        return -1;
    }

    private static String lexemaEn(List<TokenAnalisis> tokens, int posicion) {
        return posicion >= 0 && posicion < tokens.size() ? tokens.get(posicion).lexema() : null;
    }

    private static NodoArbol hoja(TokenAnalisis token) {
        return new NodoArbol(token.token() + ": " + token.lexema());
    }

    private static String unirLexemas(List<TokenAnalisis> tokens) {
        return tokens.stream().map(TokenAnalisis::lexema).reduce((a, b) -> a + " " + b).orElse("");
    }
}
