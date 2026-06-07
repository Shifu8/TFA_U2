# Procesamiento de Lenguaje Natural Básico

Proyecto académico para validar si una oración en español cumple la estructura:

```text
Sujeto + Verbo + Complemento
```

El sistema realiza análisis léxico, análisis sintáctico, tabla token/lexema, derivación paso a paso, árbol de derivación e identificación de sujeto, verbo y complemento. La validación se centra en la estructura gramatical, no en la coherencia semántica.

La visualización toma como referencia el flujo de análisis del repositorio `Shifu8/APE8_Automatas`: entrada, tabla de tokens, derivación y árbol sintáctico.

## Escenario

Un chatbot necesita validar oraciones simples en español antes de procesarlas. Para ello, este proyecto implementa una Gramática Libre de Contexto que reconoce sintagmas nominales, verbos y complementos, y entrega una explicación clara cuando una oración es inválida.

## Arquitectura Hexagonal

El backend está organizado con puertos y adaptadores:

| Capa | Responsabilidad |
| --- | --- |
| Dominio | Gramática, reglas, lexer, parser, árbol, resultado y detector de ambigüedad. |
| Aplicación | Caso de uso `AnalizarOracionUseCase`. |
| Puertos | Interfaces de entrada y salida. |
| Adaptadores | API REST Spring Boot y exportador JSON. |

Estructura principal:

```text
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/tfa/unidad2/
    │   │   ├── dominio/
    │   │   ├── aplicacion/
    │   │   ├── puertos/
    │   │   └── adaptadores/
    │   └── resources/application.properties
    └── test/java/com/tfa/unidad2/pruebas/

frontend/
└── src/
    ├── componentes/
    ├── servicios/
    ├── App.jsx
    └── styles.css
```

## Gramática Libre de Contexto

```text
S  -> SN SV
SN -> ART SUST | PRON | SUST
SV -> V C
C  -> ADV | SN | PREP SN | SN PREP SN | PREP SN PREP SN
```

La producción `C -> PREP SN PREP SN` se incluye para analizar el caso obligatorio de ambigüedad:

```text
El perro mordió al hombre en el parque.
```

## Tecnologías

| Parte | Tecnología |
| --- | --- |
| Backend | Java 21+, Spring Boot, Maven |
| Frontend | React, Vite, Lucide React |
| Comunicación | API REST |
| Pruebas | JUnit 5 |

## Comandos Para VS Code

Abre dos terminales en la raíz del proyecto.

Terminal 1, backend:

```powershell
cd backend
mvn spring-boot:run
```

Terminal 2, frontend:

```powershell
cd frontend
npm install
npm run dev
```

URLs:

```text
Backend:  http://localhost:5000
Frontend: http://localhost:5173
```

Endpoint principal:

```text
POST /api/analizar
```

## Ejecutar Pruebas

Backend:

```powershell
cd backend
mvn test
```

Frontend:

```powershell
cd frontend
npm run build
```

## Ejemplo de Entrada

```json
{
  "oracion": "El perro corre rápido"
}
```

## Ejemplo de Salida

```json
{
  "valida": true,
  "mensaje": "Oracion valida",
  "sujeto": "El perro",
  "verbo": "corre",
  "complemento": "rápido",
  "tokens": [
    { "lexema": "El", "token": "ART" },
    { "lexema": "perro", "token": "SUST" },
    { "lexema": "corre", "token": "V" },
    { "lexema": "rápido", "token": "ADV" }
  ],
  "derivacion": [
    "S",
    "SN SV",
    "ART SUST SV",
    "ART SUST V C",
    "ART SUST V ADV"
  ]
}
```

## Oraciones Válidas

| # | Oración |
| --- | --- |
| 1 | El perro corre rápido. |
| 2 | La niña estudia matemáticas. |
| 3 | María come arroz. |
| 4 | El estudiante lee libros. |
| 5 | Nosotros jugamos fútbol. |
| 6 | Juan escribe una carta. |
| 7 | La profesora explica la clase. |
| 8 | El gato duerme tranquilo. |
| 9 | Pedro compra pan. |
| 10 | Ellos miran televisión. |

## Oraciones Inválidas

| # | Oración | Motivo esperado |
| --- | --- | --- |
| 1 | Corre perro el rápido. | Falta sujeto antes del verbo. |
| 2 | El estudiante. | Falta verbo. |
| 3 | La come arroz. | Falta sustantivo después del artículo. |
| 4 | Perro el corre. | Orden incorrecto. |
| 5 | Juan una carta escribe. | Orden incorrecto. |

## Ambigüedad Sintáctica

Oración:

```text
El perro mordió al hombre en el parque.
```

El sistema marca esta oración como válida y ambigua porque el complemento `en el parque` puede interpretarse de dos formas:

1. El perro realizó la acción de morder estando en el parque.
2. El hombre mordido estaba en el parque.

El frontend muestra una sección especial con dos interpretaciones estructurales y sus árboles.

## Integrantes y Responsabilidades

| Integrante | Responsabilidad |
| --- | --- |
| Brandon | Integración general, parser y conexión frontend-backend. |
| Santiago | Gramática Libre de Contexto, reglas de producción y ambigüedad. |
| Viviana | Interfaz React y visualización del árbol de derivación. |
| Justin | Lexer, tabla de tokens y lexemas. |
| Pardo | Pruebas, validaciones y documentación. |

