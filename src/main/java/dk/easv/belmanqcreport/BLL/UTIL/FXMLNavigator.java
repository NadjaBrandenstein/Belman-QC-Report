package dk.easv.belmanqcreport.BLL.UTIL;

import dk.easv.belmanqcreport.Main;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLNavigator {

    // 1. Private static instance (eager initialization is fine here)
    private static final FXMLNavigator instance = new FXMLNavigator();

    // 2. Private constructor to prevent external instantiation
    private FXMLNavigator() {}

    // 3. Public method to access the singleton instance
    public static FXMLNavigator getInstance() {
        return instance;
    }

    // 4. Instance method instead of static
    public FXMLLoader navigateTo(Stage stage, String fxmlRelativePath) {
        try {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/" + fxmlRelativePath));
            Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

            stage.getIcons().add(new Image("/dk/easv/belmanqcreport/Icons/Belman.png"));
            stage.setTitle("Belman");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            return fxmlLoader;

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
