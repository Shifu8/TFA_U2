// Archivo: EntradaOracion.jsx
// Descripción: Permite escribir y enviar una oración para análisis.
// Responsable: Viviana

import { Search, Wand2 } from "lucide-react";

const ejemplos = [
  "El perro corre rápido.",
  "La niña estudia matemáticas.",
  "Juan escribe una carta.",
  "Corre perro el rápido.",
  "El perro mordió al hombre en el parque.",
];

export default function EntradaOracion({
  oracion,
  setOracion,
  alAnalizar,
  cargando,
}) {
  return (
    <section className="panel entrada-panel">
      <div className="panel-titulo">
        <Wand2 size={22} aria-hidden="true" />
        <div>
          <h1>Procesamiento de Lenguaje Natural Básico</h1>
          <p>Sujeto + Verbo + Complemento</p>
        </div>
      </div>

      <form className="entrada-formulario" onSubmit={alAnalizar}>
        <label htmlFor="oracion">Oración</label>
        <div className="entrada-controles">
          <input
            id="oracion"
            type="text"
            value={oracion}
            onChange={(evento) => setOracion(evento.target.value)}
            placeholder="El perro corre rápido."
            autoComplete="off"
          />
          <button type="submit" disabled={cargando} title="Analizar oración">
            <Search size={18} aria-hidden="true" />
            <span>{cargando ? "Analizando" : "Analizar"}</span>
          </button>
        </div>
      </form>

      <div className="ejemplos" aria-label="Ejemplos de oraciones">
        {ejemplos.map((ejemplo) => (
          <button
            type="button"
            key={ejemplo}
            onClick={() => setOracion(ejemplo)}
            title={`Usar: ${ejemplo}`}
          >
            {ejemplo}
          </button>
        ))}
      </div>
    </section>
  );
}

