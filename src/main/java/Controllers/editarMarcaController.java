package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Marca;
import Objetos.idMarca;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
        editarButton.setOnAction(event -> {
            try {
                editarMarca();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    private void editarMarca() throws IOException {
        // Get the new values from the text fields
        String nuevoNombre = nombreField.getText();
        String nuevoAnioDeCreacion = anioDeCreacionField.getText();
        String nuevoFundador = fundadorField.getText();

        // Check if the fields are not empty
        if (!nuevoNombre.isEmpty() && !nuevoAnioDeCreacion.isEmpty() && !nuevoFundador.isEmpty()) {
            // Create a new Marca object with updated values
            Marca nuevaMarca = new Marca(nuevoNombre, nuevoAnioDeCreacion, nuevoFundador);

            //Editar las lineas que tengan esta marca
            GestorDeArchivos.editarLineasPorMarca(nombreAEditar, nuevaMarca);

            //Editar los vehiculos que tengan esta marca
            GestorDeArchivos.editarVehiculosPorMarca(nombreAEditar, nuevaMarca);

            // Quita la marca anterior y agrega la nueva
            GestorDeArchivos.diccionarioNombreMarcas.remove(nombreAEditar);
            GestorDeArchivos.diccionarioNombreMarcas.put(nuevoNombre, nuevaMarca);


            // Editar la marca en los datos
            idMarca.editarMarcaEnDatos(nombreAEditar, nuevaMarca);

            // Show a success message
            showAlert("Éxito", "Marca editada correctamente.");

            if (marcaController != null) {
                marcaController.actualizarTableView();
            }

            cerrarVentana();

        } else {
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
