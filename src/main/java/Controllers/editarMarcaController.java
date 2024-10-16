package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Marca;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editarMarcaController {

    @FXML
    private Button editarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private TextField nombreField;

    @FXML
    private TextField anioDeCreacionField;

    @FXML
    private TextField fundadorField;

    @FXML
    private MarcaController marcaController;

    private static String nombreAEditar;
    private static String anioDeCreacionAEditar;
    private static String fundadorAEditar;


    public static void InformacionAEditar(String nombre, String anioDeCreacion, String fundador)
    {
        nombreAEditar = nombre;
        anioDeCreacionAEditar = anioDeCreacion;
        fundadorAEditar = fundador;
    }

    // This method is called automatically when the FXML file is loaded
    @FXML
    public void initialize() {
        // Set the values of the text fields to the corresponding data
        nombreField.setText(nombreAEditar);
        anioDeCreacionField.setText(anioDeCreacionAEditar);
        fundadorField.setText(fundadorAEditar);

        // Set the action of the button
        editarButton.setOnAction(event -> editarMarca());
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    private void editarMarca() {
        // Get the new values from the text fields
        String nuevoNombre = nombreField.getText();
        String nuevoAnioDeCreacion = anioDeCreacionField.getText();
        String nuevoFundador = fundadorField.getText();

        // Check if the fields are not empty
        if (!nuevoNombre.isEmpty() && !nuevoAnioDeCreacion.isEmpty() && !nuevoFundador.isEmpty()) {
            // Create a new Marca object with updated values
            Marca nuevaMarca = new Marca(nuevoNombre, nuevoAnioDeCreacion, nuevoFundador);

            // Remove the old entry and add the updated Marca to the dictionary
            GestorDeArchivos.diccionarioNombreMarcas.remove(nombreAEditar);
            GestorDeArchivos.diccionarioNombreMarcas.put(nuevoNombre, nuevaMarca);

            // Show a success message
            //showAlert("Éxito", "Marca editada correctamente.");

            if (marcaController != null) {
                marcaController.actualizarTableView(); // Actualizar el TableView
            }

            cerrarVentana(); // Close the window after editing

        } else {
            // Show an error message if any field is empty
            showAlert("Error", "Por favor, completa todos los campos.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        // Close the current window
        Stage stage = (Stage) editarButton.getScene().getWindow();
        stage.close();
    }
}
