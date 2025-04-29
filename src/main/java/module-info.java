module dk.easv.belmanqcreport {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens dk.easv.belmanqcreport to javafx.fxml;
    exports dk.easv.belmanqcreport;
}