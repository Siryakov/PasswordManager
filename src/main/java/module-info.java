module com.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens PM to javafx.fxml;
    exports PM;
}