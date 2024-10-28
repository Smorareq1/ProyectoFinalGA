package Objetos;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;

public class idMarca {

    private static final String DATA_FILE = "src/main/resources/datos/Marcas_txt/datosMarcas.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Marcas_txt/indiceMarcas.txt";

    // Método para agregar una nueva marca sin alterar los índices existentes
    public static void agregarMarca(String nombre, Marca nuevaMarca) throws IOException {
        // Añadir la nueva marca al diccionario
        GestorDeArchivos.diccionarioNombreMarcas.put(nombre, nuevaMarca);

        // Escribir solo la nueva marca al final del archivo de datos
        escribirNuevaMarcaDatos(nuevaMarca);

        // Actualizar el archivo de índices con la nueva marca al final
        agregarNuevoIndice(nombre, nuevaMarca);
        mostrarIndicesOrdenados();
    }

    // Escribir solo la nueva marca al final del archivo de datos
    public static void escribirNuevaMarcaDatos(Marca nuevaMarca) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "rw")) {
            datosFile.seek(datosFile.length()); // Ir al final del archivo
            datosFile.writeBytes(nuevaMarca.toString() + "\n"); // Añadir nueva marca
        }
    }

    // Añadir el nuevo índice al final del archivo de índices
    public static void agregarNuevoIndice(String nombre, Marca nuevaMarca) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r");
             BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE, true))) {

            long posicionInicial = datosFile.length() - nuevaMarca.toString().length() - 1; // Calcular la posición de la nueva marca
            int longitud = nuevaMarca.toString().length(); // Obtener la longitud de la marca

            // Escribir el nuevo índice al final del archivo
            indexWriter.write(nombre + "," + posicionInicial + "," + longitud + "\n");
        }
    }

    // Leer y mostrar los índices en orden alfabético, guardando en un archivo temporal
    public static void mostrarIndicesOrdenados() throws IOException {
        List<String> indices = new ArrayList<>();

        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE))) {
            String linea;
            while ((linea = indexReader.readLine()) != null) {
                indices.add(linea);
            }
        }

        // Ordenar alfabéticamente por nombre de marca
        indices.sort(Comparator.comparing(linea -> linea.split(",")[0]));

        // Escribir los índices ordenados en un archivo temporal sin modificar el original
        String sortedIndexFile = "src/main/resources/datos/Marcas_txt/indiceMarcasOrdenado.txt";
        try (BufferedWriter sortedWriter = new BufferedWriter(new FileWriter(sortedIndexFile))) {
            for (String indice : indices) {
                sortedWriter.write(indice + "\n");
            }
        }
    }

    // Método para eliminar una marca por nombre y actualizar archivos
    public static void eliminarMarca(String nombreMarca) throws IOException {
        // Verificar si la marca existe en el diccionario
        Marca marcaAEliminar = GestorDeArchivos.diccionarioNombreMarcas.remove(nombreMarca);
        if (marcaAEliminar == null) {
            System.out.println("Marca no encontrada.");
            return;
        }

        // Regenerar el archivo de datos sin la marca eliminada
        regenerarArchivoDatosSinMarca(nombreMarca);

        // Regenerar el archivo de índices sin la marca eliminada
        regenerarArchivoIndicesSinMarca(nombreMarca);

        // Mostrar los índices ordenados después de la eliminación
        mostrarIndicesOrdenados();
    }

    // Regenerar el archivo de datos sin la marca eliminada
    private static void regenerarArchivoDatosSinMarca(String nombreMarca) throws IOException {
        List<Marca> marcasRestantes = new ArrayList<>();

        try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
            String linea;
            while ((linea = datosReader.readLine()) != null) {
                Marca marca = new Marca(linea); // Asegúrate de que este constructor existe
                if (!marca.getNombre().equalsIgnoreCase(nombreMarca)) {
                    marcasRestantes.add(marca);
                }
            }
        }

        // Escribir las marcas restantes en el archivo de datos
        try (BufferedWriter datosWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Marca marca : marcasRestantes) {
                datosWriter.write(marca.toString() + "\n");
            }
        }
    }

    // Regenerar el archivo de índices sin la marca eliminada
    private static void regenerarArchivoIndicesSinMarca(String nombreMarca) throws IOException {
        // Crear un nuevo archivo de índices
        try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE))) {
            // Leer el archivo de datos y agregar marcas restantes al índice
            try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
                String linea;
                long posicionActual = 0; // Para calcular la nueva posición
                while ((linea = datosReader.readLine()) != null) {
                    Marca marca = new Marca(linea); // Asegúrate de que este constructor existe
                    if (!marca.getNombre().equalsIgnoreCase(nombreMarca)) {
                        int longitud = linea.length();
                        // Escribir el nuevo índice
                        indexWriter.write(marca.getNombre() + "," + posicionActual + "," + longitud + "\n");
                        posicionActual += longitud + 1; // Actualiza la posición actual (incluye salto de línea)
                    }
                }
            }
        }
    }



    // Método para buscar por nombre de marca en el archivo de índices
    public static void buscarPorNombre(String nombre) throws IOException {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE));
             RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r")) {

            String linea;
            while ((linea = indexReader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].equalsIgnoreCase(nombre)) {
                    long posicionInicial = Long.parseLong(partes[1]);
                    int longitud = Integer.parseInt(partes[2]);

                    // Buscar y mostrar el registro en el archivo de datos
                    datosFile.seek(posicionInicial);
                    byte[] registro = new byte[longitud];
                    datosFile.readFully(registro);

                    //Alerta
                    generarAlerta("Registro encontrado: " + new String(registro));
                    return;
                }
            }
            System.out.println("Marca no encontrada.");
        }
    }

    private static void generarAlerta(String mensaje) {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle("Alerta");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para buscar por ID (posición inicial) en el archivo de índices
    public static void buscarPorId(long id) throws IOException {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE));
             RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r")) {

            String linea;
            while ((linea = indexReader.readLine()) != null) {
                String[] partes = linea.split(",");
                long posicionInicial = Long.parseLong(partes[1]);

                if (posicionInicial == id) {
                    int longitud = Integer.parseInt(partes[2]);

                    // Buscar y mostrar el registro en el archivo de datos
                    datosFile.seek(posicionInicial);
                    byte[] registro = new byte[longitud];
                    datosFile.readFully(registro);
                    System.out.println("Registro encontrado: " + new String(registro));
                    return;
                }
            }
            System.out.println("ID no encontrado.");
        }
    }
}
