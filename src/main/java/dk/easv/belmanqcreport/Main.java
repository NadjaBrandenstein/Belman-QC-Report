package dk.easv.belmanqcreport;
// JavaFX imports
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
//Java Imports
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

        stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
        stage.setTitle("Belman");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {launch(args);}
}