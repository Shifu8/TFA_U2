# Procesamiento de Lenguaje Natural Básico

Proyecto académico para validar si una oración en español cumple la estructura:

```text
Sujeto + Verbo (+ Complemento)
```

El sistema realiza análisis léxico, análisis sintáctico, tabla token/lexema, derivación por la izquierda, árbol de derivación e identificación de sujeto, verbo y complemento opcional. La validación se centra en la estructura gramatical, no en la coherencia semántica.

La visualización toma como referencia el flujo de análisis del repositorio `Shifu8/APE8_Automatas`: entrada, tabla de tokens, derivación y árbol sintáctico.

## Escenario

Un chatbot necesita validar oraciones simples en español antes de procesarlas. Para ello, este proyecto implementa una Gramática Libre de Contexto que reconoce sintagmas nominales, verbos y complementos, y entrega una explicación clara cuando una oración es inválida.

## Arquitectura Hexagonal

| Capa | Responsabilidad |
| --- | --- |
| Dominio | Gramática, reglas, lexer, parser, árbol, resultado y detector de ambigüedad. |
| Aplicación | Caso de uso `AnalizarOracionUseCase`. |
| Puertos | Interfaces de entrada y salida. |
| Adaptadores | API REST Spring Boot y exportador JSON. |

## Estructura

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
SV -> V | V C
C  -> ADV | SN | PREP SN | ADV C | SN C | PREP SN C
```

## Significado De Tokens

| Token | Significado | Ejemplos |
| --- | --- | --- |
| `S` | Símbolo inicial de la oración | `SN SV` |
| `SN` | Sintagma nominal, funciona como sujeto o parte del complemento | `El perro`, `Juan`, `nosotros` |
| `SV` | Sintagma verbal, contiene verbo y puede incluir complemento | `come`, `corre rápido` |
| `ART` | Artículo | `el`, `la`, `un`, `una` |
| `SUST` | Sustantivo | `perro`, `niña`, `arroz`, `parque` |
| `PRON` | Pronombre | `yo`, `él`, `nosotros`, `ellos` |
| `V` | Verbo | `corre`, `come`, `escribe`, `mordió` |
| `PREP` | Preposición | `en`, `al`, `con`, `de`, `para` |
| `ADV` | Adverbio | `rápido`, `tranquilo`, `lentamente` |
| `C` | Complemento del verbo | `rápido`, `una carta`, `al hombre` |

## Derivación Por La Izquierda

El sistema usa derivación por la izquierda, es decir, en cada paso expande el primer no terminal que aparece más a la izquierda.

Ejemplo:

```text
S
SN SV
ART SUST SV
ART SUST V C
ART SUST V ADV
```

Para una oración con complemento preposicional doble:

```text
S
SN SV
ART SUST SV
ART SUST V C
ART SUST V PREP SN PREP SN
ART SUST V PREP SUST PREP SN
ART SUST V PREP SUST PREP ART SUST
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

## Ejemplo De Entrada

```json
{
  "oracion": "El perro corre rápido"
}
```

## Ejemplo De Salida

```json
{
  "valida": true,
  "mensaje": "Oración válida",
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

El sistema marca esta oración como válida y ambigua porque `en el parque` puede interpretarse de dos formas:

1. El perro realizó la acción de morder estando en el parque.
2. El hombre mordido estaba en el parque.

Otro ejemplo:

```text
Juan vio al hombre con el telescopio.
```

La frase `con el telescopio` puede interpretarse de dos formas:

1. Juan usó el telescopio para ver al hombre.
2. El hombre observado tenía el telescopio.

El frontend muestra una sección especial con dos interpretaciones estructurales y sus árboles.

## Integrantes Y Responsabilidades

| Integrante | Responsabilidad |
| --- | --- |
| Brandon | Integración general, parser y conexión frontend-backend. |
| Santiago | Gramática Libre de Contexto, reglas de producción y ambigüedad. |
| Viviana | Interfaz React y visualización del árbol de derivación. |
| Justin | Lexer, tabla de tokens y lexemas. |
| Pardo | Pruebas, validaciones y documentación. |
