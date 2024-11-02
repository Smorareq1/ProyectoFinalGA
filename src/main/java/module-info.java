module org.example.proyectofinalga {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires kernel;
    requires layout;


    exports Controllers;
    opens Controllers to javafx.fxml;

    exports Clases;
    opens Clases to javafx.fxml;

    opens Objetos to javafx.base, com.google.gson;


}