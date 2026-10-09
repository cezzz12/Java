# Parallel vector addition benchmark

A Java benchmark comparing sequential vector addition with cyclic and contiguous-block work partitioning.

## How it works

`Main` creates arrays, initializes input, times each approach and validates every result. `CyclicThread` assigns strided indexes to workers; `BlockThread` assigns contiguous index ranges. The launcher accepts array size and worker count, including sizes not evenly divisible by workers.

## Architecture

AST/program state → execution controller → repository/logs → console or JavaFX view.

## Build and run

Requires a JDK (tested with JDK 25). From this folder:

```sh
javac -d build src/*.java
java -cp build Main 1000000 8
```

The optional arguments are array size and worker count. A smaller verification run is `java -cp build Main 10003 3`.

## Verification

Checked on 2026-10-09: build: passed, runtime: passed.

## Refinements

- Made benchmark size/thread count configurable with a practical default and used the actual array length for block partitioning.

