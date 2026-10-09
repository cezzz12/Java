package state;

import model.value.StringValue;
import model.value.Value;
import state.exceptions.FileNameNotFoundException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements FileTable {
    Map<Value, BufferedReader> mapFileTable = new HashMap<>();
    @Override
    public BufferedReader lookUp(Value key) {
        if(mapFileTable.containsKey(key)) {
            return mapFileTable.get(key);
        }
        return null;
    }

    @Override
    public void addFile(Value key, BufferedReader bufferedReader) {
        String filePath = key.getValue().toString();
        mapFileTable.put(key, new BufferedReader(bufferedReader));
    }

    @Override
    public void removeFile(Value key) {
        mapFileTable.remove(key);
    }
    @Override
    public Boolean fileExists(Value key) {
        return mapFileTable.containsKey(key);
    }
    @Override
    public String toString() {
        return "MapFileTable " + mapFileTable.toString();
    }
    public Map<Value, BufferedReader> getContents() {
        return mapFileTable;
    }
}
