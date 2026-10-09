package repository;

import model.PrgState;
import model.exceptions.MyException;
import java.util.List;

public interface IRepository {
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> list);
    void addPrgState(PrgState prg);
    void logPrgStateExec(PrgState prg) throws MyException;
}