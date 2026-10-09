package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/ProgramSelector.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Program Selector");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//RUN COMMAND:
// cmd /c "cd /d "C:\Users\Cezar\JavaProj\toy-language-interpreter-cond" && run-with-java-home.bat"