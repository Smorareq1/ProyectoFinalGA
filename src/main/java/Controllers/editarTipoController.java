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

public class editarTipoController {

    @FXML
    private Button editarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private TextField editarNombreTipo;

    @FXML
    private TextField editarAnioTipo;

    @FXML
    private TipoController tipoController;

    private static String nombreAEditar;
    private static String anioAEditar;

    public static void InformacionAEditar(String nombre, String anio)
    {
        nombreAEditar = nombre;
        anioAEditar = anio;
    }

    @FXML
    public void initialize() {
        editarNombreTipo.setText(nombreAEditar);
        editarAnioTipo.setText(anioAEditar);

        editarButton.setOnAction(event -> {
            try {
                editarTipo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        cancelarButton.setOnAction(event -> cerrarVentana());
    }

    private void cerrarVentana() {
        Stage stage = (Stage) editarButton.getScene().getWindow();
        stage.close();
    }

    private void editarTipo() throws IOException {
        String nuevoNombre = editarNombreTipo.getText();
        String nuevoAnio = editarAnioTipo.getText();

        if(!nuevoNombre.isEmpty() && !nuevoAnio.isEmpty()) {

            Tipo tipoAnterior = GestorDeArchivos.diccionarioNombreTipos.get(nombreAEditar);
            Tipo nuevoTipo = new Tipo(nuevoNombre, nuevoAnio);

            //Editar datos de vehiculo que tengan esta marca
            List<Vehiculo> vehiculosAEditar = new ArrayList<>();
            for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
                if (vehiculo.getTipo().getNombreTipo().equals(nombreAEditar)) {
                    vehiculosAEditar.add(vehiculo);
                }
            }

            List<Vehiculo> vehiculosAfectados = vehiculosAfectados(nombreAEditar);

            //Fin de edicion de vehiculos por marca



            GestorDeArchivos.editarVehiculosDadoTipos(nombreAEditar, nuevoTipo);

            GestorDeArchivos.diccionarioNombreTipos.remove(nombreAEditar);
            GestorDeArchivos.diccionarioNombreTipos.put(nuevoNombre, nuevoTipo);

            //Editar tipo en datos
            idTipo.editarTipoEnDatos(nombreAEditar, nuevoTipo, tipoAnterior);

            // Editar los vehículos en los datos
            int j = 0;
            for (Vehiculo vehiculo : vehiculosAfectados) {
                // Editamos cada vehículo en datos usando los valores correspondientes de ambas listas
                idVehiculos.editarVehiculosEnDatos(vehiculo.getPlaca(), vehiculo, vehiculosAEditar.get(j));
                j++;
            }

            if(tipoController != null) {
                tipoController.actualizarTableView();
            }

            cerrarVentana();
        } else {
            showAlert("Error", "Por favor, completa todos los campos.");
        }
    }

    private static List<Vehiculo> vehiculosAfectados(String nombreTipo){
        List<Vehiculo> vehiculosAfectados = new ArrayList<>();
        for (Map.Entry<String, Vehiculo> entry : GestorDeArchivos.diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();

            // Verificamos si el nombre de la marca en el vehículo coincide con el que estamos buscando
            if (vehiculo.getTipo().getNombreTipo().equals(nombreTipo)) {
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
}
