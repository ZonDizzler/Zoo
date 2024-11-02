module com.johndistler.zoofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires java.sql.Connection;

    opens com.johndistler.zoofinal to javafx.fxml, com.google.gson, java.sql;


    exports com.johndistler.zoofinal;
    requires com.google.gson;
}
