package model.statements;

public class SyncForkStmt {
        private final IStmt stmt;

        public SyncForkStmt(IStmt stmt) {
            this.stmt = stmt;
        }

        @Override
        public String toString() {
            return "syncFork(" + stmt.toString() + ")";
        }

}
