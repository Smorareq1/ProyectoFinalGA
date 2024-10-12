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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class MarcaController {

    @FXML
    private Button btn_AgregarMarca;

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
        colNombre.setCellFactory(column -> new TableCell<Marca, String>() {
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

        colFundador.setCellFactory(column -> new TableCell<Marca, String>() {
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

        colAnio.setCellFactory(column -> new TableCell<Marca, String>() {
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

        // Agregar las marcas a la tabla
        for (Marca marca : GestorDeArchivos.listaMarcas) {
            marcas.add(marca);
        }
        tableViewMarcas.setItems(marcas);

        btn_AgregarMarca.setOnAction(event -> abrirVentanaAgregarMarca());
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
        // Agregar todas las marcas de la lista normal
        marcas.addAll(GestorDeArchivos.listaMarcas);
        // Actualizar el TableView
        tableViewMarcas.setItems(marcas);
    }

}
