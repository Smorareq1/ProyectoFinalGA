package Objetos;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GestorDeArchivos {

    // Crear un diccionario de marcas
    public static Map<String, Marca> diccionarioNombreMarcas = new HashMap<>(); // Usar HashMap para almacenar las marcas

    // Método para mostrar marcas en un Alert
    public static void mostrarMarcas() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Lista de Marcas");
        alert.setHeaderText(null); // No header text
        StringBuilder marcas = new StringBuilder();

        if (diccionarioNombreMarcas.isEmpty()) {
            marcas.append("No hay marcas en la lista.");
        } else {
            for (Map.Entry<String, Marca> entry : diccionarioNombreMarcas.entrySet()) {
                Marca marca = entry.getValue(); // Obtener el objeto Marca
                marcas.append("Nombre: ").append(entry.getKey()) // Obtener el nombre de la marca
                      .append(", Año de Creación: ").append(marca.getAnioDeCreacion())
                      .append(", Fundador: ").append(marca.getFundador())
                      .append("\n");
            }
        }

        alert.setContentText(marcas.toString());
        alert.showAndWait(); // Mostrar la alerta y esperar a que se cierre
    }
}
