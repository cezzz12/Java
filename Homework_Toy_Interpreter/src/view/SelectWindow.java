package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.statement.Statement;
import repository.ListRepository;
import state.*;

import java.util.List;

public class SelectWindow {
    private List<Statement> programs;

    public SelectWindow(List<Statement> programs) {
        this.programs = programs;
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Select Program");

        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Select a program to execute:"));

        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(
                programs.stream().map(Object::toString).toList()
        ));
        layout.getChildren().add(listView);

        Button selectButton = new Button("Select Program");
        selectButton.setOnAction(e -> {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Statement selectedStatement = programs.get(selectedIndex);
                
                // Initialize Controller and MainWindow
                ProgramState prg = new ProgramState(
                        new ListExecutionStack(),
                        new MapSymbolTable(),
                        new ListOut(),
                        new MapFileTable(),
                        new HeapMap());
                prg.executionStack().push(selectedStatement);
                
                Controller controller = new Controller(new ListRepository("log_gui.txt"));
                controller.addProgramState(prg);
                
                MainWindow mainWindow = new MainWindow(controller);
                mainWindow.show();
                stage.close();
            }
        });
        layout.getChildren().add(selectButton);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
