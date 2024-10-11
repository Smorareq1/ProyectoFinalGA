package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class MainController {
    @FXML
    private ImageView exitImage;

    @FXML
    public void initialize() {
        // Configurar el evento de clic para el ImageView
        exitImage.setOnMouseClicked((MouseEvent event) -> {
            Platform.exit(); // Cierra completamente el programa
        });
    }
}