// Archivo: DerivacionPasoAPaso.jsx
// Descripción: Presenta la derivación sintáctica por la izquierda paso a paso.
// Responsable: Brandon

export default function DerivacionPasoAPaso({ derivacion = [] }) {
  if (!derivacion.length) {
    return null;
  }

  return (
    <section className="panel">
      <h2>Derivación: por la izquierda</h2>
      <p className="derivacion-razon">
        Porque en cada paso se expande primero el no terminal más a la izquierda.
      </p>
      <ol className="derivacion">
        {derivacion.map((paso, indice) => (
          <li key={`${paso}-${indice}`}>
            <span>{indice + 1}</span>
            <code>{paso}</code>
          </li>
        ))}
      </ol>
    </section>
  );
}
