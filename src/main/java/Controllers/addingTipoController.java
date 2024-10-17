package Controllers;

import Objetos.Tipo;
import javafx.fxml.FXML;
import Objetos.GestorDeArchivos;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
        añadirButton.setOnAction(event -> añadirTipo());
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    private void añadirTipo() {
        String nombre = nombreTipoField.getText();
        String anio = anioTipoField.getText();

        if (!nombre.isEmpty() && !anio.isEmpty()) {
            Tipo nuevoTipo = new Tipo(nombre, anio);

            if(GestorDeArchivos.diccionarioNombreTipos.containsKey(nombre)){
                showAlert("Error", "El tipo ya existe.");

            }else{
                GestorDeArchivos.diccionarioNombreTipos.put(nombre, nuevoTipo);

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
