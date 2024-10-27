package Objetos;

import java.io.*;
import java.util.*;

public class idMarca {

    private static final String DATA_FILE = "src/main/resources/datos/Marcas_txt/datosMarcas.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Marcas_txt/indiceMarcas.txt";

    // Método para llenar los datos y generar los archivos de datos y de índices
    public static void idLlenarDatos() {
        try {
            escribirDatosSecuenciales(GestorDeArchivos.diccionarioNombreMarcas);
            escribirArchivoDeIndices(GestorDeArchivos.diccionarioNombreMarcas);
            mostrarIndicesOrdenados();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Escribir datos de las marcas en el archivo de datos secuencialmente
    public static void escribirDatosSecuenciales(Map<String, Marca> marcas) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "rw")) {
            datosFile.setLength(0); // Limpiar archivo
            for (Marca marca : marcas.values()) {
                datosFile.writeBytes(marca.toString() + "\n");
            }
        }
    }

    // Escribir el archivo de índices, con marcas en el mismo orden
    public static void escribirArchivoDeIndices(Map<String, Marca> marcas) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r");
             BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE))) {

            long posicionInicial = 0;
            for (Marca marca : marcas.values()) {
                String linea = datosFile.readLine();
                int longitud = linea.length();

                // Escribir índice en el archivo
                indexWriter.write(marca.getNombre() + "," + posicionInicial + "," + longitud + "\n");
                posicionInicial += longitud + 1; // Avanzar al siguiente registro (incluye salto de línea)
            }
        }
    }

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
    public static void eliminarMarca(Marca marcaAEliminar) throws IOException {
        // Verificar si la marca existe en el diccionario
        marcaAEliminar = GestorDeArchivos.diccionarioNombreMarcas.remove(marcaAEliminar.getNombre());
        if (marcaAEliminar == null) {
            System.out.println("Marca no encontrada.");
            return;
        }

        // Regenerar el archivo de datos sin la marca eliminada
        escribirDatosSecuenciales(GestorDeArchivos.diccionarioNombreMarcas);

        // Regenerar el archivo de índices sin la marca eliminada
        escribirArchivoDeIndices(GestorDeArchivos.diccionarioNombreMarcas);

        // Mostrar los índices ordenados después de la eliminación (opcional)
        mostrarIndicesOrdenados();
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
                    System.out.println("Registro encontrado: " + new String(registro));
                    return;
                }
            }
            System.out.println("Marca no encontrada.");
        }
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
