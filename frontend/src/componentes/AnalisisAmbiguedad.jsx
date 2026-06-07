// Archivo: AnalisisAmbiguedad.jsx
// Descripción: Muestra explicaciones y árboles para la ambigüedad sintáctica.
// Responsable: Santiago

import { MapPin, Split } from "lucide-react";
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
          <h2>Ambigüedad sintáctica</h2>
          <p>{resultado.ambiguedad.explicacion}</p>
        </div>
      </div>

      <div className="ambiguedad-resumen">
        <MapPin size={20} aria-hidden="true" />
        <div>
          <strong>La duda está en “en el parque”.</strong>
          <span>
            Puede ser el lugar donde ocurrió la mordida, o puede describir al
            hombre. La oración es válida, pero permite dos árboles.
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
              titulo="Árbol de esta lectura"
              compacto
            />
          </article>
        ))}
      </div>
    </section>
  );
}

