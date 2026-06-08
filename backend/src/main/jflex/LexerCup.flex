/* Archivo: LexerCup.flex
 * Descripcion: Especificacion JFlex que reconoce lexemas y entrega simbolos a CUP.
 * Responsable: Justin
 */

package com.tfa.unidad2.dominio.generado;

import com.tfa.unidad2.dominio.entidades.TokenAnalisis;
import com.tfa.unidad2.dominio.gramatica.Gramatica;
import java_cup.runtime.Symbol;

%%

%public
%class LexerCup
%unicode
%cupsym TokensCup
%cup
%line
%column
%eofval{
    return new Symbol(TokensCup.EOF);
%eofval}

%{
    private Gramatica gramatica = new Gramatica();
    private int posicionToken = 0;

    public void configurar(Gramatica gramatica) {
        this.gramatica = gramatica;
    }

    private Symbol simboloDesdeLexema(String lexema) {
        String token = contieneCaracteresInvalidos(lexema) ? "ERROR" : gramatica.clasificar(lexema);
        TokenAnalisis tokenAnalisis = new TokenAnalisis(lexema, token, posicionToken++);
        return new Symbol(tipoSimbolo(token), yyline, yycolumn, tokenAnalisis);
    }

    private int tipoSimbolo(String token) {
        return switch (token) {
            case "ART" -> TokensCup.ART;
            case "PRON" -> TokensCup.PRON;
            case "SUST" -> TokensCup.SUST;
            case "V" -> TokensCup.V;
            case "PREP" -> TokensCup.PREP;
            case "ADV" -> TokensCup.ADV;
            default -> TokensCup.ERROR;
        };
    }

    private boolean contieneCaracteresInvalidos(String lexema) {
        return lexema.matches(".*\\d.*") || !lexema.matches("\\p{L}+");
    }
%}

Espacio = [ \t\f\r\n]+
Fragmento = [^ \t\f\r\n]+

%%

{Espacio}     { }
{Fragmento}   { return simboloDesdeLexema(yytext()); }
