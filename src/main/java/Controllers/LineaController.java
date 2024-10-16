package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.Linea;
import Objetos.Marca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LineaController {

    @FXML
    private TextField nombreLineaTextField;

    @FXML
    private TextField anioLineaTextField;

    @FXML
    private ComboBox<String> marcaComboBox;

    @FXML
    private TableView<Linea> tableView;

    @FXML
    private TableColumn<Linea, String> marcaColumn;

    @FXML
    private TableColumn<Linea, String> lineaColumn;

    @FXML
    private TableColumn<Linea, String> anioColumn;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btneliminar;

    @FXML
    private TextField searchFieldNombre;

    @FXML
    private ImageView searchImg;

    private ObservableList<Linea> lineasList;

    @FXML
    public void initialize() {
        // Inicializar ComboBox con las marcas
        marcaComboBox.getItems().clear();
        marcaComboBox.getItems().addAll(GestorDeArchivos.diccionarioNombreMarcas.keySet());

        // Inicializar la lista observable
        lineasList = FXCollections.observableArrayList();

        // Vincular columnas del TableView con propiedades de la clase Linea
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreMarca"));
        lineaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreLinea"));
        anioColumn.setCellValueFactory(new PropertyValueFactory<>("anioLinea"));

        // Cargar las líneas desde el archivo JSON
        GestorDeArchivos.cargarLineasDesdeJson();

        // Verificar si el diccionario no está vacío y cargar las líneas al TableView
        if (!GestorDeArchivos.diccionarioNombreLineas.isEmpty()) {
            lineasList.addAll(GestorDeArchivos.diccionarioNombreLineas.values());
            System.out.println("Líneas cargadas al TableView: " + GestorDeArchivos.diccionarioNombreLineas.values());
        } else {
            System.out.println("No hay líneas guardadas en el archivo.");
        }

        // Establecer la lista en el TableView
        tableView.setItems(lineasList);

        btnEditar.setOnAction(event -> editarLineaSeleccionada());
        btneliminar.setOnAction(event -> eliminarLineaSeleccionada()); // Asignar acción de eliminación
        searchImg.setOnMouseClicked(event -> filtrarlineaPorNombre());
    }

    @FXML
    public void agregarLinea() {
        // Validar que los campos no estén vacíos
        if (nombreLineaTextField.getText().isEmpty() || anioLineaTextField.getText().isEmpty() || marcaComboBox.getValue() == null) {
            System.out.println("Error: Campos vacíos.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Campos vacíos");
            alert.setContentText("Por favor, completa todos los campos antes de agregar la línea.");
            alert.showAndWait();
            return;
        }

        // Obtener la marca seleccionada del ComboBox
        String nombreMarcaSeleccionada = marcaComboBox.getValue();
        Marca marcaSeleccionada = GestorDeArchivos.diccionarioNombreMarcas.get(nombreMarcaSeleccionada);

        if (marcaSeleccionada == null) {
            System.out.println("No se ha seleccionado ninguna marca.");
            return;
        }

        // Crear una nueva instancia de Linea con la marca seleccionada
        Linea nuevaLinea = new Linea(marcaSeleccionada, nombreLineaTextField.getText(), anioLineaTextField.getText());

        // Agregar la nueva línea al diccionario de líneas
        GestorDeArchivos.diccionarioNombreLineas.put(nuevaLinea.getNombreLinea(), nuevaLinea);

        // Agregar la nueva línea a la lista observable para que aparezca en el TableView
        lineasList.add(nuevaLinea);

        // Guardar las líneas en el archivo JSON
        GestorDeArchivos.guardarLineasEnJson();

        // Limpiar los campos después de agregar
        nombreLineaTextField.clear();
        anioLineaTextField.clear();
        marcaComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void editarLineaSeleccionada()
    {

        System.out.println("El botón Editar fue presionado");

        Linea lineaSeleccionada = tableView.getSelectionModel().getSelectedItem();

        if (lineaSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editarLinea.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la ventana de edición
                EditarLineaController editarController = loader.getController();

                // Pasar los datos a editar y el controlador principal
                // Pasar los datos a editar y el controlador principal
                editarController.setLineaAEditar(lineaSeleccionada);  // Pasar la línea completa
                editarController.setLineaController(this);

                Stage stage = new Stage();
                stage.setTitle("Editar Línea");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                actualizarTableView(); // Actualiza el TableView después de la edición
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ninguna Línea Seleccionada");
            alert.setContentText("Por favor, selecciona una línea para editar.");
            alert.showAndWait();
        }
    }


    public void actualizarTableView() {
        // Limpiar la lista actual
        lineasList.clear();
        // Agregar todas las líneas del Map
        lineasList.addAll(GestorDeArchivos.diccionarioNombreLineas.values()); // Cambiado Linea.addAll() por lineasList.addAll()
        // Actualizar el TableView
        tableView.setItems(lineasList);
    }

    private void eliminarLineaSeleccionada() {
        // Obtener la línea seleccionada
        Linea lineaSeleccionada = tableView.getSelectionModel().getSelectedItem();

        if (lineaSeleccionada != null) {

            GestorDeArchivos.diccionarioNombreLineas.remove(lineaSeleccionada.getNombreLinea());

            actualizarTableView();
            GestorDeArchivos.guardarLineasEnJson();
            // Mostrar mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Línea Eliminada");
            alert.setHeaderText(null);
            alert.setContentText("La línea ha sido eliminada exitosamente.");
            alert.showAndWait();
        } else {
            // Mostrar un mensaje de alerta si no se seleccionó ninguna línea
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ninguna Línea Seleccionada");
            alert.setContentText("Por favor, selecciona una línea para eliminar.");
            alert.showAndWait();
        }
    }


    private void filtrarlineaPorNombre()
    {
        String filtro = searchFieldNombre.getText().toLowerCase().trim();
        if (filtro.isEmpty()) {
            actualizarTableView(); // Si el filtro está vacío, restaurar la lista completa
        } else {
            ObservableList<Linea> lineasFiltradas = FXCollections.observableArrayList();
            for (Linea linea : GestorDeArchivos.diccionarioNombreLineas.values()) {
                if (linea.getNombreLinea().toLowerCase().contains(filtro)) {
                    lineasFiltradas.add(linea);
                }
            }
            tableView.setItems(lineasFiltradas);
        }
        searchFieldNombre.clear(); // Limpiar el campo de búsqueda después de filtrar
    }
}





