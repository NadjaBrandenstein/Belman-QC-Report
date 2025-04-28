package dk.easv.belmanqcreport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Operator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());
        stage.setTitle("Belman");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}