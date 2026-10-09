package repository;

import model.statement.Statement;
import state.ProgramState;

import java.util.List;

public interface Repository {
    //ProgramState getCurrentProgramState();
    void addProgramState(ProgramState programState);
    void removeProgramState(ProgramState programState);
    void logProgramStateExecution(ProgramState programState);
    List<ProgramState> getProgramStates();
    void setProgramList(List<ProgramState> programStates);

}
