package model.adt;

import model.values.Value;
import model.exceptions.MyException;

import java.util.HashMap;
import java.util.Map;

public class Heap implements IHeap {
    private Map<Integer, Value> heap;
    private int freeAddress; // next candidate free address, addresses start at 1

    public Heap() {
        this.heap = new HashMap<>();
        this.freeAddress = 1;
    }

    public Heap(Map<Integer, Value> initial) {
        this.heap = new HashMap<>(initial);
        this.freeAddress = computeNextFree();
    }

    private int computeNextFree() {
        int addr = 1;
        while (heap.containsKey(addr)) addr++;
        return addr;
    }

    @Override
    public synchronized int allocate(Value value) {
        int addr = freeAddress;
        heap.put(addr, value);
        freeAddress = computeNextFree();
        return addr;
    }

    @Override
    public synchronized Value get(int address) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Heap: invalid address " + address);
        return heap.get(address);
    }

    @Override
    public synchronized void put(int address, Value value) {
        heap.put(address, value);
        if (address == freeAddress) freeAddress = computeNextFree();
    }

    @Override
    public synchronized boolean containsKey(int address) {
        return heap.containsKey(address);
    }

    @Override
    public synchronized void update(int address, Value value) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Heap: cannot update non-existing address " + address);
        heap.put(address, value);
    }

    @Override
    public synchronized void remove(int address) throws MyException {
        if (!heap.containsKey(address))
            throw new MyException("Heap: cannot remove non-existing address " + address);
        heap.remove(address);
        if (address < freeAddress) freeAddress = address;
    }

    @Override
    public synchronized Map<Integer, Value> getContent() {
        return heap;
    }

    @Override
    public synchronized void setContent(Map<Integer, Value> content) {
        this.heap = content;
        this.freeAddress = computeNextFree();
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}
