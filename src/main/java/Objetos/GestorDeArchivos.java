package Objetos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GestorDeArchivos {

    // Diccionarios
    public static Map<String, Marca> diccionarioNombreMarcas = new HashMap<>();

    public static Map<String, Tipo> diccionarioNombreTipos = new HashMap<>();

    public static Map<String, Linea> diccionarioNombreLineas = new HashMap<>();

    public static Map<String, Vehiculo> diccionarioNombreVehiculos = new HashMap<>();

    //Sets para evitar duplicados en propiedades de vehiculo
    public static Set<String> setPlacas = new HashSet<>();
    public static Set<String> setChasis = new HashSet<>();
    public static Set<String> setMotores = new HashSet<>();
    public static Set<String> setVins = new HashSet<>();

    ///////////////////////////////////// MARCAS ///////////////////////////////////////////////////


    // Método para guardar los datos en un archivo JSON en una carpeta llamada 'datos'
    public static void guardarMarcasDatosEnJson() {
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

    ///////////////////////////////////// LINEAS ///////////////////////////////////////////////////

    public static void guardarLineasEnJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Usar PrettyPrinting para que el JSON sea más legible
        String json = gson.toJson(diccionarioNombreLineas);

        // Ruta del archivo JSON donde se guardarán las líneas
        Path path = Paths.get("src/main/resources/datos/lineas.json");

        try {
            // Crear el directorio si no existe
            Files.createDirectories(path.getParent());

            // Escribir el JSON en el archivo
            try (FileWriter writer = new FileWriter(path.toFile())) {
                writer.write(json);
                writer.flush();
                System.out.println("Líneas guardadas correctamente en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar las líneas en JSON: " + e.getMessage());
        }
    }

    public static void cargarLineasDesdeJson() {
        Gson gson = new Gson();
        Path path = Paths.get("src/main/resources/datos/lineas.json");

        try {
            // Leer todas las líneas del archivo JSON
            String json = Files.readString(path);
            System.out.println("Contenido del archivo JSON: " + json);  // Verifica el contenido

            // Deserializar el JSON en un Map<String, Linea>
            Map<String, Linea> lineasMap = gson.fromJson(json, new com.google.gson.reflect.TypeToken<Map<String, Linea>>() {}.getType());

            // Inicializar el diccionario de líneas con los datos desde el JSON
            if (lineasMap != null) {
                diccionarioNombreLineas.putAll(lineasMap);
                System.out.println("Líneas cargadas: " + lineasMap);  // Verifica los datos cargados
            } else {
                System.out.println("No se encontraron líneas en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al cargar las líneas desde JSON: " + e.getMessage());
        }
    }

    public static void buscarYEliminarLineaPorMarca(String nombreMarca) {
        Iterator<Map.Entry<String, Linea>> it = diccionarioNombreLineas.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Linea> entry = it.next();
            Linea linea = entry.getValue();

            if (linea.getNombreMarcaDeLinea().equals(nombreMarca)) {
                it.remove(); // Eliminar la entrada actual si coincide la marca
            }
        }
    }

    public static void editarLineasPorMarca(String nombreMarca, Marca nuevaMarca) {
        for (Map.Entry<String, Linea> entry : diccionarioNombreLineas.entrySet()) {
            Linea linea = entry.getValue();

            // Verificamos si el nombre de la marca en la línea coincide con el que estamos editando
            if (linea.getNombreMarcaDeLinea().equals(nombreMarca)) {
                // Actualizamos el nombre de la marca en la línea
                linea.setNombreMarcaDeLinea(nuevaMarca.getNombre());

                // También actualizamos el objeto Marca dentro de la línea si es necesario
                Marca marca = linea.getMarca();
                if (marca != null && marca.getNombre().equals(nombreMarca)) {

                    //Editar los valores de marca que fueron modificados
                    marca.setNombre(nuevaMarca.getNombre());
                    marca.setAnioDeCreacion(nuevaMarca.anioDeCreacion);
                    marca.setFundador(nuevaMarca.fundador);


                    linea.setMarca(marca); // Aseguramos que la línea tenga la referencia actualizada a la marca
                }
            }
        }
    }

    public static void printLineas(){
        for (Map.Entry<String, Linea> entry : diccionarioNombreLineas.entrySet()) {
            Linea linea = entry.getValue();
            System.out.println(" ");
            System.out.println("Nombre de la marca: " + linea.getNombreMarcaDeLinea());

            //Informacion de la marca
            System.out.println("Año de creacion de la marca: " + linea.getMarca().getAnioDeCreacion());
            System.out.println("Fundador de la marca: " + linea.getMarca().getFundador());

            System.out.println("Nombre de la linea: " + linea.getNombreLinea());
            System.out.println("Año de la linea: " + linea.getAnioLinea());
            System.out.println(" ");
        }
    }

    //////////////////////////////// TIPOS ///////////////////////////////////

    public static void guardarTiposEnJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(diccionarioNombreTipos);

        // Ruta del archivo JSON donde se guardarán los tipos
        Path path = Paths.get("src/main/resources/datos/tipos.json");

        try {
            // Crear el directorio si no existe
            Files.createDirectories(path.getParent());

            // Escribir el JSON en el archivo
            try (FileWriter writer = new FileWriter(path.toFile())) {
                writer.write(json);
                writer.flush();
                System.out.println("Tipos guardados correctamente en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los tipos en JSON: " + e.getMessage());
        }
    }

    public static void CargarTiposDesdeJson(){
        Gson gson = new Gson();
        Path path = Paths.get("src/main/resources/datos/tipos.json");

        try {
            // Leer todas las líneas del archivo JSON
            String json = Files.readString(path);
            System.out.println("Contenido del archivo JSON: " + json);  // Verifica el contenido

            // Deserializar el JSON en un Map<String, Linea>
            Map<String, Tipo> tiposMap = gson.fromJson(json, new com.google.gson.reflect.TypeToken<Map<String, Tipo>>() {}.getType());

            // Inicializar el diccionario de líneas con los datos desde el JSON
            if (tiposMap != null) {
                diccionarioNombreTipos.putAll(tiposMap);
                System.out.println("Líneas cargadas: " + tiposMap);  // Verifica los datos cargados
            } else {
                System.out.println("No se encontraron líneas en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al cargar las líneas desde JSON: " + e.getMessage());
        }
    }

    //////////////////////////////// VEHICULOS ///////////////////////////////////

    public static void guardarVehiculosEnJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(diccionarioNombreVehiculos);

        // Ruta del archivo JSON donde se guardarán los vehiculos
        Path path = Paths.get("src/main/resources/datos/vehiculos.json");

        try {
            // Crear el directorio si no existe
            Files.createDirectories(path.getParent());

            // Escribir el JSON en el archivo
            try (FileWriter writer = new FileWriter(path.toFile())) {
                writer.write(json);
                writer.flush();
                System.out.println("Vehiculos guardados correctamente en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los vehiculos en JSON: " + e.getMessage());
        }
    }

    public static void cargarVehiculosDesdeJson(){
        Gson gson = new Gson();
        Path path = Paths.get("src/main/resources/datos/vehiculos.json");

        try {
            // Leer todas las líneas del archivo JSON
            String json = Files.readString(path);
            System.out.println("Contenido del archivo JSON: " + json);  // Verifica el contenido

            // Deserializar el JSON en un Map<String, Linea>
            Map<String, Vehiculo> vehiculosMap = gson.fromJson(json, new com.google.gson.reflect.TypeToken<Map<String, Vehiculo>>() {}.getType());

            // Inicializar el diccionario de líneas con los datos desde el JSON
            if (vehiculosMap != null) {
                diccionarioNombreVehiculos.putAll(vehiculosMap);
                System.out.println("Vehiculos cargados: " + vehiculosMap);  // Verifica los datos cargados
            } else {
                System.out.println("No se encontraron vehiculos en el archivo JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los vehiculos desde JSON: " + e.getMessage());
        }
    }

    public static void cargarSets(){
        for (Map.Entry<String, Vehiculo> entry : diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();
            setPlacas.add(vehiculo.getPlaca());
            setChasis.add(vehiculo.getChasis());
            setMotores.add(vehiculo.getMotor());
            setVins.add(vehiculo.getVin());
        }
    }

    public static void buscarYEliminarVehiculoPorMarca(String nombreMarca) {
        Iterator<Map.Entry<String, Vehiculo>> it = diccionarioNombreVehiculos.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Vehiculo> entry = it.next();
            Vehiculo vehiculo = entry.getValue();

            if (vehiculo.getMarca().getNombre().equals(nombreMarca)) {

                //Eliminar datos del set
                setPlacas.remove(vehiculo.getPlaca());
                setChasis.remove(vehiculo.getChasis());
                setMotores.remove(vehiculo.getMotor());
                setVins.remove(vehiculo.getVin());

                //Eliminar vehiculo
                it.remove(); // Eliminar la entrada actual si coincide la marca
            }
        }
    }

    public static void buscarYEliminarVehiculoPorLinea(String nombreLinea) {
        Iterator<Map.Entry<String, Vehiculo>> it = diccionarioNombreVehiculos.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Vehiculo> entry = it.next();
            Vehiculo vehiculo = entry.getValue();

            if (vehiculo.getLinea().getNombreLinea().equals(nombreLinea)) {
                //Eliminar datos del set
                setPlacas.remove(vehiculo.getPlaca());
                setChasis.remove(vehiculo.getChasis());
                setMotores.remove(vehiculo.getMotor());
                setVins.remove(vehiculo.getVin());


                //Eliminar vehiculo
                it.remove(); // Eliminar la entrada actual si coincide la marca
            }
        }
    }

    public static void busarYEliminarVehiculoPorTipo(String nombreTipo) {
        Iterator<Map.Entry<String, Vehiculo>> it = diccionarioNombreVehiculos.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Vehiculo> entry = it.next();
            Vehiculo vehiculo = entry.getValue();

            if (vehiculo.getTipo().getNombreTipo().equals(nombreTipo)) {
                //Eliminar datos del set
                setPlacas.remove(vehiculo.getPlaca());
                setChasis.remove(vehiculo.getChasis());
                setMotores.remove(vehiculo.getMotor());
                setVins.remove(vehiculo.getVin());

                //Eliminar vehiculo
                it.remove(); // Eliminar la entrada actual si coincide la marca
            }
        }
    }

    //Prueba

    public static void printMarcasDeVehiculos(){
        for (Map.Entry<String, Vehiculo> entry : diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();
            System.out.println(" ");
            System.out.println("Marca del vehiculo: " + vehiculo.getMarca().getNombre());
            System.out.println("Año de creacion de la marca: " + vehiculo.getMarca().getAnioDeCreacion());
            System.out.println("Fundador de la marca: " + vehiculo.getMarca().getFundador());
            System.out.println(" ");
        }
    }

    //Prueba fin

     public static void editarVehiculosPorMarca(String nombreMarca, Marca nuevaMarca) {
        for (Map.Entry<String, Vehiculo> entry : diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();

            // Verificamos si el nombre de la marca en el vehiculo coincide con el que estamos editando
            if (vehiculo.getMarca().getNombre().equals(nombreMarca)) {
                // Actualizamos el nombre de la marca en el vehiculo
                vehiculo.getMarca().setNombre(nuevaMarca.getNombre());
                vehiculo.setMarcaNombreVehiculo(nuevaMarca.getNombre());

                // También actualizamos los atributos adicionales de la marca en el vehiculo
                Marca marca = vehiculo.getMarca();
                if (marca != null && marca.getNombre().equals(nuevaMarca.getNombre())) {
                    // Editar los valores de marca que fueron modificados
                    marca.setAnioDeCreacion(nuevaMarca.getAnioDeCreacion());
                    marca.setFundador(nuevaMarca.getFundador());
                }

                // Actualizamos la marca dentro de la línea asociada al vehículo
                Linea linea = vehiculo.getLinea(); // Obtener la línea del vehículo
                if (linea != null) {

                    linea.setNombreMarcaDeLinea(nuevaMarca.getNombre()); // Actualizar el nombre de la marca en la línea

                    // Verificamos si la línea está asociada con la marca que estamos actualizando
                    if (linea.getMarca() != null && linea.getMarca().getNombre().equals(nombreMarca)) {
                        // Actualizamos la línea con la nueva marca
                        linea.getMarca().setNombre(nuevaMarca.getNombre());
                        linea.getMarca().setAnioDeCreacion(nuevaMarca.getAnioDeCreacion());
                        linea.getMarca().setFundador(nuevaMarca.getFundador());

                        // Actualizamos el nombre de la marca en la línea
                        linea.setNombreMarcaDeLinea(nuevaMarca.getNombre());
                    }
                }
            }
        }
    }

    public static void editarVehiculosDadoLineas(String nombreLineaPasado, Linea nuevaLinea) {
        for (Map.Entry<String, Vehiculo> entry : diccionarioNombreVehiculos.entrySet()) {
            Vehiculo vehiculo = entry.getValue();

            // Verificamos si el nombre de la marca en el vehiculo coincide con el que estamos editando
            if (vehiculo.getLinea().getNombreLinea().equals(nombreLineaPasado)) {
                // Actualizamos el nombre de la marca en el vehiculo
                vehiculo.getLinea().setNombreLinea(nuevaLinea.getNombreLinea());
                vehiculo.getLinea().setAnioLinea(nuevaLinea.getAnioLinea());

                // También actualizamos los atributos adicionales de la marca en el vehiculo
                Linea linea = vehiculo.getLinea();
                if (linea != null && linea.getNombreLinea().equals(nuevaLinea.getNombreLinea())) {
                    // Editar los valores de marca que fueron modificados
                    linea.setAnioLinea(nuevaLinea.getAnioLinea());
                    linea.setNombreLinea(nuevaLinea.getNombreLinea());
                }
            }
        }
    }


}
