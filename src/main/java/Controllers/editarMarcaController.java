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


            //Editar datos de las lineas que tengan esta marca
            List<Linea> lineasAEditarAntiguas = new ArrayList<>();
            for (Linea linea : GestorDeArchivos.diccionarioNombreLineas.values()) {
                if (linea.getMarca().getNombre().equals(nombreAEditar)) {
                    lineasAEditarAntiguas.add(linea);
                }
            }
            List<Linea> lineasAEditarNuevas = lineasAfectadas(nombreAEditar);

            GestorDeArchivos.editarLineasPorMarca(nombreAEditar, nuevaMarca);

            //Fin de edicion de lineas por marca

            //Editar datos de vehiculo que tengan esta marca
            List<Vehiculo> vehiculosAEditar = new ArrayList<>();
            for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
                if (vehiculo.getMarca().getNombre().equals(nombreAEditar)) {
                    vehiculosAEditar.add(vehiculo);
                }
            }

            List<Vehiculo> vehiculosAfectados = vehiculosAfectados(nombreAEditar);

            GestorDeArchivos.editarVehiculosPorMarca(nombreAEditar, nuevaMarca);

            //Fin de edicion de vehiculos por marca

            // Quita la marca anterior y agrega la nueva
            Marca marcaAnterior = GestorDeArchivos.diccionarioNombreMarcas.get(nombreAEditar);
            GestorDeArchivos.diccionarioNombreMarcas.remove(nombreAEditar);
            GestorDeArchivos.diccionarioNombreMarcas.put(nuevoNombre, nuevaMarca);


            // Editar la marca en los datos
            idMarca.editarMarcaEnDatos(nombreAEditar, nuevaMarca, marcaAnterior);

            // Editar las líneas en los datos
           int i = 0; // Contador manual
            for (Linea linea : lineasAEditarNuevas) {
                // Editamos cada línea en datos usando los valores correspondientes de ambas listas
                idLinea.editarLineaEnDatos(linea.getNombreLinea(), linea, lineasAEditarAntiguas.get(i));
                i++; // Incrementamos el contador
            }

            // Editar los vehículos en los datos
            int j = 0;
            for (Vehiculo vehiculo : vehiculosAfectados) {
                // Editamos cada vehículo en datos usando los valores correspondientes de ambas listas
                idVehiculos.editarVehiculosEnDatos(vehiculo.getPlaca(), vehiculo, vehiculosAEditar.get(j));
                j++;
            }


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

    private static List<Linea> lineasAfectadas(String nombreMarca) {
        List<Linea> lineasAfectadas = new ArrayList<>();
        for (Map.Entry<String, Linea> entry : GestorDeArchivos.diccionarioNombreLineas.entrySet()) {
            Linea linea = entry.getValue();

            // Verificamos si el nombre de la marca en la línea coincide con el que estamos buscando
            if (linea.getNombreMarcaDeLinea().equals(nombreMarca)) {
                // Agregamos la línea a la lista de líneas afectadas sin modificarla
                lineasAfectadas.add(linea);
            }
        }
        return lineasAfectadas;
    }

    private static List<Vehiculo> vehiculosAfectados(String nombreMarca){
        List<Vehiculo> vehiculosAfectados = new ArrayList<>();
        for (Map.Entry<String, Vehiculo> entry : GestorDeArchivos.diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();

            // Verificamos si el nombre de la marca en el vehículo coincide con el que estamos buscando
            if (vehiculo.getMarca().getNombre().equals(nombreMarca)) {
                // Agregamos el vehículo a la lista de vehículos afectados sin modificarlo
                vehiculosAfectados.add(vehiculo);
            }
        }
        return vehiculosAfectados;
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
