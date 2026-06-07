// Archivo: AnalizarOracionUseCase.java
// Descripcion: Orquesta lexer, parser y detector de ambiguedad.
// Responsable: Brandon

package com.tfa.unidad2.aplicacion.casosuso;

import com.tfa.unidad2.dominio.entidades.ErrorAnalisis;
import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import com.tfa.unidad2.dominio.servicios.DetectorAmbiguedad;
import com.tfa.unidad2.dominio.servicios.Lexer;
import com.tfa.unidad2.dominio.servicios.Parser;
import com.tfa.unidad2.puertos.entrada.PuertoAnalizarOracion;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnalizarOracionUseCase implements PuertoAnalizarOracion {

    private final Lexer lexer;
    private final Parser parser;
    private final DetectorAmbiguedad detectorAmbiguedad;

    public AnalizarOracionUseCase() {
        this(new Lexer(), new Parser(), new DetectorAmbiguedad());
    }

    public AnalizarOracionUseCase(Lexer lexer, Parser parser, DetectorAmbiguedad detectorAmbiguedad) {
        this.lexer = lexer;
        this.parser = parser;
        this.detectorAmbiguedad = detectorAmbiguedad;
    }

    @Override
    public ResultadoAnalisis analizar(String oracion) {
        String oracionLimpia = lexer.limpiarOracion(oracion);
        if (oracionLimpia.isBlank()) {
            ResultadoAnalisis resultado = new ResultadoAnalisis();
            resultado.setValida(false);
            resultado.setMensaje("La oración no puede estar vacía.");
            resultado.setError(new ErrorAnalisis("ORACION_VACIA", 0));
            resultado.setProcesadosHasta(0);
            return resultado;
        }

        List<TokenAnalisis> tokens = lexer.tokenizar(oracionLimpia);
        ResultadoAnalisis resultado = parser.analizar(tokens);

        if (resultado.isValida()) {
            var ambiguedad = detectorAmbiguedad.detectar(tokens);
            if (ambiguedad != null) {
                resultado.setAmbiguo(true);
                resultado.setAmbiguedad(ambiguedad);
            }
        }

        return resultado;
    }
}
