package Controllers;

import Objetos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class idVehiculosController {
    @FXML
    TableView<IndiceVehiculo> idsVehiculosOrdenadosTableView;

    @FXML
    private TableColumn<IndiceVehiculo, String > placaIdColumn;
    @FXML
    private TableColumn<IndiceVehiculo, Long> indiceIdColumn;
    @FXML
    private TableColumn<IndiceVehiculo, Integer> longitudIdColumn;

    @FXML
    private TableView<Vehiculo> datosVehiculosTableView;
    @FXML
    private TableColumn<Vehiculo, String> marcaColumn;
    @FXML
    private TableColumn<Vehiculo, String> lineaColumn;
    @FXML
    private TableColumn<Vehiculo, String> tipoColumn;
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
    private ImageView refreshImg;
    @FXML
    private ImageView placaSearchImage;

    @FXML
    private TextField placaSearchTxt;


    @FXML
    private ObservableList<IndiceVehiculo> indiceVehiculosList;
    @FXML
    private ObservableList<Vehiculo> datosVehiculosList;

    @FXML
    private void initialize() {
        //Inicializar las listas de datos
        indiceVehiculosList = FXCollections.observableArrayList();
        datosVehiculosList = FXCollections.observableArrayList();

        //Configurar las columnas de las tablas
        configurarColumnas();

        //Leer y cargar los datos de los txts
        cargarIndiceVehiculos("src/main/resources/datos/Vehiculos_txt/indiceVehiculosOrdenado.txt");
        cargarDatosVehiculos("src/main/resources/datos/Vehiculos_txt/datosVehiculos.txt");

        //Setear los datos
        idsVehiculosOrdenadosTableView.setItems(indiceVehiculosList);
        datosVehiculosTableView.setItems(datosVehiculosList);

        //Imagenes y sus funciones
        refreshImg.setOnMouseClicked(e -> actualizarTableViews());
        placaSearchImage.setOnMouseClicked(e -> buscarVehiculo());
    }

    private void configurarColumnas() {
        placaIdColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        indiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("indice"));
        longitudIdColumn.setCellValueFactory(new PropertyValueFactory<>("longitud"));

        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marcaNombreVehiculo"));
        lineaColumn.setCellValueFactory(new PropertyValueFactory<>("lineaNombreVehiculo"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoNombreVehiculo"));
        modeloColumn.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        asientosColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDeAsientos"));
        placaColumn.setCellValueFactory(new PropertyValueFactory<>("placa"));
        chasisColumn.setCellValueFactory(new PropertyValueFactory<>("chasis"));
        motorColumn.setCellValueFactory(new PropertyValueFactory<>("motor"));
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
    }

     private void cargarIndiceVehiculos(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String placa = datos[0];
                long indice = Long.parseLong(datos[1]);
                int longitud = Integer.parseInt(datos[2]);
                indiceVehiculosList.add(new IndiceVehiculo(placa, indice, longitud));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosVehiculos(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreMarca = datos[0];
                String nombreTipo = datos[1];
                String nombreLinea = datos[2];
                String modelo = datos[3];
                String color = datos[4];
                String numeroDeAsientos = datos[5];
                String placa = datos[6];
                String chasis = datos[7];
                String motor = datos[8];
                String vin = datos[9];

                Vehiculo vehiculoAsociado = GestorDeArchivos.diccionarioNombreVehiculos.get(placa);
                datosVehiculosList.add(vehiculoAsociado);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarTableViews(){
        indiceVehiculosList.clear();
        datosVehiculosList.clear();

        cargarIndiceVehiculos("src/main/resources/datos/Vehiculos_txt/indiceVehiculosOrdenado.txt");
        cargarDatosVehiculos("src/main/resources/datos/Vehiculos_txt/datosVehiculos.txt");

        idsVehiculosOrdenadosTableView.setItems(indiceVehiculosList);
        datosVehiculosTableView.setItems(datosVehiculosList);
    }

    private void buscarVehiculo(){
        String filtro = placaSearchTxt.getText();

        if(filtro.isEmpty()) {
            actualizarTableViews();
        }else{
            ObservableList<IndiceVehiculo> listaFiltrada = FXCollections.observableArrayList();
            ObservableList<Vehiculo> listaVehiculoFiltrada = FXCollections.observableArrayList();

            for(IndiceVehiculo indice : indiceVehiculosList){
                if(Objects.equals(indice.getPlaca(), filtro)){
                    listaFiltrada.add(indice);

                    Vehiculo vehiculo = GestorDeArchivos.diccionarioNombreVehiculos.get(filtro);
                    listaVehiculoFiltrada.add(vehiculo);

                    idVehiculos.buscarPorNombre(indice, filtro);
                }
            }

            idsVehiculosOrdenadosTableView.setItems(listaFiltrada);
            datosVehiculosTableView.setItems(listaVehiculoFiltrada);
            placaSearchTxt.clear();
        }
    }



}
