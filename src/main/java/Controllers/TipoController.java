package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Tipo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TipoController {

    @FXML
    private Button btn_AgregarTipo;

    @FXML
    private Button btn_EditarTipo;

    @FXML
    private Button btn_EliminarTipo;

    @FXML
    private TextField searchFieldNombre;

    @FXML
    private ImageView searchImg;

    @FXML
    private ImageView refreshIcon;

    @FXML
    private TableView<Tipo> tableViewTipos;

    @FXML
    private TableColumn<Tipo, String> colNombreTipo;

    @FXML
    private TableColumn<Tipo, String> colAnioTipo;

    @FXML
    private ObservableList<Tipo> tipos;

    @FXML
    public void initialize() {
        // Inicializar la lista de tipos
        tipos = FXCollections.observableArrayList();

        // Enlazar las columnas con los atributos de la clase Tipo
        colNombreTipo.setCellValueFactory(new PropertyValueFactory<>("nombreTipo"));
        colAnioTipo.setCellValueFactory(new PropertyValueFactory<>("anioTipo"));

        // Configurar las celdas de las columnas para centrar el texto
        colNombreTipo.setCellFactory(tc -> new TableCell<Tipo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER;"); // Centrar el texto
                }
            }
        });

        colAnioTipo.setCellFactory(tc -> new TableCell<Tipo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER;"); // Centrar el texto
                }
            }
        });

        // Asignar los datos a la tabla
        for (Tipo tipo : GestorDeArchivos.diccionarioNombreTipos.values()) {
            tipos.add(tipo);
        }

        tableViewTipos.setItems(tipos);

        // Configurar la búsqueda filtrada por nombre
        searchFieldNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tableViewTipos.setItems(tipos);
            } else {
                ObservableList<Tipo> tiposFiltrados = FXCollections.observableArrayList();
                for (Tipo tipo : tipos) {
                    if (tipo.getNombreTipo().toLowerCase().contains(newValue.toLowerCase())) {
                        tiposFiltrados.add(tipo);
                    }
                }
                tableViewTipos.setItems(tiposFiltrados);
            }
        });

        // Configurar la acción de agregar un tipo
        btn_AgregarTipo.setOnAction(event -> abrirVentanaAgregarTipo());

        // Configurar la acción de editar un tipo
        btn_EditarTipo.setOnAction(event -> editarTipoSeleccionado());

        // Configurar la acción de eliminar un tipo
        btn_EliminarTipo.setOnAction(event -> eliminarTipoSeleccionado());

        // Configurar la acción de buscar (enfocar campo de búsqueda)
        searchImg.setOnMouseClicked(event -> filtrarTiposPorNombre());

        // Configurar la acción de refrescar la tabla
        refreshIcon.setOnMouseClicked(event -> actualizarTableView());
    }

    private void filtrarTiposPorNombre() {
        String filtro = searchFieldNombre.getText().toLowerCase();

        if(filtro.isEmpty()) {
            tableViewTipos.setItems(tipos);
        } else {
            ObservableList<Tipo> tiposFiltrados = FXCollections.observableArrayList();
            for (Tipo tipo : GestorDeArchivos.diccionarioNombreTipos.values()) {
                if (tipo.getNombreTipo().toLowerCase().contains(filtro)) {
                    tiposFiltrados.add(tipo);
                }
            }
            tableViewTipos.setItems(tiposFiltrados);
        }
    }

    // Método para agregar un nuevo tipo
    private void abrirVentanaAgregarTipo() {
        try {
            // Cargar el nuevo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addingTipo.fxml"));
            Parent root = loader.load();

            // Crear un nuevo escenario (Stage)
            Stage stage = new Stage();
            stage.setTitle("Agregar Tipo");
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

    // Método para editar un tipo seleccionado
    private void editarTipoSeleccionado() {
        Tipo selectedTipo = tableViewTipos.getSelectionModel().getSelectedItem();

        if (selectedTipo != null) {

            try{
                editarTipoController.InformacionAEditar(selectedTipo.getNombreTipo(), selectedTipo.getAnioTipo());
                // Cargar el nuevo FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editarTipo.fxml"));
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
            }
            catch (Exception e) {
                e.printStackTrace();
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

    // Método para eliminar un tipo seleccionado
    private void eliminarTipoSeleccionado() {
        Tipo selectedTipo = tableViewTipos.getSelectionModel().getSelectedItem();
        if (selectedTipo != null) {

            GestorDeArchivos.diccionarioNombreTipos.remove(selectedTipo.getNombreTipo());

            actualizarTableView();

            mostrarAlerta("Tipo eliminado correctamente.");
        } else {
            mostrarAlerta("No se ha seleccionado ningún tipo para eliminar.");
        }
    }

    // Método para mostrar una alerta simple
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para actualizar la tabla (simular la actualización de los datos)
    public void actualizarTableView() {
        tipos.clear();
        tipos.addAll(GestorDeArchivos.diccionarioNombreTipos.values());
        tableViewTipos.setItems(tipos);
    }
}
