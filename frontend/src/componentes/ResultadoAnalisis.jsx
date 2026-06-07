// Archivo: ResultadoAnalisis.jsx
// Descripcion: Muestra estado, sujeto, verbo, complemento y errores.
// Responsable: Brandon

import { AlertCircle, CheckCircle2, Split, XCircle } from "lucide-react";

const ayudasError = {
  NUMERO_NO_PERMITIDO:
    "Los numeros no forman parte de la gramatica. Usa solo palabras del vocabulario permitido.",
  SIGNO_NO_PERMITIDO:
    "Las comas, parentesis y simbolos internos se marcan como error. Solo se ignoran signos finales como punto, interrogacion o exclamacion.",
  PALABRA_NO_RECONOCIDA:
    "La palabra no esta dentro del vocabulario base del analizador.",
  FALTA_VERBO: "La oracion tiene sujeto, pero no tiene verbo.",
  FALTA_COMPLEMENTO: "El complemento es opcional; revisa si quedo incompleto despues del verbo.",
  FALTA_SUJETO: "La oracion empieza con verbo o no presenta un sujeto valido.",
  ORDEN_INCORRECTO: "La estructura esperada es sujeto, luego verbo y, si existe, complemento.",
  FALTA_SUSTANTIVO: "Despues de un articulo debe aparecer un sustantivo.",
  CONCORDANCIA_INVALIDA:
    "El articulo y el sustantivo deben coincidir en genero y numero.",
};

export default function ResultadoAnalisis({ resultado, errorLocal }) {
  if (errorLocal) {
    return (
      <section className="panel resultado-panel invalido">
        <div className="estado">
          <AlertCircle size={22} aria-hidden="true" />
          <strong>{errorLocal}</strong>
        </div>
      </section>
    );
  }

  if (!resultado) {
    return null;
  }

  const esAmbigua = resultado.valida && resultado.ambiguo;
  const Icono = esAmbigua ? Split : resultado.valida ? CheckCircle2 : XCircle;
  const estadoClase = !resultado.valida ? "invalido" : esAmbigua ? "ambiguo" : "valido";
  const tituloEstado = !resultado.valida
    ? "Oracion invalida"
    : esAmbigua
      ? "Oracion valida, pero ambigua"
      : "Oracion valida";
  const codigoError = resultado.error?.codigo;
  const ayuda = codigoError ? ayudasError[codigoError] : "";

  return (
    <section className={`panel resultado-panel ${estadoClase}`}>
      <div className="estado">
        <Icono size={24} aria-hidden="true" />
        <div>
          <strong>{tituloEstado}</strong>
          <span>{resultado.mensaje}</span>
        </div>
      </div>

      {esAmbigua && (
        <div className="aviso-ambiguedad">
          <strong>Ambiguedad sintactica detectada</strong>
          <span>
            La oracion cumple la gramatica, pero puede generar mas de una interpretacion.
          </span>
        </div>
      )}

      {resultado.valida && (
        <div className="partes-oracion">
          <div>
            <span>Sujeto</span>
            <strong>{resultado.sujeto}</strong>
          </div>
          <div>
            <span>Verbo</span>
            <strong>{resultado.verbo}</strong>
          </div>
          <div>
            <span>Complemento</span>
            <strong>{resultado.complemento || "Sin complemento"}</strong>
          </div>
        </div>
      )}

      {!resultado.valida && resultado.error && (
        <>
          <div className="detalle-error">
            <span>Codigo</span>
            <strong>{resultado.error.codigo}</strong>
            <span>Lexema</span>
            <strong>{resultado.error.lexema || "No aplica"}</strong>
            <span>Posicion</span>
            <strong>{resultado.procesados_hasta}</strong>
          </div>
          {ayuda && <p className="validacion-ayuda">{ayuda}</p>}
        </>
      )}
    </section>
  );
}
