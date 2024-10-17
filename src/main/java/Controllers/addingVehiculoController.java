package Controllers;

import Objetos.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class addingVehiculoController {

    //Jalaran el objeto
    @FXML
    private ComboBox<String> marcaComboBox;

    @FXML
    private ComboBox<String> lineaComboBox;

    @FXML
    private ComboBox<String> tipoComboBox;

    //Publicos o comunes
    @FXML
    private TextField modeloField;

    @FXML
    private TextField colorField;

    @FXML
    private TextField numeroDeAsientosField;

    //No se pueden repetir
    @FXML
    private TextField placaField;

    @FXML
    private TextField chasisField;

    @FXML
    private TextField motorField;

    @FXML
    private TextField vinField;

    //Botones
    @FXML
    private Button agregarBtn;

    @FXML
    private Button cancelarBtn;

    //Vehiculo controller
    @FXML
    private VehiculoController vehiculoController;

    //Inicializar
    @FXML
    public void initialize() {

        //llenar los combobox
        marcaComboBox.getItems().clear();
        marcaComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreMarcas.keySet());

        lineaComboBox.getItems().clear();
        lineaComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreLineas.keySet());

        tipoComboBox.getItems().clear();
        tipoComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreTipos.keySet());

        agregarBtn.setOnAction(event -> agregarVehiculo());
        cancelarBtn.setOnAction(event -> cerrarVentana());
    }

    //Agregar vehiculo
    private void agregarVehiculo(){

        //Objeto de la clase
        String nombreMarcaSeleccionada = marcaComboBox.getValue();
        Marca marca = GestorDeArchivos.diccionarioNombreMarcas.get(nombreMarcaSeleccionada);

        String nombreLineaSeleccionada = lineaComboBox.getValue();
        Linea linea = GestorDeArchivos.diccionarioNombreLineas.get(nombreLineaSeleccionada);

        String nombreTipoSeleccionado = tipoComboBox.getValue();
        Tipo tipo = GestorDeArchivos.diccionarioNombreTipos.get(nombreTipoSeleccionado);

        //Comunes
        String modelo = modeloField.getText();
        String color = colorField.getText();
        String numeroDeAsientos = numeroDeAsientosField.getText();

        //No se pueden repetir
        String placa = placaField.getText();
        String chasis = chasisField.getText();
        String motor = motorField.getText();
        String vin = vinField.getText();

        //Ver si los valores privados no existen todavia
        if(GestorDeArchivos.setPlacas.contains(placa) || GestorDeArchivos.setChasis.contains(chasis) || GestorDeArchivos.setMotores.contains(motor) || GestorDeArchivos.setVins.contains(vin)){
            //Mensaje de error
            showAlert("Error", "El vehiculo contiene datos que no se pueden repetir de otros vehiculos.");
        }
        else{

            if (marca != null && linea!= null && tipo!= null && !modelo.isEmpty() && !color.isEmpty() && !numeroDeAsientos.isEmpty() && !placa.isEmpty() && !chasis.isEmpty() && !motor.isEmpty() && !vin.isEmpty()) {
            Vehiculo nuevoVehiculo = new Vehiculo(marca, linea, tipo, modelo, color, placa, chasis, motor, vin, numeroDeAsientos);
            GestorDeArchivos.diccionarioNombreVehiculos.put(placa, nuevoVehiculo);

            //Meter los valores que no se pueden repetir a los sets
            GestorDeArchivos.setPlacas.add(placa);
            GestorDeArchivos.setChasis.add(chasis);
            GestorDeArchivos.setMotores.add(motor);
            GestorDeArchivos.setVins.add(vin);

            //Limpiar campos

            modeloField.clear();
            colorField.clear();
            numeroDeAsientosField.clear();
            placaField.clear();
            chasisField.clear();
            motorField.clear();
            vinField.clear();


            //Mensaje de exito
            showAlert("Exito", "Vehiculo añadido correctamente.");
            if (vehiculoController != null) {
                vehiculoController.actualizarTableView();
            }
            cerrarVentana();
            } else {
                //Mensaje de error
                showAlert("Error", "Por favor, completa todos los campos.");
            }
        }
    }

    //Mostrar mensaje de alerta
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Cerrar ventana
    private void cerrarVentana() {
        Stage stage = (Stage) cancelarBtn.getScene().getWindow();
        stage.close();
    }



}
