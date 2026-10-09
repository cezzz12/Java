package model.adt;

import model.exceptions.ADTException;
import java.util.List;

public interface MyIStack<T> {
    T pop() throws ADTException;
    void push(T v);
    boolean isEmpty();
    List<T> getAll(); // for debug / printing
    String toString();
}
