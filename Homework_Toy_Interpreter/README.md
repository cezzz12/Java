# Toy language interpreter

An interpreter for a small typed language with expressions, control flow, references/heap operations and forked program execution, exposed through console/JavaFX views.

## How it works

`model/` contains expressions, statements, types and values. `state/` holds execution stacks, symbol tables, output, file tables and heap. `controller/` drives small execution steps over active programs and garbage collection. `repository/` stores active programs and execution logs. `view/` contains JavaFX presentation.

## Architecture

AST/program state → execution controller → repository/logs → console or JavaFX view.

## Build and run

Requires JDK 25 and Maven. JavaFX dependencies are resolved through Maven. From this folder:

```sh
mvn verify
mvn javafx:run
```

The POM defaults to Windows JavaFX libraries. On Linux or macOS pass `-Djavafx.platform=linux` or `-Djavafx.platform=mac`. Use `mac-aarch64` for an Apple Silicon JavaFX classifier when applicable.

The included heap examples can run without opening the GUI:

```sh
java -cp target/classes test.TestHeap
```

These are demonstration checks; they print execution/expected results rather than a formal assertion suite.

## Verification

Checked on 2026-10-09: build: passed, runtime: passed.

## Refinements

- Configured Maven to compile the actual src directory instead of an empty default source tree.
- Made the file-reading demonstration use the bundled relative test input.

