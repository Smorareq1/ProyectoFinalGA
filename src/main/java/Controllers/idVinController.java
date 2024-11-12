package Controllers;

import Objetos.*;
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

public class idVinController
{
    @FXML
    private TableView idsVinOrdenadosTableView;

    @FXML
    private TableColumn VinIdColumn;
    @FXML
    private TableColumn  indiceIdColumn;
    @FXML
    private TableColumn longitudIdColumn;
    @FXML
    private TableColumn SiguienteVin;

    @FXML
    private TableView DatosVinTableView;
    @FXML
    private TableColumn  MarcaColumn;
    @FXML
    private TableColumn  LineaColumn;
    @FXML
    private TableColumn  PlacaColumn;
    @FXML
    private TableColumn  VinColumn;

    @FXML
    private ImageView refreshImg;
    @FXML
    private ImageView VinSearchImage;

    @FXML
    private TextField VinSearchTxt;

    @FXML
    private ObservableList<IndiceVIN> listaIndiceVines;
    @FXML
    private ObservableList<DatosVines> listaDatosVines;

    @FXML
    private void initialize() {
        listaDatosVines = FXCollections.observableArrayList();
        listaIndiceVines = FXCollections.observableArrayList();

        configurarColumnas();

        cargarIndiceVines("src/main/resources/datos/VIN_txt/indiceVIN.txt");
        cargarDatosVines("src/main/resources/datos/VIN_txt/datosVIN.txt");

        //Setear los datos en las tablas
        idsVinOrdenadosTableView.setItems(listaIndiceVines);
        DatosVinTableView.setItems(listaDatosVines);

        refreshImg.setOnMouseClicked(e -> actualizarTableViews());
        VinSearchImage.setOnMouseClicked(e -> buscarVin());
    }

    private void configurarColumnas() {
        VinIdColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        indiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("posicionInicial"));
        longitudIdColumn.setCellValueFactory(new PropertyValueFactory<>("longitud"));
        SiguienteVin.setCellValueFactory(new PropertyValueFactory<>("siguiente"));

        MarcaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreMarca"));
        LineaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreLinea"));
        PlacaColumn.setCellValueFactory(new PropertyValueFactory<>("Placa"));
        VinColumn.setCellValueFactory(new PropertyValueFactory<>("Vin"));
    }

    private void cargarIndiceVines(String filePath) {

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    // Omitir líneas vacías o solo con espacios en blanco
                    if (linea.trim().isEmpty()) {
                        continue;
                    }

                    String[] datos = linea.split(",");

                    // Verificar si la línea tiene el número de elementos esperados
                    if (datos.length < 4) {
                        System.out.println("Formato incorrecto en la línea del archivo de índice: " + linea);
                        continue; // Saltar esta línea si no tiene suficientes datos
                    }

                    try {
                        String vin = datos[0];
                        int posicionInicial = Integer.parseInt(datos[1]);
                        int longitud = Integer.parseInt(datos[2]);
                        int siguiente = Integer.parseInt(datos[3]);

                        listaIndiceVines.add(new IndiceVIN(vin, posicionInicial, longitud, siguiente));
                    } catch (NumberFormatException e) {
                        System.out.println("Error al convertir datos numéricos en la línea: " + linea);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void cargarDatosVines(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length < 4) {
                    System.out.println("Formato incorrecto en la línea del archivo de datos: " + linea);
                    continue; // Skip this line if it doesn’t have enough data
                }

                if (linea.trim().isEmpty()) {
                    continue;
                }

                String nombreMarca = datos[0];
                String nombreLinea = datos[1];
                String Placa = datos[2];
                String Vin = datos[3];

                DatosVines datosVinesAsociados = new DatosVines(nombreMarca, nombreLinea, Placa, Vin);
                listaDatosVines.add(datosVinesAsociados);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarTableViews(){
        listaIndiceVines.clear();
        listaDatosVines.clear();

        cargarIndiceVines("src/main/resources/datos/VIN_txt/indiceVIN.txt");
        cargarDatosVines("src/main/resources/datos/VIN_txt/datosVIN.txt");

        idsVinOrdenadosTableView.setItems(listaIndiceVines);
        DatosVinTableView.setItems(listaDatosVines);
    }

    private void buscarVin(){
        String filtro = VinSearchTxt.getText();

        if(filtro.isEmpty()) {
            actualizarTableViews();
        }else{
            ObservableList<IndiceVIN> listaFiltrada = FXCollections.observableArrayList();
            ObservableList<DatosVines> listaVinFiltrada = FXCollections.observableArrayList();

            for(IndiceVIN indice : listaIndiceVines){
                if(Objects.equals(indice.getVin(), filtro)){
                    listaFiltrada.add(indice);

                    DatosVines vinEncontrado = IdVines.buscarVin(filtro);
                    listaVinFiltrada.add(vinEncontrado);
                }
            }

            idsVinOrdenadosTableView.setItems(listaFiltrada);
            DatosVinTableView.setItems(listaVinFiltrada);
            VinSearchTxt.clear();
        }
    }

}
