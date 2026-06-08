<!--
Archivo: README.md
Descripcion: Documenta el flujo JFlex/CUP, arquitectura, comandos y responsabilidades del proyecto.
Responsable: Pardo
-->

# Procesamiento de Lenguaje Natural Basico

Proyecto academico para validar oraciones simples en espanol con una Gramatica Libre de Contexto. El sistema realiza analisis lexico, analisis sintactico, tabla token/lexema, derivacion por la izquierda, arbol de derivacion, deteccion de errores y analisis de ambiguedad.

La version actual usa **JFlex** para generar el analizador lexico y **CUP** para generar el parser sintactico.

## Flujo Principal

```text
Oracion del usuario
  -> NormalizadorOracion
  -> LexerCup generado con JFlex
  -> ParserCup generado con CUP
  -> ResultadoAnalisis
  -> DetectorAmbiguedad
  -> API REST
  -> Frontend React
```

El frontend no necesita cambios especiales porque sigue consumiendo el endpoint:

```text
POST /api/analizar
```

## Gramatica Libre de Contexto

```text
S  -> SN SV
SN -> ART SUST
SN -> PRON
SN -> SUST
SV -> V
SV -> V C
C  -> ADV
C  -> SN
C  -> PREP SN
C  -> ADV C
C  -> SN C
C  -> PREP SN C
```

## Tokens

| Token | Significado | Ejemplos |
| --- | --- | --- |
| `ART` | Articulo | `el`, `la`, `los`, `una` |
| `PRON` | Pronombre | `yo`, `tu`, `ella`, `nosotros` |
| `SUST` | Sustantivo | `perro`, `Juan`, `carta`, `parque` |
| `V` | Verbo | `corre`, `come`, `escribe`, `mordio` |
| `PREP` | Preposicion | `a`, `al`, `con`, `en`, `para` |
| `ADV` | Adverbio | `rapido`, `tranquilo`, `lentamente` |
| `ERROR` | Lexema invalido o no reconocido | `123`, `perro,`, `carta#` |

## Integracion JFlex y CUP

Los archivos editables del analizador generado son:

```text
backend/src/main/jflex/LexerCup.flex
backend/src/main/cup/ParserCup.cup
```

Maven genera automaticamente estas clases durante `generate-sources`:

```text
backend/target/generated-sources/jflex/.../LexerCup.java
backend/target/generated-sources/cup/.../ParserCup.java
backend/target/generated-sources/cup/.../TokensCup.java
```

No se deben editar manualmente los archivos dentro de `target/`, porque se regeneran al compilar.

## Arquitectura

| Capa | Responsabilidad |
| --- | --- |
| Dominio | Gramatica, entidades, JFlex/CUP, derivacion, arbol y ambiguedad. |
| Aplicacion | Caso de uso `AnalizarOracionUseCase`. |
| Puertos | Interfaces de entrada y salida. |
| Adaptadores | API REST Spring Boot y exportador JSON. |
| Frontend | Interfaz React para entrada, tokens, resultado, derivacion, arbol y ambiguedad. |

## Archivos Principales

| Archivo | Descripcion | Responsable |
| --- | --- | --- |
| `backend/pom.xml` | Configura Spring Boot, JFlex, CUP y pruebas. | Brandon |
| `backend/src/main/jflex/LexerCup.flex` | Especificacion JFlex del analizador lexico. | Justin |
| `backend/src/main/cup/ParserCup.cup` | Especificacion CUP de la gramatica sintactica. | Santiago |
| `AnalizadorJFlexCup.java` | Conecta el lexer de JFlex con el parser de CUP. | Brandon |
| `ConstructorResultadoCup.java` | Construye resultado, derivacion, arbol y errores desde CUP. | Brandon |
| `NormalizadorOracion.java` | Limpia espacios y signos externos antes del analisis. | Justin |
| `FragmentoSintactico.java` | Transporta subarboles y patrones de las reducciones CUP. | Viviana |
| `SintagmaVerbalCup.java` | Agrupa verbo y complemento reducidos por CUP. | Santiago |
| `Gramatica.java` | Carga vocabulario y valida concordancia articulo/sustantivo. | Santiago |
| `lexemas.json` | Define vocabulario base del analizador. | Justin |
| `DetectorAmbiguedad.java` | Detecta y explica casos de ambiguedad sintactica. | Santiago |
| `AnalizadorOracionTest.java` | Prueba oraciones validas, invalidas, errores, arbol y ambiguedad. | Pardo |
| `frontend/src/componentes/*.jsx` | Muestra entrada, tokens, resultado, derivacion, arbol y ambiguedad. | Viviana |
| `frontend/src/servicios/analisisServicio.js` | Conecta React con la API REST. | Brandon |

Cada archivo Java nuevo incluye cabecera con nombre, descripcion y responsable.

## Estructura

```text
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── cup/ParserCup.cup
    │   ├── jflex/LexerCup.flex
    │   ├── java/com/tfa/unidad2/
    │   │   ├── adaptadores/
    │   │   ├── aplicacion/
    │   │   ├── dominio/
    │   │   └── puertos/
    │   └── resources/
    │       ├── application.properties
    │       └── lexemas.json
    └── test/java/com/tfa/unidad2/pruebas/

frontend/
└── src/
    ├── componentes/
    ├── servicios/
    ├── App.jsx
    └── styles.css
```

## Comandos

Backend:

```powershell
cd backend
mvn spring-boot:run
```

Frontend:

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

## Pruebas

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
  "oracion": "El perro corre rapido"
}
```

## Ejemplo de Salida

```json
{
  "valida": true,
  "mensaje": "Oracion valida",
  "sujeto": "El perro",
  "verbo": "corre",
  "complemento": "rapido",
  "tokens": [
    { "lexema": "El", "token": "ART" },
    { "lexema": "perro", "token": "SUST" },
    { "lexema": "corre", "token": "V" },
    { "lexema": "rapido", "token": "ADV" }
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

## Oraciones Validar

| Validas | Invalidas |
| --- | --- |
| `El perro corre rapido.` | `Corre perro el rapido.` |
| `Juan escribe una carta.` | `El estudiante.` |
| `La profesora explica la clase.` | `La come arroz.` |
| `El perro mordio al hombre en el parque.` | `Perro el corre.` |
| `Juan vio al hombre con el telescopio.` | `Juan una carta escribe.` |

## Ambiguedad Sintactica

El sistema marca como validas y ambiguas estas oraciones:

```text
El perro mordio al hombre en el parque.
Juan vio al hombre con el telescopio.
```

La ambiguedad se analiza despues de CUP, usando la tabla de tokens ya generada por JFlex.

## Integrantes

| Integrante | Responsabilidad |
| --- | --- |
| Brandon | Integracion general, caso de uso, conexion JFlex/CUP y API. |
| Santiago | Gramatica Libre de Contexto, reglas CUP y ambiguedad. |
| Viviana | Interfaz React, visualizacion de arbol y estructuras visuales. |
| Justin | Analizador lexico JFlex, normalizacion y vocabulario. |
| Pardo | Pruebas, validaciones y documentacion. |
