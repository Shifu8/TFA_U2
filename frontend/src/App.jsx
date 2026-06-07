// Archivo: App.jsx
// Descripción: Coordina la interfaz principal y el consumo de la API REST.
// Responsable: Brandon

import { useState } from "react";
import AnalisisAmbiguedad from "./componentes/AnalisisAmbiguedad.jsx";
import ArbolDerivacion from "./componentes/ArbolDerivacion.jsx";
import DerivacionPasoAPaso from "./componentes/DerivacionPasoAPaso.jsx";
import EntradaOracion from "./componentes/EntradaOracion.jsx";
import ResultadoAnalisis from "./componentes/ResultadoAnalisis.jsx";
import TablaTokens from "./componentes/TablaTokens.jsx";
import { analizarOracion } from "./servicios/analisisServicio.js";

export default function App() {
  const [oracion, setOracion] = useState("El perro corre rápido.");
  const [resultado, setResultado] = useState(null);
  const [errorLocal, setErrorLocal] = useState("");
  const [cargando, setCargando] = useState(false);

  async function alAnalizar(evento) {
    evento.preventDefault();
    const texto = oracion.replace(/\s+/g, " ").trim();

    if (!texto) {
      setResultado(null);
      setErrorLocal("La oración no puede estar vacía.");
      return;
    }

    setCargando(true);
    setErrorLocal("");

    try {
      const datos = await analizarOracion(texto);
      setResultado(datos);
    } catch (error) {
      setResultado(null);
      setErrorLocal(error.message || "No se pudo conectar con el backend.");
    } finally {
      setCargando(false);
    }
  }

  return (
    <main className="app">
      <div className="contenedor">
        <EntradaOracion
          oracion={oracion}
          setOracion={setOracion}
          alAnalizar={alAnalizar}
          cargando={cargando}
        />

        <ResultadoAnalisis resultado={resultado} errorLocal={errorLocal} />

        {resultado && (
          <div className="rejilla">
            <TablaTokens tokens={resultado.tokens} />
            <DerivacionPasoAPaso derivacion={resultado.derivacion} />
            <ArbolDerivacion arbol={resultado.arbol} />
            <AnalisisAmbiguedad resultado={resultado} />
          </div>
        )}
      </div>
    </main>
  );
}

