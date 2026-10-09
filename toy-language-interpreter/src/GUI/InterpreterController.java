package GUI;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.PrgState;
import model.exceptions.MyException;
import model.values.Value;
import model.statements.IStmt;
import model.adt.Pair;

import java.util.*;
import java.util.Map;

public class InterpreterController {
    private Controller controller;

    @FXML private TextField nrPrgStatesField;
    @FXML private TableView<HeapEntry> heapTable;
    @FXML private TableColumn<HeapEntry, Integer> addrCol;
    @FXML private TableColumn<HeapEntry, String> valCol;
    @FXML private ListView<String> outListView;
    @FXML private ListView<String> fileTableView;
    @FXML private ListView<Integer> prgIdsListView;
    @FXML private TableView<SymEntry> symTableView;
    @FXML private TableColumn<SymEntry, String> varCol;
    @FXML private TableColumn<SymEntry, String> svalCol;
    @FXML private ListView<String> exeStackListView;
    @FXML private Button runStepBtn;

    @FXML
    public void initialize() {
        if (addrCol != null) addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        if (valCol != null) valCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        if (varCol != null) varCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (svalCol != null) svalCol.setCellValueFactory(new PropertyValueFactory<>("value"));


        // react to selection changes (also covers keyboard selection)
        if (prgIdsListView != null) {
            prgIdsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateSymAndStack(newVal));
        }
    }

    public void setController(Controller ctr) {
        this.controller = ctr;
        try {
            this.controller.initExecutor();
        } catch (Exception ignored) {}
        updateAllViews();
    }

    @FXML
    public void handleRunOneStep() {
        try {
            // if no programs left, inform the user and disable the button
            List<PrgState> prgList = controller.getRepo().getPrgList();
            if (prgList == null || prgList.isEmpty()) {
                showAlert("Execution finished", "There are no more programs to execute.");
                runStepBtn.setDisable(true);
                return;
            }

            controller.stepOnceGui();
            updateAllViews();
        } catch (MyException e) {
            showAlert("Execution error", e.getMessage());
        }
    }

    @FXML
    public void handlePrgIdSelect() {
        Integer id = prgIdsListView.getSelectionModel().getSelectedItem();
        updateSymAndStack(id);
    }

    private void updateAllViews() {
        if (controller == null) return;
        List<PrgState> prgList = controller.getRepo().getPrgList();
        if (prgList == null) prgList = Collections.emptyList();
        nrPrgStatesField.setText(String.valueOf(prgList.size()));

        // disable run button when no programs to run
        if (runStepBtn != null) runStepBtn.setDisable(prgList.isEmpty());

        // Aggregate heap across all program states (heap is shared in the assignment, but be defensive)
        Map<Integer, Value> heap = new HashMap<>();
        for (PrgState p : prgList) {
            if (p == null) continue;
            Map<Integer, Value> h = p.getHeap().getContent();
            if (h != null) heap.putAll(h);
        }
        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Value> e : heap.entrySet()) heapEntries.add(new HeapEntry(e.getKey(), e.getValue().toString()));
        heapTable.setItems(heapEntries);



        // Aggregate output from all programs (append in order)
        List<String> outsAll = new ArrayList<>();
        for (PrgState p : prgList) {
            if (p == null) continue;
            List<Value> out = p.getOut().getAll();
            for (Value v : out) outsAll.add(v.toString());
        }
        outListView.setItems(FXCollections.observableArrayList(outsAll));

        // Aggregate file table keys from all programs (unique)
        Set<String> files = new LinkedHashSet<>();
        for (PrgState p : prgList) {
            if (p == null) continue;
            Map<?, ?> ft = p.getFileTable().getContent();
            if (ft != null) for (Object k : ft.keySet()) files.add(k.toString());
        }
        fileTableView.setItems(FXCollections.observableArrayList(files));

        // Program IDs list
        ObservableList<Integer> ids = FXCollections.observableArrayList();
        for (PrgState p : prgList) if (p != null) ids.add(p.getId());
        prgIdsListView.setItems(ids);
        if (!ids.isEmpty()) prgIdsListView.getSelectionModel().selectFirst();
        Integer sel = prgIdsListView.getSelectionModel().getSelectedItem();
        updateSymAndStack(sel);
    }

    private void updateSymAndStack(Integer prgId) {
        if (controller == null) return;
        if (prgId == null) {
            symTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }
        PrgState selected = null;
        for (PrgState p : controller.getRepo().getPrgList()) if (p.getId() == prgId) selected = p;
        if (selected == null) return;

        Map<String, Value> sym = selected.getSymTable().getContent();
        ObservableList<SymEntry> symEntries = FXCollections.observableArrayList();
        for (Map.Entry<String, Value> e : sym.entrySet()) symEntries.add(new SymEntry(e.getKey(), e.getValue().toString()));
        symTableView.setItems(symEntries);

        List<IStmt> stackAll = selected.getStk().getAll();
        List<String> stackStr = new ArrayList<>();
        for (int i = stackAll.size() - 1; i >= 0; i--) stackStr.add(stackAll.get(i).toString());
        exeStackListView.setItems(FXCollections.observableArrayList(stackStr));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class HeapEntry {
        private final Integer address;
        private final String value;
        public HeapEntry(Integer address, String value) { this.address = address; this.value = value; }
        public Integer getAddress() { return address; }
        public String getValue() { return value; }
    }

    public static class SymEntry {
        private final String name;
        private final String value;
        public SymEntry(String name, String value) { this.name = name; this.value = value; }
        public String getName() { return name; }
        public String getValue() { return value; }
    }

}
