// Archivo: RestControllerAnalizador.java
// Descripcion: Expone el caso de uso mediante una API REST con Spring Boot.
// Responsable: Brandon

package com.tfa.unidad2.adaptadores.entrada;

import com.tfa.unidad2.dominio.entidades.ResultadoAnalisis;
import com.tfa.unidad2.puertos.entrada.PuertoAnalizarOracion;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class RestControllerAnalizador {

    private final PuertoAnalizarOracion puertoAnalizarOracion;

    public RestControllerAnalizador(PuertoAnalizarOracion puertoAnalizarOracion) {
        this.puertoAnalizarOracion = puertoAnalizarOracion;
    }

    @PostMapping("/api/analizar")
    public ResultadoAnalisis analizar(@RequestBody(required = false) AnalizarRequest request) {
        String oracion = request == null ? "" : request.oracion();
        return puertoAnalizarOracion.analizar(oracion);
    }

    @GetMapping("/api/salud")
    public Map<String, String> salud() {
        return Map.of("estado", "ok");
    }
}

