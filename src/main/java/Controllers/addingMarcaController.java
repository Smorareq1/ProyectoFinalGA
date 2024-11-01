package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Marca;
import Objetos.idMarca;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class addingMarcaController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField anioDeCreacionField;
    @FXML
    private TextField fundadorField;
    @FXML
    private Button añadirButton;
    @FXML
    private Button cancelarButton; // Agrega el botón de cancelar
    @FXML
    private MarcaController marcaController; // Agregar referencia al controlador de Marca

    @FXML
    public void initialize() {

        añadirButton.setOnAction(event -> {
            try {
                añadirMarca();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    private void añadirMarca() throws IOException {
        String nombre = nombreField.getText();
        String anioDeCreacion = anioDeCreacionField.getText();
        String fundador = fundadorField.getText();

        if (!nombre.isEmpty() && !anioDeCreacion.isEmpty() && !fundador.isEmpty()) {
            Marca nuevaMarca = new Marca(nombre, anioDeCreacion, fundador);

            if(GestorDeArchivos.diccionarioNombreMarcas.containsKey(nombre)){
                showAlert("Error", "La marca ya existe.");

            }else{

                idMarca.agregarMarca(nombre, nuevaMarca); // Añadir la nueva marca

                // Limpiar los campos después de añadir
                nombreField.clear();
                anioDeCreacionField.clear();
                fundadorField.clear();

                // Mensaje de éxito
                showAlert("Éxito", "Marca añadida correctamente.");
                if (marcaController != null) {
                    marcaController.actualizarTableView(); // Actualizar el TableView
                }

                cerrarVentana(); // Cerrar la ventana después de añadir
            }

        } else {
            // Mensaje de error si algún campo está vacío
            showAlert("Error", "Por favor, completa todos los campos.");
        }
    }

    // Método para mostrar un mensaje de alerta
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para cerrar la ventana
    private void cerrarVentana() {
        // Obtiene el escenario (Stage) actual y lo cierra
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();
    }
}
