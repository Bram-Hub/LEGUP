import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow
{

    private Stage primaryStage;
    private static final String fxmlLocation = "legup/main_window.fxml";

    public MainWindow(Stage stage)
    {
        this.primaryStage = stage;
    }

    public void show() throws IOException
    {
        System.err.println(MainWindow.class.getResource(fxmlLocation));
        FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource(fxmlLocation));
        BorderPane root = loader.load();
        Scene scene = new Scene(root,  1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}