package Objetos;

import java.io.*;

public class idLinea {

    private static final String DATA_FILE = "src/main/resources/datos/Lineas_txt/datosLineas.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Lineas_txt/indiceLineas.txt";
    private static final String INDEX_SORTED_FILE ="src/main/resources/datos/Lineas_txt/indiceLineasOrdenado.txt";

    public static void agregarLinea(String nombre, Linea nuevaLinea) throws IOException {
        GestorDeArchivos.diccionarioNombreLineas.put(nombre, nuevaLinea);

        escribirNuevaLineaDatos(nuevaLinea);

        agregarNuevoIndice(nombre, nuevaLinea);
        idMarca.mostrarIndicesOrdenados(INDEX_FILE,INDEX_SORTED_FILE); //Se le manda el archivo de índices ordenado
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

   public static void buscarPorNombre(IndiceLinea indice ,String nombre) throws IOException {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE));
             RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r")) {

            String linea;
            while ((linea = indexReader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length != 3) {
                    System.out.println("Formato incorrecto en el índice.");
                    continue;
                }

                String nombreIndice = partes[0].trim();
                long posicionInicial;
                int longitud;

                try {
                    posicionInicial = indice.getIndice();
                    longitud = indice.getLongitud();
                } catch (NumberFormatException e) {
                    System.out.println("Error en el formato de posición o longitud en el índice.");
                    continue;
                }

                // Verifica si el nombre en el índice coincide con el nombre de búsqueda
                if (nombreIndice.equalsIgnoreCase(nombre)) {
                    // Lee el registro desde el archivo de datos usando posición y longitud del índice
                    datosFile.seek(posicionInicial);
                    byte[] registro = new byte[longitud];
                    datosFile.readFully(registro);

                    // Muestra el registro encontrado
                    idMarca.generarAlerta("Registro encontrado: " + new String(registro));
                    return;
                }
            }
            System.out.println("Linea no encontrada.");
        }
    }


}
