package state;

import model.value.Value;

import java.io.BufferedReader;
import java.util.Map;

public interface FileTable {
    BufferedReader lookUp(Value key);
    void addFile(Value key, BufferedReader bufferedReader);
    void removeFile(Value key);
    Boolean fileExists(Value key);
    public Map<Value, BufferedReader> getContents();
}
