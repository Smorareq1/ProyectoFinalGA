package Controllers;

import Objetos.GestorDeArchivos;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MenuController {

    @FXML
    private ImageView exitImage;

    @FXML
    public void initialize() {
        // Configurar el evento de clic para el ImageView
        exitImage.setOnMouseClicked((MouseEvent event) -> {
            Platform.exit(); // Cierra completamente el programa

            //Guardar los datos en un archivo
            GestorDeArchivos.guardarMarcasDatosEnJson();
            GestorDeArchivos.guardarLineasEnJson();
            GestorDeArchivos.guardarTiposEnJson();
            GestorDeArchivos.guardarVehiculosEnJson();


        });
    }

     @FXML
    // Métodos para cada botón
    public void handleInicioAction() {
        loadFXML("/fxml/Inicio.fxml");
    }

     @FXML
    public void handleMarcaAction() {
        loadFXML("/fxml/marcaWindow.fxml");
    }

     @FXML
    public void handleTipoAction() {
        loadFXML("/fxml/tipoWindow.fxml");
    }

     @FXML
    public void handleLineaAction() {
        loadFXML("/fxml/lineaWindow.fxml");
    }

     @FXML
    public void handleVehiculoAction() {
        loadFXML("/fxml/VehiculoWindow.fxml");
    }

    @FXML
    public void handleIdAction() {
        loadFXML("/fxml/idMarcasWindow.fxml");
    }

     @FXML
    // Método para cargar el FXML
    private void loadFXML(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();
            Stage stage = (Stage) exitImage.getScene().getWindow(); // Obtener la escena actual
            stage.setScene(new Scene(root)); // Cargar la nueva escena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
