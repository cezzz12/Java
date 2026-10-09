# Toy language interpreter

An interpreter for a small typed language with expressions, control flow, references/heap operations and forked program execution, exposed through console/JavaFX views.

## How it works

`model/` contains expressions, statements, types and values. `model/adt` and `PrgState` hold execution stacks, symbol tables, output and heap. `controller/` drives small execution steps over active programs and garbage collection. `repository/` stores active programs and execution logs. `view/` contains the console command menu and `GUI/` contains JavaFX controllers/FXML views. Additional language statements in this version: `FORstmt`.

## Architecture

AST/program state → execution controller → repository/logs → console or JavaFX view.

## Build and run

Requires JDK 17 or newer and Maven; verified with JDK 25. JavaFX dependencies are resolved through Maven. From this folder:

```sh
mvn verify
mvn javafx:run
```

The POM defaults to Windows JavaFX libraries. On Linux or macOS pass `-Djavafx.platform=linux` or `-Djavafx.platform=mac`. Use `mac-aarch64` for an Apple Silicon JavaFX classifier when applicable.

For the console interface after building:

```sh
java -cp target/classes Main
```

Choose an example to run; enter `0` to exit. The simplest example declares `v`, assigns 2, and prints it. Execution snapshots are written to log files.

## Verification

Checked on 2026-10-09: build: passed, runtime: passed.

The console smoke check executed the first sample program; JavaFX views and every language construct were not individually exercised.

## Refinements

- Replaced machine-specific Java launch helpers with a Maven launcher that uses the caller's configured JDK.

