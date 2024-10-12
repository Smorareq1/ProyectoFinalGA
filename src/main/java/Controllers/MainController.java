package Controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.IOException;

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

    @FXML
    private void gestionarButtonAction(ActionEvent event) {
        try {
            // Cargar la segunda ventana (Menu)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Inicio.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Crear una nueva ventana (Stage) para la escena del Menu
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();

            // Cerrar la ventana actual (ventanaPrincipal)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}