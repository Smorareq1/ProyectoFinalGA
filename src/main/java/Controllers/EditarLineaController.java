package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Linea;
import Objetos.Marca;
import Objetos.idLinea;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditarLineaController {

    @FXML
    private TextField lineaTextField;

    @FXML
    private TextField anioTextField;

    @FXML
    private Button editarButton;

    @FXML
    private Button cancelarButton;

    private Linea lineaAEditar; // Este será el objeto que se editará

    private LineaController lineaController;

    @FXML
    public void initialize() {
        editarButton.setOnAction(event -> {
            try {
                editarLinea();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    public void setLineaAEditar(Linea linea) {
        this.lineaAEditar = linea;
        // Set the text fields with the current values of the selected Linea
        lineaTextField.setText(linea.getNombreLinea());
        anioTextField.setText(linea.getAnioLinea());
    }

    private void editarLinea() throws IOException {
        // Obtener los nuevos valores desde los campos de texto
        String nuevoNombreLinea = lineaTextField.getText();
        String nuevoAnioLinea = anioTextField.getText();

        // Verificar que los campos no estén vacíos
        if (!nuevoNombreLinea.isEmpty() && !nuevoAnioLinea.isEmpty()) {

             Linea lineaAnterior = new Linea (lineaAEditar.getMarca(), lineaAEditar.getNombreLinea(), lineaAEditar.getAnioLinea());

            GestorDeArchivos.printLineasDeVehiculos();
            GestorDeArchivos.editarVehiculosDadoLineas(lineaAEditar.getNombreLinea(), new Linea(lineaAEditar.getMarca(), nuevoNombreLinea, nuevoAnioLinea));
            GestorDeArchivos.printLineasDeVehiculos();

            // Actualizar los valores en la Linea original
            lineaAEditar.setNombreLinea(nuevoNombreLinea);  // Actualizar el nombre
            lineaAEditar.setAnioLinea(nuevoAnioLinea);      // Actualizar el año

            GestorDeArchivos.diccionarioNombreLineas.remove(lineaAnterior.getNombreLinea());
            GestorDeArchivos.diccionarioNombreLineas.put(nuevoNombreLinea, lineaAEditar);

            idLinea.editarLineaEnDatos(lineaAnterior.getNombreLinea(), new Linea(lineaAEditar.getMarca(), nuevoNombreLinea, nuevoAnioLinea), lineaAnterior);


            // Actualizar el TableView en el controlador principal
            if (lineaController != null) {
                lineaController.actualizarTableView();
            }

            // Mostrar un mensaje de éxito
            showAlert("Éxito", "Línea editada correctamente.");
            cerrarVentana();  // Cerrar la ventana de edición
        } else {
            // Mostrar un mensaje de error si los campos están vacíos
            showAlert("Error", "Por favor, completa todos los campos.");
        }
    }

    public void setLineaController(LineaController controller) {
        this.lineaController = controller;
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







