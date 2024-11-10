package Controllers;

import Objetos.GestorDeArchivos;
import Objetos.IndiceVIN;
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

public class idVinController
{
    @FXML
    private TableView idsTiposOrdenadosTableView;

    @FXML
    private TableColumn VinIdColumn;
    @FXML
    private TableColumn  indiceIdColumn;
    @FXML
    private TableColumn longitudIdColumn;

    @FXML
    private TableView DatosVinTableView;
    @FXML
    private TableColumn  nombreColumn;
    @FXML
    private TableColumn  anioColumn;

    @FXML
    private ImageView refreshImg;
    @FXML
    private ImageView tipoSearchImage;

    @FXML
    private TextField VinSearchTxt;
}
