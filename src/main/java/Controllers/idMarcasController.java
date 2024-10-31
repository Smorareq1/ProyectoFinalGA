package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.IndiceMarca;
import Objetos.Marca;
import Objetos.idMarca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static Objetos.GestorDeArchivos.diccionarioNombreMarcas;

public class idMarcasController {

    @FXML
    private TableView<IndiceMarca> idsMarcasOrdenadosTableView;

    @FXML
    private TableColumn<IndiceMarca, String> marcaIdColumn;
    @FXML
    private TableColumn<IndiceMarca, Long> indiceIdColumn;
    @FXML
    private TableColumn<IndiceMarca, Integer> longitudIdColumn;

    @FXML
    private TableView<Marca> DatosMarcasTableView;
    @FXML
    private TableColumn<Marca, String> nombreColumn;
    @FXML
    private TableColumn<Marca, String> anioColumn;
    @FXML
    private TableColumn<Marca, String> fundadorColumn;

    @FXML
    private ImageView refreshImg;
    @FXML
    private ImageView indexSearchImage;
    @FXML
    private ImageView nameSearchImage;

    @FXML
    private TextField indexSearchTxt;



    @FXML
    private ObservableList<IndiceMarca> indiceMarcasList;
    @FXML
    private ObservableList<Marca> datosMarcasList;

    @FXML
    public void initialize() {
        // Inicializar las listas de datos
        indiceMarcasList = FXCollections.observableArrayList();
        datosMarcasList = FXCollections.observableArrayList();

        // Configurar las columnas del TableView
        configurarColumnas();

        // Leer archivos y cargar datos
        cargarIndicesMarcas("src/main/resources/datos/Marcas_txt/indiceMarcasOrdenado.txt");
        cargarDatosMarcas("src/main/resources/datos/Marcas_txt/datosMarcas.txt");

        // Setear los datos en los TableView
        idsMarcasOrdenadosTableView.setItems(indiceMarcasList);
        DatosMarcasTableView.setItems(datosMarcasList);

        //Imagenes
        refreshImg.setOnMouseClicked(event -> actualizarTableViews());
        indexSearchImage.setOnMouseClicked(event -> {
            try {
                buscarPorNombre();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Prueba
        //imprimirIndicesMarcas();
        //imprimirDatosMarcas();

    }

    private void buscarPorNombre() throws IOException {

        String filtro = indexSearchTxt.getText();

        if(filtro.isEmpty()) {
            actualizarTableViews();
        }else{
            ObservableList<IndiceMarca> listaFiltrada = FXCollections.observableArrayList();
            ObservableList<Marca> listaMarcaFiltrada = FXCollections.observableArrayList();
            for (IndiceMarca indice : indiceMarcasList) {
                if (Objects.equals(indice.getMarca(), filtro)) {
                    listaFiltrada.add(indice);


                    Marca marca = diccionarioNombreMarcas.get(filtro);
                    listaMarcaFiltrada.add(marca);

                    //Prueba
                    System.out.println(marca);
                    //fin prueba

                    //Alerta de que se encontro el indice y se muestra la marca
                    String Nombremarca = indice.getMarca();
                    idMarca.buscarPorNombre(indice, Nombremarca);
                }
            }
            idsMarcasOrdenadosTableView.setItems(listaFiltrada);
            DatosMarcasTableView.setItems(listaMarcaFiltrada);

        }

    }


    private void configurarColumnas() {
        // Configurar las columnas de los TableView
        marcaIdColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
        indiceIdColumn.setCellValueFactory(new PropertyValueFactory<>("indice"));
        longitudIdColumn.setCellValueFactory(new PropertyValueFactory<>("longitud"));

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        anioColumn.setCellValueFactory(new PropertyValueFactory<>("anioDeCreacion"));
        fundadorColumn.setCellValueFactory(new PropertyValueFactory<>("fundador"));
    }

    private void cargarIndicesMarcas(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String marca = datos[0];
                long indice = Long.parseLong(datos[1]);
                int longitud = Integer.parseInt(datos[2]);
                indiceMarcasList.add(new IndiceMarca(marca, indice, longitud));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarDatosMarcas(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                String anioCreacion = datos[1];
                String fundador = datos[2];
                datosMarcasList.add(new Marca(nombre, anioCreacion, fundador));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizarTableViews() {
        // Limpia las listas actuales de datos
        indiceMarcasList.clear();
        datosMarcasList.clear();

        // Recarga los datos desde los archivos
        cargarIndicesMarcas("src/main/resources/datos/Marcas_txt/indiceMarcasOrdenado.txt");
        cargarDatosMarcas("src/main/resources/datos/Marcas_txt/datosMarcas.txt");

        // Refresca ambos TableView para mostrar los datos actualizados
        idsMarcasOrdenadosTableView.setItems(indiceMarcasList);
        DatosMarcasTableView.setItems(datosMarcasList);

    }

    //Prueba

    // Método para imprimir el contenido de indiceMarcasList
    public void imprimirIndicesMarcas() {
        System.out.println("Índices de Marcas:");
        for (IndiceMarca indice : indiceMarcasList) {
            System.out.println("Marca: " + indice.getMarca() +
                               ", Índice: " + indice.getIndice() +
                               ", Longitud: " + indice.getLongitud());
        }
    }

    // Método para imprimir el contenido de datosMarcasList
    public void imprimirDatosMarcas() {
        System.out.println("Datos de Marcas:");
        for (Marca marca : datosMarcasList) {
            System.out.println("Nombre: " + marca.getNombre() +
                               ", Año de Creación: " + marca.getAnioDeCreacion() +
                               ", Fundador: " + marca.getFundador());
        }
    }


    // Fin prueba
}
