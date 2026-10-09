package model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    private final List<T> list;

    public MyList() { list = new ArrayList<>(); }

    @Override
    public void add(T v) { list.add(v); }

    @Override
    public List<T> getAll() { return new ArrayList<>(list); }

    @Override
    public String toString() { return list.toString(); }
}
