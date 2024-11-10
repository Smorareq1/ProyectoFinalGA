package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class idWindowTemplateController {

    @FXML
    private Button marcasId_button;

    @FXML
    private Button LineaId_button;

    @FXML
    private Button tipoId_button;

    @FXML
    private Button vehiculoId_button;

    @FXML
    private Button VinId_button;


    @FXML
    public void initialize() {
        marcasId_button.setOnAction(e -> cargarContenidoEnEscena("/fxml/idMarcasWindow.fxml"));
        LineaId_button.setOnAction(e -> cargarContenidoEnEscena("/fxml/idLineasWindow.fxml"));
        tipoId_button.setOnAction(e -> cargarContenidoEnEscena("/fxml/idTipoWindow.fxml"));
        vehiculoId_button.setOnAction(e -> cargarContenidoEnEscena("/fxml/idVehiculosWindow.fxml"));
        VinId_button.setOnAction(e -> cargarContenidoEnEscena("/fxml/idVinWindow.fxml"));
    }

    private void cargarContenidoEnEscena(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            // Obtén el Stage actual a partir de uno de los botones
            Stage stage = (Stage) marcasId_button.getScene().getWindow();
            // Reemplaza el contenido de la escena principal con el nuevo contenido
            stage.getScene().setRoot(root);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
