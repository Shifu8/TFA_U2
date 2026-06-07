// Archivo: ResultadoAnalisis.jsx
// Descripción: Muestra estado, sujeto, verbo, complemento y errores.
// Responsable: Brandon

import { AlertCircle, CheckCircle2, XCircle } from "lucide-react";

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
        <div className="detalle-error">
          <span>Código</span>
          <strong>{resultado.error.codigo}</strong>
          <span>Token procesado</span>
          <strong>{resultado.procesados_hasta}</strong>
        </div>
      )}
    </section>
  );
}

