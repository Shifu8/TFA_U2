// Archivo: AnalisisAmbiguedad.jsx
// Descripción: Muestra explicaciones y árboles para la ambigüedad sintáctica.
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
        <Split size={21} aria-hidden="true" />
        <div>
          <h2>Ambigüedad sintáctica</h2>
          <p>{resultado.ambiguedad.explicacion}</p>
        </div>
      </div>

      <div className="interpretaciones">
        {resultado.ambiguedad.interpretaciones.map((interpretacion) => (
          <div className="interpretacion" key={interpretacion.titulo}>
            <h3>{interpretacion.titulo}</h3>
            <p>{interpretacion.descripcion}</p>
            <ArbolDerivacion
              arbol={interpretacion.arbol}
              titulo="Estructura posible"
              compacto
            />
          </div>
        ))}
      </div>
    </section>
  );
}
