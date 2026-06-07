// Archivo: analisisServicio.js
// Descripción: Conecta la interfaz React con la API REST del backend.
// Responsable: Brandon

const API_BASE = import.meta.env.VITE_API_URL || "http://localhost:5000";

export async function analizarOracion(oracion) {
  const respuesta = await fetch(`${API_BASE}/api/analizar`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ oracion }),
  });

  if (!respuesta.ok) {
    throw new Error("No se pudo completar el análisis.");
  }

  return respuesta.json();
}

