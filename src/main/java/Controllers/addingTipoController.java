package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Tipo;
import Objetos.idTipo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class addingTipoController {

    @FXML
    private TextField nombreTipoField;
    @FXML
    private TextField anioTipoField;
    @FXML
    private Button añadirButton;
    @FXML
    private Button cancelarButton;
    @FXML
    private TipoController tipoController;

    @FXML
    public void initialize() {
        añadirButton.setOnAction(event -> {
            try {
                añadirTipo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            });
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    private void añadirTipo() throws IOException {
        String nombre = nombreTipoField.getText();
        String anio = anioTipoField.getText();

        if (!nombre.isEmpty() && !anio.isEmpty()) {
            Tipo nuevoTipo = new Tipo(nombre);

            if(GestorDeArchivos.diccionarioNombreTipos.containsKey(nombre)){
                showAlert("Error", "El tipo ya existe.");

            }
            else{
                idTipo.agregarTipo(nombre, nuevoTipo);

                nombreTipoField.clear();
                anioTipoField.clear();

                // Mensaje de éxito
                showAlert("Éxito", "Tipo añadido correctamente.");
                if (tipoController != null) {
                    tipoController.actualizarTableView();
                }

                // Cerrar ventana después de añadir
                cerrarVentana();
            }

        } else {
            showAlert("Error", "Por favor, completa todos los campos.");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
