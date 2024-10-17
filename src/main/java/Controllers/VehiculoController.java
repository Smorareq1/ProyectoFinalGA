package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Vehiculo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class VehiculoController {

    @FXML
    private Button btn_AgregarVehiculo;

    @FXML
    private Button btn_EditarVehiculo;

    @FXML
    private Button btn_EliminarVehiculo;

    @FXML
    private TextField searchFieldMarca;

    @FXML
    private ImageView searchImg;

    @FXML
    private ImageView refreshIcon;

    @FXML
    private TableView<Vehiculo> tableViewVehiculos;

    @FXML
    private TableColumn<Vehiculo, String> marcaColumn;

    @FXML
    private TableColumn<Vehiculo, String> tipoColumn;

    @FXML
    private TableColumn<Vehiculo, String> lineaColumn;

    @FXML
    private TableColumn<Vehiculo, String> modeloColumn;

    @FXML
    private TableColumn<Vehiculo, String> colorColumn;

    @FXML
    private TableColumn<Vehiculo, String> asientosColumn;

    @FXML
    private TableColumn<Vehiculo, String> placaColumn;

    @FXML
    private TableColumn<Vehiculo, String> chasisColumn;

    @FXML
    private TableColumn<Vehiculo, String> motorColumn;

    @FXML
    private TableColumn<Vehiculo, String> vinColumn;

    @FXML
    ObservableList<Vehiculo> vehiculos;

    @FXML
    public void initialize(){

        // Inicializar la lista de vehiculos
        vehiculos = FXCollections.observableArrayList();

        // Configurar las columnas de la tabla
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marcaNombreVehiculo"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoNombreVehiculo"));
        lineaColumn.setCellValueFactory(new PropertyValueFactory<>("lineaNombreVehiculo"));
        modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        asientosColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDeAsientos"));
        placaColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        chasisColumn.setCellValueFactory(new PropertyValueFactory<>("chasis"));
        motorColumn.setCellValueFactory(new PropertyValueFactory<>("motor"));
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));

        //Agregar los vehiculos al ObservableList desde el Map
        for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
            vehiculos.add(vehiculo);
        }

        tableViewVehiculos.setItems(vehiculos);

        //Botones
        btn_AgregarVehiculo.setOnAction(event -> abrirVentanaAgregarVehiculo());
        btn_EditarVehiculo.setOnAction(event -> editarVehiculoSeleccionado());
        btn_EliminarVehiculo.setOnAction(event -> eliminarVehiculoSeleccionado());

        //Imagenes
        searchImg.setOnMouseClicked(event -> filtrarPorMarca());
        refreshIcon.setOnMouseClicked(event -> actualizarTableView());
    }

    //Botones
    private void abrirVentanaAgregarVehiculo() {
        try {
            // Cargar el nuevo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addingVehiculo.fxml"));
            Parent root = loader.load();

            // Crear un nuevo escenario (Stage)
            Stage stage = new Stage();
            stage.setTitle("Agregar Vehiculo");
            stage.initModality(Modality.APPLICATION_MODAL); // Hacer que esta ventana sea modal
            stage.setScene(new Scene(root));

            // Mostrar la ventana
            stage.showAndWait();

            // Después de cerrar la ventana, actualizar el TableView
            actualizarTableView();
        } catch (Exception e) {
            e.printStackTrace(); // Manejar excepciones adecuadamente en tu aplicación
        }
    }

    private void editarVehiculoSeleccionado() {
    }

    private void eliminarVehiculoSeleccionado() {
        Vehiculo vehiculoSeleccionado = tableViewVehiculos.getSelectionModel().getSelectedItem();

        if (vehiculoSeleccionado != null) {
            GestorDeArchivos.diccionarioNombreVehiculos.remove(vehiculoSeleccionado.getPlaca());

            actualizarTableView();

            //Mensaje de exito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exito");
            alert.setHeaderText(null);
            alert.setContentText("Vehiculo eliminado correctamente.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ningun Vehiculo Seleccionado");
            alert.setContentText("Por favor, selecciona un vehiculo para eliminar.");
            alert.showAndWait();
        }
    }


    //Imagenes
    private void filtrarPorMarca() {
        String filtro = searchFieldMarca.getText();

        if (filtro.isEmpty()) {
            actualizarTableView();
        } else {
            vehiculos.clear();
            for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
                if (vehiculo.getMarcaNombreVehiculo().toLowerCase().contains(filtro.toLowerCase())) {
                    vehiculos.add(vehiculo);
                }
            }
            tableViewVehiculos.setItems(vehiculos);
        }
    }

    public void actualizarTableView() {
        vehiculos.clear();
        vehiculos.addAll(GestorDeArchivos.diccionarioNombreVehiculos.values());
        tableViewVehiculos.setItems(vehiculos);
    }


}
