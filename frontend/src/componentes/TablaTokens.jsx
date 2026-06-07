// Archivo: TablaTokens.jsx
// Descripción: Renderiza la tabla de lexemas y tokens reconocidos.
// Responsable: Justin

export default function TablaTokens({ tokens = [] }) {
  if (!tokens.length) {
    return null;
  }

  return (
    <section className="panel">
      <h2>Tabla token/lexema</h2>
      <div className="tabla-contenedor">
        <table>
          <thead>
            <tr>
              <th>Lexema</th>
              <th>Token</th>
            </tr>
          </thead>
          <tbody>
            {tokens.map((token, indice) => (
              <tr key={`${token.lexema}-${indice}`}>
                <td>{token.lexema}</td>
                <td>
                  <span className={token.token === "ERROR" ? "token-error" : "token"}>
                    {token.token}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}

