# Filmsamling

Et CRUD-program til en filmsamling - 1. semester, datamatiker, EK. 

Filmsamlingen er en digital oversigt over din personlige filmsamling.
Programmet gør det muligt at oprette, vise, søge efter redigere og slette film samt registrere, hvornår de sidst er set. 

Projektet udvikles som et terminalprogram i Java med fokus på objektorienteret programmering, unit testing, fejlhåndtering, fillagring og sortering.

## Gruppen
| Fornavn | GitHub                                        |
|---------|-----------------------------------------------|
| Nikita  | [NikitaM2527](https://github.com/NikitaM2527) |
| Mathias | [mwnDK1402](https://github.com/mwnDK1402)     |
| Nikolas | [NikolasCCT](https://github.com/NikolasCCT)   |

## Kør programmet

Terminalprogrammet er det egentlige program:

```sh
mvn compile exec:java -Dexec.mainClass=Main
```

## Browser-version

`docs/` indeholder en ekstra version, der kører det samme program i browseren
via TeaVM. Den er kun et ekstra lag omkring programmet og ændrer ikke ved den
almindelige terminaludgave.

Efter ændringer i Java-koden skal browser-versionen genbygges og kopieres:

```sh
JAVA_HOME=/usr/lib/jvm/java-21-openjdk mvn package
cp target/teavm/classes.wasm target/teavm/classes.wasm-runtime.js target/teavm/classes.wasm.map docs/teavm/
```

Test lokalt med:

```sh
python3 -m http.server 8010 -d docs
```