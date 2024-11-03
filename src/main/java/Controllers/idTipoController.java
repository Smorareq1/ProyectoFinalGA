package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.IndiceTipo;
import Objetos.idTipo;
import Objetos.Tipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static Objetos.GestorDeArchivos.diccionarioNombreTipos;

public class idTipoController {

    @FXML
    private TableView<IndiceTipo> idsTiposOrdenadosTableView;

    @FXML
    private TableColumn<IndiceTipo, String> tipoIdColumn;
    @FXML
    private TableColumn<IndiceTipo, Long> indiceIdColumn;
    @FXML
    private TableColumn<IndiceTipo, Integer> longitudIdColumn;

    @FXML
    private TableView<Tipo> DatosTipoTableView;
    @FXML
    private TableColumn<Tipo, String> nombreColumn;
    @FXML
    private TableColumn<Tipo, String> anioColumn;

    @FXML
    private ImageView refreshImg;
    @FXML
    private ImageView tipoSearchImage;

    @FXML
    private TextField tipoSearchTxt;

    @FXML
    private ObservableList<IndiceTipo> indiceTiposList;
    @FXML
    private ObservableList<Tipo> datosTiposList;

    @FXML
    public void initialize() {
        // Inicializar las listas de datos
        indiceTiposList = FXCollections.observableArrayList();
        datosTiposList = FXCollections.observableArrayList();

        // Configurar las columnas del TableView
        configurarColumnas();

        // Leer archivos y cargar datos
        cargarIndicesTipos("src/main/resources/datos/Tipos_txt/indiceTiposOrdenados.txt");
        cargarDatosTipos("src/main/resources/datos/Tipos_txt/datosTipos.txt");

        // Setear los datos en los TableView
        idsTiposOrdenadosTableView.setItems(indiceTiposList);
        DatosTipoTableView.setItems(datosTiposList);

        // Imagenes
        refreshImg.setOnMouseClicked(event -> actualizarTableViews());
        tipoSearchImage.setOnMouseClicked(event -> {
            try {
                buscarPorNombre();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void buscarPorNombre() throws IOException {

        String filtro = tipoSearchTxt.getText();

        if (filtro.isEmpty()) {
            actualizarTableViews();
        }

        else {
            ObservableList<IndiceTipo> listaFiltrada = FXCollections.observableArrayList();
            ObservableList<Tipo> listaTipoFiltrada = FXCollections.observableArrayList();
            for (IndiceTipo indice : indiceTiposList) {
                if (Objects.equals(indice.getNombreTipo(), filtro)) {
                    listaFiltrada.add(indice);

                    Tipo tipo = diccionarioNombreTipos.get(filtro);
                    listaTipoFiltrada.add(tipo);

                    System.out.println(tipo);
                    String Nombretipo = indice.getNombreTipo();
                    idTipo.buscarPorNombre(indice, Nombretipo);
                }
            }
            idsTiposOrdenadosTableView.setItems(listaFiltrada);
            DatosTipoTableView.setItems(listaTipoFiltrada);
        }
    }

    private void configurarColumnas() {
        tipoIdColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        indiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("indice"));
        longitudIdColumn.setCellValueFactory(new PropertyValueFactory<>("longitud"));

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreTipo"));
        anioColumn.setCellValueFactory(new PropertyValueFactory<>("anio"));
    }

    private void cargarIndicesTipos(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreTipo = datos[0];
                long indice = Long.parseLong(datos[1]);
                int longitud = Integer.parseInt(datos[2]);
                indiceTiposList.add(new IndiceTipo(nombreTipo, indice, longitud));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosTipos(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreTipo = datos[0];
                String anioTipo = datos[1];
                datosTiposList.add(new Tipo(nombreTipo, anioTipo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarTableViews() {
        indiceTiposList.clear();
        datosTiposList.clear();
        cargarIndicesTipos("src/main/resources/datos/Tipos_txt/indiceTiposOrdenados.txt");
        cargarDatosTipos("src/main/resources/datos/Tipos_txt/datosTipos.txt");
        idsTiposOrdenadosTableView.setItems(indiceTiposList);
        DatosTipoTableView.setItems(datosTiposList);
    }

    // Método para imprimir el contenido de indiceTiposList
    public void imprimirIndiceTipo() {
        System.out.println("Índices de Tipos: ");
        for (IndiceTipo indice : indiceTiposList) {
            System.out.println(
                    ", Índice: " + indice.getIndice() +
                    ", Longitud: " + indice.getLongitud());
        }
    }

    // Método para imprimir el contenido de datosTiposList
    public void imprimirDatosTipos() {
        System.out.println("Datos de Tipo: ");
        for (Tipo tipo : datosTiposList) {
            System.out.println("Nombre: " + tipo.getNombreTipo() +
                    ", Año: " + tipo.getAnioTipo());
        }
    }
}
