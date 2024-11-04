package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Tipo;
import Objetos.idTipo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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

            GestorDeArchivos.printTiposDeVehiculos();
            GestorDeArchivos.editarVehiculosDadoTipos(nombreAEditar, nuevoTipo);
            GestorDeArchivos.printTiposDeVehiculos();

            GestorDeArchivos.diccionarioNombreTipos.remove(nombreAEditar);
            GestorDeArchivos.diccionarioNombreTipos.put(nuevoNombre, nuevoTipo);

            //Editar tipo en datos
            idTipo.editarTipoEnDatos(nombreAEditar, nuevoTipo, tipoAnterior);

            if(tipoController != null) {
                tipoController.actualizarTableView();
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
}
