// Archivo: AmbiguedadSintactica.java
// Descripcion: Agrupa la explicacion y las interpretaciones de ambiguedad.
// Responsable: Santiago

package com.tfa.unidad2.dominio.entidades;

import java.util.List;

public class AmbiguedadSintactica {

    private String explicacion;
    private List<InterpretacionAmbiguedad> interpretaciones;

    public AmbiguedadSintactica(String explicacion, List<InterpretacionAmbiguedad> interpretaciones) {
        this.explicacion = explicacion;
        this.interpretaciones = interpretaciones;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    public List<InterpretacionAmbiguedad> getInterpretaciones() {
        return interpretaciones;
    }

    public void setInterpretaciones(List<InterpretacionAmbiguedad> interpretaciones) {
        this.interpretaciones = interpretaciones;
    }
}

