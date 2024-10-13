package Objetos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GestorDeArchivos {

    // Crear un diccionario de marcas
    public static Map<String, Marca> diccionarioNombreMarcas = new HashMap<>();

    public static void cargarDatosDesdeJson() {
        Gson gson = new Gson();
        Path path = Paths.get("src/main/resources/datos/marcas.json"); // Path to the JSON file

        try {
            // Read all bytes from the JSON file
            String json = Files.readString(path);

            // Deserialize JSON into a Map<String, Marca>
            Map<String, Marca> marcasMap = gson.fromJson(json, new com.google.gson.reflect.TypeToken<Map<String, Marca>>(){}.getType());

            // Initialize the dictionary with the data from JSON
            if (marcasMap != null) {
                diccionarioNombreMarcas.putAll(marcasMap);
                System.out.println("Datos cargados correctamente desde el archivo JSON.");
            } else {
                System.out.println("No se encontraron datos en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los datos desde JSON: " + e.getMessage());
        }
    }


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
                Marca marca = entry.getValue();
                marcas.append("Nombre: ").append(entry.getKey())
                      .append(", Año de Creación: ").append(marca.getAnioDeCreacion())
                      .append(", Fundador: ").append(marca.getFundador())
                      .append("\n");
            }
        }

        alert.setContentText(marcas.toString());
        alert.showAndWait();
    }

    // Método para guardar los datos en un archivo JSON en una carpeta llamada 'datos'
    public static void guardarDatosEnJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(diccionarioNombreMarcas);

        // Ruta del archivo JSON en el directorio 'resources/datos'
        Path path = Paths.get("src/main/resources/datos/marcas.json");

        try {
            // Crear directorio si no existe
            Files.createDirectories(path.getParent());

            // Escribir el JSON en el archivo
            try (FileWriter writer = new FileWriter(path.toFile())) {
                writer.write(json);
                System.out.println("Datos guardados correctamente en el archivo JSON.");
                System.out.println("Ruta del archivo: " + path.toAbsolutePath()); // Muestra la ruta completa del archivo
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos en JSON: " + e.getMessage());
        }
    }
}
