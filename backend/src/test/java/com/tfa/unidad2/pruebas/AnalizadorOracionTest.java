// Archivo: AnalizadorOracionTest.java
// Descripcion: Prueba oraciones validas, invalidas, arbol y ambiguedad.
// Responsable: Pardo

package com.tfa.unidad2.pruebas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tfa.unidad2.aplicacion.casosuso.AnalizarOracionUseCase;
import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;

class AnalizadorOracionTest {

    private AnalizarOracionUseCase casoUso;

    @BeforeEach
    void preparar() {
        casoUso = new AnalizarOracionUseCase();
    }

    @ParameterizedTest
    @org.junit.jupiter.params.provider.MethodSource("oracionesValidas")
    void analizaOracionesValidas(String oracion, String sujeto, String verbo, String complemento) {
        ResultadoAnalisis resultado = casoUso.analizar(oracion);

        assertTrue(resultado.isValida());
        assertEquals(sujeto, resultado.getSujeto());
        assertEquals(verbo, resultado.getVerbo());
        assertEquals(complemento, resultado.getComplemento());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Corre perro el rapido.",
            "El estudiante.",
            "La come arroz.",
            "Perro el corre.",
            "Juan una carta escribe."
    })
    void rechazaOracionesInvalidas(String oracion) {
        ResultadoAnalisis resultado = casoUso.analizar(oracion);

        assertFalse(resultado.isValida());
        assertNotNull(resultado.getError());
    }

    @Test
    void rechazaConcordanciaIncorrectaEntreArticuloYSustantivo() {
        ResultadoAnalisis resultado = casoUso.analizar("La perro come tranquilo.");

        assertFalse(resultado.isValida());
        assertEquals("CONCORDANCIA_INVALIDA", resultado.getError().getCodigo());
        assertEquals("La perro", resultado.getError().getLexema());
    }

    @Test
    void aceptaOracionSinComplemento() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro come.");

        assertTrue(resultado.isValida());
        assertEquals("El perro", resultado.getSujeto());
        assertEquals("come", resultado.getVerbo());
        assertEquals("", resultado.getComplemento());
        assertEquals("ART SUST V", resultado.getDerivacion().get(resultado.getDerivacion().size() - 1));
    }

    @Test
    void aceptaComplementoLargoPorPartes() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro come tranquilo en la casa.");

        assertTrue(resultado.isValida());
        assertEquals("tranquilo en la casa", resultado.getComplemento());
        assertEquals(
                "ART SUST V ADV PREP ART SUST",
                resultado.getDerivacion().get(resultado.getDerivacion().size() - 1)
        );
    }

    @Test
    void rechazaNumeros() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro corre 123.");

        assertFalse(resultado.isValida());
        assertEquals("NUMERO_NO_PERMITIDO", resultado.getError().getCodigo());
        assertEquals("123", resultado.getError().getLexema());
    }

    @Test
    void rechazaComasYSignosInternos() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro, corre rapido.");

        assertFalse(resultado.isValida());
        assertEquals("SIGNO_NO_PERMITIDO", resultado.getError().getCodigo());
        assertEquals("perro,", resultado.getError().getLexema());
    }

    @Test
    void rechazaSimbolosEspeciales() {
        ResultadoAnalisis resultado = casoUso.analizar("Juan escribe una carta#.");

        assertFalse(resultado.isValida());
        assertEquals("SIGNO_NO_PERMITIDO", resultado.getError().getCodigo());
    }

    @Test
    void aceptaSignosFinalesPermitidos() {
        ResultadoAnalisis resultado = casoUso.analizar("¿El perro corre rapido?");

        assertTrue(resultado.isValida());
        assertEquals("El perro", resultado.getSujeto());
    }

    @Test
    void generaTablaTokens() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro corre rapido.");

        assertEquals("El", resultado.getTokens().get(0).lexema());
        assertEquals("ART", resultado.getTokens().get(0).token());
        assertEquals("perro", resultado.getTokens().get(1).lexema());
        assertEquals("SUST", resultado.getTokens().get(1).token());
        assertEquals("corre", resultado.getTokens().get(2).lexema());
        assertEquals("V", resultado.getTokens().get(2).token());
        assertEquals("rapido", resultado.getTokens().get(3).lexema());
        assertEquals("ADV", resultado.getTokens().get(3).token());
    }

    @Test
    void generaArbolDerivacion() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro corre rapido.");

        assertEquals("S", resultado.getArbol().getNombre());
        assertEquals("SN", resultado.getArbol().getHijos().get(0).getNombre());
        assertEquals("SV", resultado.getArbol().getHijos().get(1).getNombre());
        assertEquals("ART SUST V ADV", resultado.getDerivacion().get(resultado.getDerivacion().size() - 1));
    }

    @Test
    void generaDerivacionPorLaIzquierdaConComplementoNominal() {
        ResultadoAnalisis resultado = casoUso.analizar("Juan escribe una carta.");

        assertEquals(List.of(
                "S",
                "SN SV",
                "SUST SV",
                "SUST V C",
                "SUST V SN",
                "SUST V ART SUST"
        ), resultado.getDerivacion());
    }

    @Test
    void generaDerivacionPorLaIzquierdaConComplementoPreposicionalDoble() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro mordió al hombre en el parque.");

        assertTrue(resultado.getDerivacion().contains("ART SUST V PREP SN C"));
        assertTrue(resultado.getDerivacion().contains("ART SUST V PREP SUST PREP SN"));
        assertEquals(
                "ART SUST V PREP SUST PREP ART SUST",
                resultado.getDerivacion().get(resultado.getDerivacion().size() - 1)
        );
    }

    @Test
    void identificaSujetoYVerbo() {
        ResultadoAnalisis resultado = casoUso.analizar("Juan escribe una carta.");

        assertEquals("Juan", resultado.getSujeto());
        assertEquals("escribe", resultado.getVerbo());
    }

    @Test
    void detectaAmbiguedadSintactica() {
        ResultadoAnalisis resultado = casoUso.analizar("El perro mordió al hombre en el parque.");

        assertTrue(resultado.isValida());
        assertTrue(resultado.isAmbiguo());
        assertNotNull(resultado.getAmbiguedad());
        assertEquals(2, resultado.getAmbiguedad().getInterpretaciones().size());
    }

    @Test
    void detectaOtraAmbiguedadSintactica() {
        ResultadoAnalisis resultado = casoUso.analizar("Juan vio al hombre con el telescopio.");

        assertTrue(resultado.isValida());
        assertTrue(resultado.isAmbiguo());
        assertNotNull(resultado.getAmbiguedad());
        assertEquals(2, resultado.getAmbiguedad().getInterpretaciones().size());
    }

    private static Stream<Arguments> oracionesValidas() {
        return Stream.of(
                Arguments.of("El perro corre rapido.", "El perro", "corre", "rapido"),
                Arguments.of("La niña estudia matematicas.", "La niña", "estudia", "matematicas"),
                Arguments.of("María come arroz.", "María", "come", "arroz"),
                Arguments.of("El estudiante lee libros.", "El estudiante", "lee", "libros"),
                Arguments.of("Nosotros jugamos futbol.", "Nosotros", "jugamos", "futbol"),
                Arguments.of("Juan escribe una carta.", "Juan", "escribe", "una carta"),
                Arguments.of("La profesora explica la clase.", "La profesora", "explica", "la clase"),
                Arguments.of("El gato duerme tranquilo.", "El gato", "duerme", "tranquilo"),
                Arguments.of("Pedro compra pan.", "Pedro", "compra", "pan"),
                Arguments.of("Ellos miran television.", "Ellos", "miran", "television")
        );
    }
}
