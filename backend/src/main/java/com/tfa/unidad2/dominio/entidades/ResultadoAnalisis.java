// Archivo: ResultadoAnalisis.java
// Descripcion: Agrupa el resultado lexico, sintactico y visual del analisis.
// Responsable: Brandon

package com.tfa.unidad2.dominio.entidades;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class ResultadoAnalisis {

    private boolean valida;
    private String mensaje;
    private String sujeto;
    private String verbo;
    private String complemento;
    private List<TokenAnalisis> tokens;
    private List<String> derivacion;
    private NodoArbol arbol;
    private boolean ambiguo;
    private AmbiguedadSintactica ambiguedad;
    private ErrorAnalisis error;

    @JsonProperty("procesados_hasta")
    private int procesadosHasta;

    public ResultadoAnalisis() {
        this.tokens = new ArrayList<>();
        this.derivacion = new ArrayList<>();
        this.sujeto = "";
        this.verbo = "";
        this.complemento = "";
    }

    public boolean isValida() {
        return valida;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSujeto() {
        return sujeto;
    }

    public void setSujeto(String sujeto) {
        this.sujeto = sujeto;
    }

    public String getVerbo() {
        return verbo;
    }

    public void setVerbo(String verbo) {
        this.verbo = verbo;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public List<TokenAnalisis> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenAnalisis> tokens) {
        this.tokens = tokens;
    }

    public List<String> getDerivacion() {
        return derivacion;
    }

    public void setDerivacion(List<String> derivacion) {
        this.derivacion = derivacion;
    }

    public NodoArbol getArbol() {
        return arbol;
    }

    public void setArbol(NodoArbol arbol) {
        this.arbol = arbol;
    }

    public boolean isAmbiguo() {
        return ambiguo;
    }

    public void setAmbiguo(boolean ambiguo) {
        this.ambiguo = ambiguo;
    }

    public AmbiguedadSintactica getAmbiguedad() {
        return ambiguedad;
    }

    public void setAmbiguedad(AmbiguedadSintactica ambiguedad) {
        this.ambiguedad = ambiguedad;
    }

    public ErrorAnalisis getError() {
        return error;
    }

    public void setError(ErrorAnalisis error) {
        this.error = error;
    }

    public int getProcesadosHasta() {
        return procesadosHasta;
    }

    public void setProcesadosHasta(int procesadosHasta) {
        this.procesadosHasta = procesadosHasta;
    }
}

