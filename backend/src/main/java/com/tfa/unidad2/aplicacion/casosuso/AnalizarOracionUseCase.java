// Archivo: AnalizarOracionUseCase.java
// Descripcion: Orquesta el analizador JFlex/CUP y el detector de ambiguedad.
// Responsable: Brandon

package com.tfa.unidad2.aplicacion.casosuso;

import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.dominio.gramatica.Gramatica;
import com.tfa.unidad2.dominio.servicios.AnalizadorJFlexCup;
import com.tfa.unidad2.dominio.servicios.DetectorAmbiguedad;
import com.tfa.unidad2.puertos.entrada.PuertoAnalizarOracion;
import org.springframework.stereotype.Service;

@Service
public class AnalizarOracionUseCase implements PuertoAnalizarOracion {

    private final AnalizadorJFlexCup analizador;
    private final DetectorAmbiguedad detectorAmbiguedad;

    public AnalizarOracionUseCase() {
        this(new Gramatica());
    }

    private AnalizarOracionUseCase(Gramatica gramatica) {
        this(new AnalizadorJFlexCup(gramatica), new DetectorAmbiguedad());
    }

    public AnalizarOracionUseCase(AnalizadorJFlexCup analizador, DetectorAmbiguedad detectorAmbiguedad) {
        this.analizador = analizador;
        this.detectorAmbiguedad = detectorAmbiguedad;
    }

    @Override
    public ResultadoAnalisis analizar(String oracion) {
        ResultadoAnalisis resultado = analizador.analizar(oracion);

        if (resultado.isValida()) {
            var ambiguedad = detectorAmbiguedad.detectar(resultado.getTokens());
            if (ambiguedad != null) {
                resultado.setAmbiguo(true);
                resultado.setAmbiguedad(ambiguedad);
            }
        }

        return resultado;
    }
}
