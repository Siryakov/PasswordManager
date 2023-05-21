module com.example.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.passwordmanager to javafx.fxml;
    exports com.example.passwordmanager;
}