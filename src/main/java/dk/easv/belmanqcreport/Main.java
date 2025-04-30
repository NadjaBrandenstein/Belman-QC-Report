package dk.easv.belmanqcreport;

import dk.easv.belmanqcreport.BLL.UTIL.PDFGenerator;
import dk.easv.belmanqcreport.BLL.UTIL.PDFGeneratorImp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/dk/easv/belmanqcreport/FXML/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenBounds.getWidth(), screenBounds.getHeight());

        stage.setTitle("Belman");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch(args);}
}