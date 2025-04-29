module dk.easv.belmanqcreport {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;


    opens dk.easv.belmanqcreport to javafx.fxml;
    exports dk.easv.belmanqcreport;
}