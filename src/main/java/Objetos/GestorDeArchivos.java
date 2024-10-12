package Objetos;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GestorDeArchivos {

    // Crear una lista de marcas
    public static List<Marca> listaMarcas = new ArrayList<>();



    // Método para mostrar marcas en un Alert
    public static void mostrarMarcas() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Lista de Marcas");
        alert.setHeaderText(null); // No header text
        StringBuilder marcas = new StringBuilder();

        if (listaMarcas.isEmpty()) {
            marcas.append("No hay marcas en la lista.");
        } else {
            for (Marca marca : listaMarcas) {
                marcas.append("Nombre: ").append(marca.getNombre())
                      .append(", Año de Creación: ").append(marca.getAnioDeCreacion())
                      .append(", Fundador: ").append(marca.getFundador())
                      .append("\n");
            }
        }

        alert.setContentText(marcas.toString());
        alert.showAndWait(); // Mostrar la alerta y esperar a que se cierre
    }
}
