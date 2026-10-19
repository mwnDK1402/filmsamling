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
mvn compile exec:java -Dexec.mainClass=org.example.Main
```

## Arkitektur: terminal-IO bag et interface

Al programlogik ligger i `FilmSamlingApp` og kender kun interfacet
`org.example.terminal.Terminal` (`readLine`, `print`, `println`). Programmets
kode indeholder derfor ingen `Scanner`, `System.in` eller `System.out`.

Der findes to implementeringer af interfacet:

| Klasse | Bruges af | Læser input via |
|--------|-----------|-----------------|
| `ConsoleTerminal` | `Main` (desktop) | `Scanner(System.in)` |
| `WebTerminal` | `BrowserMain` (browser) | xterm-terminalen i `docs/index.html` |

De to indgangspunkter (`Main` og `BrowserMain`) vælger hver sin implementering og
starter det samme `FilmSamlingApp`. Forskellen mellem desktop og browser er
altså kun hvilken terminal der sættes ind — ikke programmets opførsel.

Browseren kan ikke blokere, så `WebTerminal.readLine()` kompileres af TeaVM til
en coroutine: kaldet "venter" uden at blokere siden, og genoptages når brugeren
trykker Enter. Set fra `FilmSamlingApp` opfører den sig som et almindeligt
blokerende kald.

Den eneste browser-specifikke kode er `WebTerminal` samt `WebInput`, som siden
kalder for at aflevere indtastede linjer.

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