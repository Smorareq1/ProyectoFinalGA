package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Vehiculo;
import Objetos.Marca;
import Objetos.Linea;
import Objetos.Tipo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editarVehiculoController {

    @FXML
    private Button editarBtn;

    @FXML
    private Button cancelarBtn;

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

    //Controller vehiculo
    @FXML
    private VehiculoController vehiculoController;

    private static Vehiculo vehiculoAEditar;

    public static void setVehiculoAEditar(Vehiculo vehiculo) {
        vehiculoAEditar = vehiculo;
    }

    //Inicializar
    @FXML
    public void initialize() {
        //Inicializar ComboBox con las marcas
        marcaComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreMarcas.keySet());
        //Inicializar ComboBox con los tipos
        tipoComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreTipos.keySet());
        //Inicializar ComboBox con las lineas
        lineaComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreLineas.keySet());
        //Inicializar los campos con los datos del vehiculo
        marcaComboBox.setValue(vehiculoAEditar.getMarca().getNombre());
        lineaComboBox.setValue(vehiculoAEditar.getLinea().getNombreLinea());
        tipoComboBox.setValue(vehiculoAEditar.getTipo().getNombreTipo());
        modeloField.setText(vehiculoAEditar.getModelo());
        colorField.setText(vehiculoAEditar.getColor());
        numeroDeAsientosField.setText(vehiculoAEditar.getNumeroDeAsientos());
        placaField.setText(vehiculoAEditar.getPlaca());
        chasisField.setText(vehiculoAEditar.getChasis());
        motorField.setText(vehiculoAEditar.getMotor());
        vinField.setText(vehiculoAEditar.getVin());

        //Boton de editar
        editarBtn.setOnAction(event -> editarVehiculo());
        //Boton de cancelar
        cancelarBtn.setOnAction(event -> cerrarVentana());
    }

    private void editarVehiculo(){
        //Objeto de la clase
        String nombreMarcaSeleccionada = marcaComboBox.getValue();
        Marca marcaNueva = GestorDeArchivos.diccionarioNombreMarcas.get(nombreMarcaSeleccionada);

        String nombreLineaSeleccionada = lineaComboBox.getValue();
        Linea lineaNueva = GestorDeArchivos.diccionarioNombreLineas.get(nombreLineaSeleccionada);

        String nombreTipoSeleccionado = tipoComboBox.getValue();
        Tipo tipoNuevo = GestorDeArchivos.diccionarioNombreTipos.get(nombreTipoSeleccionado);

        //Comunes
        String modeloNuevo = modeloField.getText();
        String colorNuevo = colorField.getText();
        String numeroDeAsientosNuevo = numeroDeAsientosField.getText();

        //No se pueden repetir
        String placaNuevo = placaField.getText();
        String chasisNuevo = chasisField.getText();
        String motorNuevo = motorField.getText();
        String vinNuevo = vinField.getText();

        //Quitar los no repetibles de los sets de los objetos
        GestorDeArchivos.setPlacas.remove(vehiculoAEditar.getPlaca());
        GestorDeArchivos.setChasis.remove(vehiculoAEditar.getChasis());
        GestorDeArchivos.setMotores.remove(vehiculoAEditar.getMotor());
        GestorDeArchivos.setVins.remove(vehiculoAEditar.getVin());

        //Verificar que los no repetibles no esten repetidos
        if(GestorDeArchivos.setPlacas.contains(placaNuevo) || GestorDeArchivos.setChasis.contains(chasisNuevo) || GestorDeArchivos.setMotores.contains(motorNuevo) || GestorDeArchivos.setVins.contains(vinNuevo)){
            //Mensaje de error
            showAlert("Error", "El vehiculo contiene datos que no se pueden repetir de otros vehiculos.");
        }
        else{

            if (marcaNueva != null && lineaNueva!= null && tipoNuevo!= null && !modeloNuevo.isEmpty() && !colorNuevo.isEmpty() && !numeroDeAsientosNuevo.isEmpty() && !placaNuevo.isEmpty() && !chasisNuevo.isEmpty() && !motorNuevo.isEmpty() && !vinNuevo.isEmpty()) {
            Vehiculo nuevoVehiculo = new Vehiculo(marcaNueva, lineaNueva, tipoNuevo, modeloNuevo, colorNuevo, placaNuevo, chasisNuevo, motorNuevo, vinNuevo, numeroDeAsientosNuevo);

            //Remover el anterior
            GestorDeArchivos.diccionarioNombreVehiculos.remove(vehiculoAEditar.getPlaca());

            //Agregar el nuevo
            GestorDeArchivos.diccionarioNombreVehiculos.put(placaNuevo, nuevoVehiculo);

            //Meter los valores que no se pueden repetir a los sets
            GestorDeArchivos.setPlacas.add(placaNuevo);
            GestorDeArchivos.setChasis.add(chasisNuevo);
            GestorDeArchivos.setMotores.add(motorNuevo);
            GestorDeArchivos.setVins.add(vinNuevo);

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

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void cerrarVentana(){
        Stage stage = (Stage) cancelarBtn.getScene().getWindow();
        stage.close();
    }


}
