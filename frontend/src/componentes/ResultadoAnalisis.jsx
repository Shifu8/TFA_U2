// Archivo: ResultadoAnalisis.jsx
// Descripción: Muestra estado, sujeto, verbo, complemento y errores.
// Responsable: Brandon

import { AlertCircle, CheckCircle2, XCircle } from "lucide-react";

const ayudasError = {
  NUMERO_NO_PERMITIDO:
    "Los números no forman parte de la gramática. Usa solo palabras del vocabulario permitido.",
  SIGNO_NO_PERMITIDO:
    "Las comas, paréntesis y símbolos internos se marcan como error. Solo se ignoran signos finales como punto, interrogación o exclamación.",
  PALABRA_NO_RECONOCIDA:
    "La palabra no está dentro del vocabulario base del analizador.",
  FALTA_VERBO: "La oración tiene sujeto, pero no tiene verbo.",
  FALTA_COMPLEMENTO: "La oración tiene sujeto y verbo, pero no tiene complemento.",
  FALTA_SUJETO: "La oración empieza con verbo o no presenta un sujeto válido.",
  ORDEN_INCORRECTO: "La estructura esperada es sujeto, luego verbo y luego complemento.",
  FALTA_SUSTANTIVO: "Después de un artículo debe aparecer un sustantivo.",
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

  const Icono = resultado.valida ? CheckCircle2 : XCircle;
  const codigoError = resultado.error?.codigo;
  const ayuda = codigoError ? ayudasError[codigoError] : "";

  return (
    <section
      className={`panel resultado-panel ${resultado.valida ? "valido" : "invalido"}`}
    >
      <div className="estado">
        <Icono size={24} aria-hidden="true" />
        <div>
          <strong>{resultado.valida ? "Oración válida" : "Oración inválida"}</strong>
          <span>{resultado.mensaje}</span>
        </div>
      </div>

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
            <strong>{resultado.complemento}</strong>
          </div>
        </div>
      )}

      {!resultado.valida && resultado.error && (
        <>
          <div className="detalle-error">
            <span>Código</span>
            <strong>{resultado.error.codigo}</strong>
            <span>Lexema</span>
            <strong>{resultado.error.lexema || "No aplica"}</strong>
            <span>Posición</span>
            <strong>{resultado.procesados_hasta}</strong>
          </div>
          {ayuda && <p className="validacion-ayuda">{ayuda}</p>}
        </>
      )}
    </section>
  );
}

