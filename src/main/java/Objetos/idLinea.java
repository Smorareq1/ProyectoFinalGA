package Objetos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class idLinea {

    private static final String DATA_FILE = "src/main/resources/datos/Lineas_txt/datosLineas.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Lineas_txt/indiceLineas.txt";
     private static final String INDEX_SORTED_FILE ="src/main/resources/datos/Lineas_txt/indiceLineasOrdenado.txt";

    public static void agregarLinea(String nombre, Linea nuevaLinea) throws IOException {
        GestorDeArchivos.diccionarioNombreLineas.put(nombre, nuevaLinea);

        escribirNuevaLineaDatos(nuevaLinea);

        agregarNuevoIndice(nombre, nuevaLinea);
        idMarca.mostrarIndicesOrdenados(INDEX_SORTED_FILE);
    }

    private static void escribirNuevaLineaDatos(Linea nuevaLinea) throws IOException {
            try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "rw")) {
                datosFile.seek(datosFile.length());
                datosFile.writeBytes(nuevaLinea.toString() + "\n");

            }
    }

    private static void agregarNuevoIndice(String nombre, Linea nuevaLinea) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r");
             BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE, true))) {

            long posicionInicial = datosFile.length() - nuevaLinea.toString().length() - 1; // Calcular la posición de la nueva marca
            int longitud = nuevaLinea.toString().length(); // Obtener la longitud de la marca

            // Escribir el nuevo índice al final del archivo
            indexWriter.write(nombre + "," + posicionInicial + "," + longitud + "\n");
        }
    }

}
