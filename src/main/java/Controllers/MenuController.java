package Controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

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
