module org.example.proyectofinalga {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.proyectofinalga to javafx.fxml;
    exports org.example.proyectofinalga;
}