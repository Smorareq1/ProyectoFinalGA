package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Marca;
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

public class MarcaController {

    @FXML
    private Button btn_AgregarMarca;

    @FXML
    private Button btn_EditarMarca;

    @FXML
    private Button btn_EliminarMarca; // Nuevo botón para eliminar

    @FXML
    private TextField searchFieldNombre;

    @FXML
    private ImageView searchImg;

    @FXML
    private ImageView refreshIcon;

    @FXML
    private TableView<Marca> tableViewMarcas;

    @FXML
    private TableColumn<Marca, String> colNombre;

    @FXML
    private TableColumn<Marca, String> colFundador;

    @FXML
    private TableColumn<Marca, String> colAnio;

    @FXML
    ObservableList<Marca> marcas;

    @FXML
    public void initialize() {

        // Inicializar la lista de marcas
        marcas = FXCollections.observableArrayList();

        // Configurar las columnas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFundador.setCellValueFactory(new PropertyValueFactory<>("fundador"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioDeCreacion"));

        // Centrar el texto en las columnas
        setTableCellAlignment(colNombre);
        setTableCellAlignment(colFundador);
        setTableCellAlignment(colAnio);

        // Agregar las marcas al ObservableList desde el Map
        for (Marca marca : GestorDeArchivos.diccionarioNombreMarcas.values()) {
            marcas.add(marca);
        }
        tableViewMarcas.setItems(marcas);

        btn_AgregarMarca.setOnAction(event -> abrirVentanaAgregarMarca());
        btn_EditarMarca.setOnAction(event -> editarMarcaSeleccionada());
        btn_EliminarMarca.setOnAction(event -> eliminarMarcaSeleccionada()); // Asignar acción de eliminación

        // Asignar acción de búsqueda a la imagen
        searchImg.setOnMouseClicked(event -> filtrarMarcasPorNombre());

        // Refresh icon
        refreshIcon.setOnMouseClicked(event -> actualizarTableView());
    }

    private void setTableCellAlignment(TableColumn<Marca, String> column) {
        column.setCellFactory(col -> new TableCell<Marca, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
                setStyle("-fx-alignment: CENTER;");
            }
        });
    }

    private void abrirVentanaAgregarMarca() {
        try {
            // Cargar el nuevo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addingMarca.fxml"));
            Parent root = loader.load();

            // Crear un nuevo escenario (Stage)
            Stage stage = new Stage();
            stage.setTitle("Agregar Marca");
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

    public void actualizarTableView() {
        // Limpiar la lista actual
        marcas.clear();
        // Agregar todas las marcas del Map
        marcas.addAll(GestorDeArchivos.diccionarioNombreMarcas.values());
        // Actualizar el TableView
        tableViewMarcas.setItems(marcas);
    }

    private void editarMarcaSeleccionada() {
        // Obtener la marca seleccionada
        Marca marcaSeleccionada = tableViewMarcas.getSelectionModel().getSelectedItem();

        if (marcaSeleccionada != null)
        {
            // Acceder a las propiedades de la marca seleccionada
            String nombre = marcaSeleccionada.getNombre();
            String fundador = marcaSeleccionada.getFundador();
            String anioDeCreacion = marcaSeleccionada.getAnioDeCreacion();

            try {


                editarMarcaController.InformacionAEditar(nombre, anioDeCreacion, fundador);

                // Cargar el nuevo FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editMarca.fxml"));
                Parent root = loader.load();

                // Crear un nuevo escenario (Stage)
                Stage stage = new Stage();
                stage.setTitle("Agregar Marca");
                stage.initModality(Modality.APPLICATION_MODAL); // Hacer que esta ventana sea modal
                stage.setScene(new Scene(root));

                // Mostrar la ventana
                stage.showAndWait();

                // Después de cerrar la ventana, actualizar el TableView
                actualizarTableView();
            } catch (Exception e) {
                e.printStackTrace(); // Manejar excepciones adecuadamente en tu aplicación
            }

        } else {
            // Mostrar un mensaje de alerta si no se seleccionó ninguna marca
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ninguna Marca Seleccionada");
            alert.setContentText("Por favor, selecciona una marca para editar.");
            alert.showAndWait();
        }
    }

    private void eliminarMarcaSeleccionada() {
        // Obtener la marca seleccionada
        Marca marcaSeleccionada = tableViewMarcas.getSelectionModel().getSelectedItem();

        if (marcaSeleccionada != null) {
            // Eliminar la marca del diccionario en GestorDeArchivos
            GestorDeArchivos.diccionarioNombreMarcas.remove(marcaSeleccionada.getNombre());

            // Actualizar el TableView
            actualizarTableView();

            // Eliminar las líneas asociadas a la marca y lineas asociadas a los vehiculos de la marca
            GestorDeArchivos.buscarYEliminarLineaPorMarca(marcaSeleccionada.getNombre());
            GestorDeArchivos.buscarYEliminarVehiculoPorMarca(marcaSeleccionada.getNombre());

            // Mostrar mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Marca Eliminada");
            alert.setHeaderText(null);
            alert.setContentText("La marca ha sido eliminada exitosamente.");
            alert.showAndWait();
        } else {
            // Mostrar un mensaje de alerta si no se seleccionó ninguna marca
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ninguna Marca Seleccionada");
            alert.setContentText("Por favor, selecciona una marca para eliminar.");
            alert.showAndWait();
        }
    }

    private void filtrarMarcasPorNombre() {
        String filtro = searchFieldNombre.getText().toLowerCase().trim();
        if (filtro.isEmpty()) {
            actualizarTableView(); // Si el filtro está vacío, restaurar la lista completa
        } else {
            ObservableList<Marca> marcasFiltradas = FXCollections.observableArrayList();
            for (Marca marca : GestorDeArchivos.diccionarioNombreMarcas.values()) {
                if (marca.getNombre().toLowerCase().contains(filtro)) {
                    marcasFiltradas.add(marca);
                }
            }
            tableViewMarcas.setItems(marcasFiltradas);
        }
        searchFieldNombre.clear(); // Limpiar el campo de búsqueda después de filtrar
    }
}
