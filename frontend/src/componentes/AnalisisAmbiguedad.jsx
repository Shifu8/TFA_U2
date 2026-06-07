// Archivo: AnalisisAmbiguedad.jsx
// Descripcion: Muestra explicaciones y arboles para la ambiguedad sintactica.
// Responsable: Santiago

import { Split } from "lucide-react";
import ArbolDerivacion from "./ArbolDerivacion.jsx";

export default function AnalisisAmbiguedad({ resultado }) {
  if (!resultado?.ambiguo || !resultado.ambiguedad) {
    return null;
  }

  return (
    <section className="panel ambiguedad-panel">
      <div className="panel-titulo compacto">
        <Split size={22} aria-hidden="true" />
        <div>
          <h2>Ambiguedad sintactica</h2>
          <p>{resultado.ambiguedad.explicacion}</p>
        </div>
      </div>

      <div className="ambiguedad-resumen">
        <Split size={20} aria-hidden="true" />
        <div>
          <strong>Valida, pero con mas de una lectura.</strong>
          <span>
            El analizador no lo marca como error porque la estructura gramatical
            si se cumple; abajo se muestran las interpretaciones posibles.
          </span>
        </div>
      </div>

      <div className="interpretaciones">
        {resultado.ambiguedad.interpretaciones.map((interpretacion, indice) => (
          <article className="interpretacion" key={interpretacion.titulo}>
            <div className="interpretacion-cabecera">
              <span>{indice + 1}</span>
              <div>
                <h3>{interpretacion.titulo}</h3>
                <p>{interpretacion.descripcion}</p>
              </div>
            </div>
            <ArbolDerivacion
              arbol={interpretacion.arbol}
              titulo="Arbol de esta lectura"
              compacto
            />
          </article>
        ))}
      </div>
    </section>
  );
}
