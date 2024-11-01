package Controllers;

import Objetos.IndiceTipo;
import Objetos.Tipo;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;

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
    private TableColumn<Tipo, String> fundadorColumn;

    @FXML
    private ImageView refreshImg;

    @FXML
    private ImageView tipoSearchImage;

    @FXML
    private TextField tipoSearchTxt;
}
