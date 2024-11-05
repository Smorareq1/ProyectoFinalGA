package Controllers;

import Objetos.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            // vehiculos que tengan esta linea
            List<Vehiculo> vehiculosAEditarAntiguos = new ArrayList<>();
            for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
                if (vehiculo.getLinea().getNombreLinea().equals(lineaAnterior.getNombreLinea())) {
                    vehiculosAEditarAntiguos.add(vehiculo);
                }
            }

            List<Vehiculo> vehiculosAfectados = vehiculosAfectados(lineaAnterior.getNombreLinea());

            GestorDeArchivos.editarVehiculosDadoLineas(lineaAEditar.getNombreLinea(), new Linea(lineaAEditar.getMarca(), nuevoNombreLinea, nuevoAnioLinea));

            // Actualizar los valores en la Linea original
            lineaAEditar.setNombreLinea(nuevoNombreLinea);  // Actualizar el nombre
            lineaAEditar.setAnioLinea(nuevoAnioLinea);      // Actualizar el año

            GestorDeArchivos.diccionarioNombreLineas.remove(lineaAnterior.getNombreLinea());
            GestorDeArchivos.diccionarioNombreLineas.put(nuevoNombreLinea, lineaAEditar);

            idLinea.editarLineaEnDatos(lineaAnterior.getNombreLinea(), new Linea(lineaAEditar.getMarca(), nuevoNombreLinea, nuevoAnioLinea), lineaAnterior);

            //Editar vehiculos que tengan esta linea
            int i = 0;
            for (Vehiculo vehiculo : vehiculosAfectados) {
                // Actualizamos la línea del vehículo a los nuevos valores
                vehiculo.setLinea(lineaAEditar);  // Asignar la línea editada al vehículo
                vehiculo.setLineaNombreVehiculo(lineaAEditar.getNombreLinea());
                // Editamos cada vehículo en los datos usando el vehículo actualizado y el antiguo
                idVehiculos.editarVehiculosEnDatos(vehiculo.getPlaca(), vehiculo, vehiculosAEditarAntiguos.get(i));
                i++;
            }



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

     private static List<Vehiculo> vehiculosAfectados(String nombreLinea){
        List<Vehiculo> vehiculosAfectados = new ArrayList<>();
        for (Map.Entry<String, Vehiculo> entry : GestorDeArchivos.diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();

            // Verificamos si el nombre de la marca en el vehículo coincide con el que estamos buscando
            if (vehiculo.getLinea().getNombreLinea().equals(nombreLinea)) {
                // Agregamos el vehículo a la lista de vehículos afectados sin modificarlo
                vehiculosAfectados.add(vehiculo);
            }
        }
        return vehiculosAfectados;
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







