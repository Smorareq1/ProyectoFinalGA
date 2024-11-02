package Controllers;

import Objetos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class idLineasController {

    @FXML
    private TableView<IndiceLinea> idsLineasOrdenadosTableView;

    @FXML
    private TableColumn<IndiceLinea, String> lineaIdColumn;
    @FXML
    private TableColumn<IndiceLinea, Long> indiceIdColumn;
    @FXML
    private TableColumn<IndiceLinea, Integer> longitudIdColumn;

    @FXML
    private TableView<Linea> DatosLineasTableView;
    @FXML
    private TableColumn<Linea, String> nombreColumn;
    @FXML
    private TableColumn<Linea, String> anioColumn;
    @FXML
    private TableColumn<Linea, String> marcaColumn;

    @FXML
    private ImageView refreshImg;
    @FXML
    private ImageView lineaSearchImage;
    @FXML
    private TextField lineaSearchTxt;

    @FXML
    private ObservableList<IndiceLinea> indiceLineasList;
    @FXML
    private ObservableList<Linea> datosLineasList;

    @FXML
    private void initialize() {
        //Inicializar las listas de datos
        indiceLineasList = FXCollections.observableArrayList();
        datosLineasList = FXCollections.observableArrayList();

        //Configurar las columnas de las tablas
        configurarColumnas();

        //Leer y cargar los datos de los txts
        cargarIndiceLineas("src/main/resources/datos/Lineas_txt/indiceLineasOrdenado.txt");
        cargarDatosLineas("src/main/resources/datos/Lineas_txt/datosLineas.txt");

        //Setear los datos
        idsLineasOrdenadosTableView.setItems(indiceLineasList);
        DatosLineasTableView.setItems(datosLineasList);

        //Imagenes y sus funciones
        refreshImg.setOnMouseClicked(e -> actualizarTableViews());
        lineaSearchImage.setOnMouseClicked(e -> {
            try {
                buscarLinea();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void configurarColumnas(){
        //Configuracion de las columnas de los table View
        lineaIdColumn.setCellValueFactory(new PropertyValueFactory<>("nombreLinea"));
        indiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("indice"));
        longitudIdColumn.setCellValueFactory(new PropertyValueFactory<>("longitud"));

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreLinea"));
        anioColumn.setCellValueFactory(new PropertyValueFactory<>("anioLinea"));
        marcaColumn.setCellValueFactory(new PropertyValueFactory<>("nombreMarcaDeLinea"));
    }

    private void cargarIndiceLineas(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreLinea = datos[0];
                long indice = Long.parseLong(datos[1]);
                int longitud = Integer.parseInt(datos[2]);
                indiceLineasList.add(new IndiceLinea(nombreLinea, indice, longitud));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosLineas(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombreMarca = datos[0];
                String nombreLinea = datos[1];
                String anioCreacion = datos[2];

                Linea lineaEncontrada = GestorDeArchivos.diccionarioNombreLineas.get(nombreLinea);
                Marca marcaAsociada = lineaEncontrada.getMarca();

                datosLineasList.add(new Linea(marcaAsociada,nombreLinea, anioCreacion));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarTableViews(){
        indiceLineasList.clear();
        datosLineasList.clear();

        cargarIndiceLineas("src/main/resources/datos/Lineas_txt/indiceLineasOrdenado.txt");
        cargarDatosLineas("src/main/resources/datos/Lineas_txt/datosLineas.txt");

        idsLineasOrdenadosTableView.setItems(indiceLineasList);
        DatosLineasTableView.setItems(datosLineasList);
    }

    private void buscarLinea() throws IOException {
        String filtro = lineaSearchTxt.getText();

        if(filtro.isEmpty()){
            actualizarTableViews();
        }else{
            ObservableList<IndiceLinea> listaFiltrada = FXCollections.observableArrayList();
            ObservableList<Linea> listaLineaFiltrada = FXCollections.observableArrayList();

            for(IndiceLinea indice : indiceLineasList){
                if(Objects.equals(indice.getNombreLinea(), filtro)){
                    listaFiltrada.add(indice);

                    Linea linea = GestorDeArchivos.diccionarioNombreLineas.get(filtro);
                    listaLineaFiltrada.add(linea);

                    //System.out.println(linea);

                    String nombreLinea = indice.getNombreLinea();
                    idLinea.buscarPorNombre(indice, nombreLinea);
                }
            }
            idsLineasOrdenadosTableView.setItems(listaFiltrada);
            DatosLineasTableView.setItems(listaLineaFiltrada);
        }

    }


}
