// Archivo: ArbolDerivacion.jsx
// Descripción: Dibuja el árbol de derivación de forma gráfica.
// Responsable: Viviana

function NodoVisual({ nodo }) {
  if (!nodo) {
    return null;
  }

  const tieneHijos = Array.isArray(nodo.hijos) && nodo.hijos.length > 0;
  const claseHijos = nodo.hijos?.length > 1 ? "arbol-hijos multiple" : "arbol-hijos simple";

  return (
    <div className="arbol-item">
      <span className={tieneHijos ? "nodo rama" : "nodo hoja"}>
        {nodo.nombre}
      </span>
      {tieneHijos && (
        <div className={claseHijos}>
          {nodo.hijos.map((hijo, indice) => (
            <NodoVisual nodo={hijo} key={`${hijo.nombre}-${indice}`} />
          ))}
        </div>
      )}
    </div>
  );
}

export default function ArbolDerivacion({
  arbol,
  titulo = "Árbol de derivación",
  compacto = false,
}) {
  if (!arbol) {
    return null;
  }

  const contenido = (
    <>
      <h2>{titulo}</h2>
      <div className="arbol-scroll">
        <div className="arbol">
          <NodoVisual nodo={arbol} />
        </div>
      </div>
    </>
  );

  if (compacto) {
    return <div className="arbol-compacto">{contenido}</div>;
  }

  return (
    <section className="panel arbol-panel">
      {contenido}
    </section>
  );
}
