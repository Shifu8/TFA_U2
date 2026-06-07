// Archivo: Gramatica.java
// Descripcion: Define vocabulario, tokens y reglas base del analizador.
// Responsable: Santiago

package com.tfa.unidad2.dominio.gramatica;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gramatica {

    private static final String ARCHIVO_LEXEMAS = "lexemas.json";

    private final Map<String, Set<String>> vocabulario;
    private final Map<String, EntradaLexema> lexemas;

    public Gramatica() {
        this(cargarDatos());
    }

    Gramatica(DatosLexemas datos) {
        this.vocabulario = new LinkedHashMap<>();
        this.lexemas = new LinkedHashMap<>();

        datos.tokens().forEach((token, entradas) -> {
            Set<String> palabras = new LinkedHashSet<>();
            for (EntradaLexema entrada : entradas) {
                String lexemaNormalizado = normalizar(entrada.lexema());
                palabras.add(lexemaNormalizado);
                lexemas.put(clave(token, lexemaNormalizado), entrada.normalizada());
            }
            vocabulario.put(token, Collections.unmodifiableSet(palabras));
        });
    }

    public String normalizarPalabra(String palabra) {
        return normalizar(palabra);
    }

    public String clasificar(String palabra) {
        String normalizada = normalizarPalabra(palabra);
        for (Map.Entry<String, Set<String>> entrada : vocabulario.entrySet()) {
            if (entrada.getValue().contains(normalizada)) {
                return entrada.getKey();
            }
        }
        return "ERROR";
    }

    public boolean concuerdanArticuloSustantivo(String articulo, String sustantivo) {
        EntradaLexema datosArticulo = buscar("ART", articulo);
        EntradaLexema datosSustantivo = buscar("SUST", sustantivo);

        if (datosArticulo == null || datosSustantivo == null) {
            return true;
        }

        return sonCompatibles(datosArticulo.genero(), datosSustantivo.genero(), true)
                && sonCompatibles(datosArticulo.numero(), datosSustantivo.numero(), false);
    }

    public List<String> obtenerReglas() {
        return ReglasProduccion.REGLAS;
    }

    private EntradaLexema buscar(String token, String lexema) {
        return lexemas.get(clave(token, normalizar(lexema)));
    }

    private boolean sonCompatibles(String esperado, String encontrado, boolean permiteComun) {
        if (estaVacio(esperado) || estaVacio(encontrado)) {
            return true;
        }
        if (permiteComun && ("c".equals(esperado) || "c".equals(encontrado))) {
            return true;
        }
        return esperado.equals(encontrado);
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private static DatosLexemas cargarDatos() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream entrada = Gramatica.class.getClassLoader().getResourceAsStream(ARCHIVO_LEXEMAS)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontro el archivo " + ARCHIVO_LEXEMAS + ".");
            }
            return mapper.readValue(entrada, DatosLexemas.class);
        } catch (IOException error) {
            throw new IllegalStateException("No se pudo cargar " + ARCHIVO_LEXEMAS + ".", error);
        }
    }

    private static String normalizar(String palabra) {
        if (palabra == null) {
            return "";
        }
        return Normalizer.normalize(palabra.strip().toLowerCase(), Normalizer.Form.NFC);
    }

    private static String clave(String token, String lexema) {
        return token + "::" + lexema;
    }

    public record DatosLexemas(Map<String, List<EntradaLexema>> tokens) {
    }

    public record EntradaLexema(String lexema, String genero, String numero) {

        EntradaLexema normalizada() {
            return new EntradaLexema(normalizar(lexema), normalizar(genero), normalizar(numero));
        }
    }
}
