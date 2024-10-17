package Controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import Objetos.GestorDeArchivos;

public class InicioController {

    @FXML
    private Text marcasRegistradasText;

    @FXML
    private Text tiposRegistradosText;

    @FXML
    private Text lineasRegistradasText;

    @FXML
    private Text vehiculosRegistradosText;

    @FXML
    public void initialize() {
        // Obtén el tamaño del diccionarios
        int marcasSize = GestorDeArchivos.diccionarioNombreMarcas.size();
        int tiposSize = GestorDeArchivos.diccionarioNombreTipos.size();
        int lineasSize = GestorDeArchivos.diccionarioNombreLineas.size();
        int vehiculosSize = GestorDeArchivos.diccionarioNombreVehiculos.size();

        // Actualiza el valor del Text en la interfaz
        marcasRegistradasText.setText(String.valueOf(marcasSize));
        tiposRegistradosText.setText(String.valueOf(tiposSize));
        lineasRegistradasText.setText(String.valueOf(lineasSize));
        vehiculosRegistradosText.setText(String.valueOf(vehiculosSize));
    }

}
