// Archivo: EntradaOracion.jsx
// Descripción: Permite escribir y enviar una oración para análisis.
// Responsable: Viviana

import { Search } from "lucide-react";

const ejemplos = [
  { texto: "El perro corre rápido.", tipo: "valida", etiqueta: "Válida" },
  { texto: "El perro come.", tipo: "valida", etiqueta: "Sin comp." },
  { texto: "La niña estudia matemáticas.", tipo: "valida", etiqueta: "Válida" },
  { texto: "Juan escribe una carta.", tipo: "valida", etiqueta: "Válida" },
  { texto: "La perro come tranquilo.", tipo: "invalida", etiqueta: "Conc." },
  { texto: "El perro, corre rápido.", tipo: "invalida", etiqueta: "Coma" },
  { texto: "El perro corre 123.", tipo: "invalida", etiqueta: "Número" },
  { texto: "Corre perro el rápido.", tipo: "invalida", etiqueta: "Inválida" },
  { texto: "El perro mordió al hombre en el parque.", tipo: "ambigua", etiqueta: "Ambigua" },
  { texto: "Juan vio al hombre con el telescopio.", tipo: "ambigua", etiqueta: "Ambigua" },
];

export default function EntradaOracion({
  oracion,
  setOracion,
  alAnalizar,
  cargando,
}) {
  return (
    <section className="panel entrada-panel">
      <div className="panel-titulo titulo-principal">
        <div>
          <h1>Procesamiento de Lenguaje Natural Básico</h1>
          <p>Sujeto + Verbo (+ Complemento)</p>
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

      <div className="sugerencias-titulo">Sugerencias rápidas</div>
      <div className="ejemplos" aria-label="Ejemplos de oraciones">
        {ejemplos.map((ejemplo) => (
          <button
            type="button"
            className={`ejemplo ${ejemplo.tipo}`}
            key={ejemplo.texto}
            onClick={() => setOracion(ejemplo.texto)}
            title={`Usar: ${ejemplo.texto}`}
          >
            <span className="ejemplo-etiqueta">{ejemplo.etiqueta}</span>
            <span className="ejemplo-texto">{ejemplo.texto}</span>
          </button>
        ))}
      </div>
    </section>
  );
}
