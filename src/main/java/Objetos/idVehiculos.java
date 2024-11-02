package Objetos;

import java.io.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class idVehiculos {

    private static final String DATA_FILE = "src/main/resources/datos/Vehiculos_txt/datosVehiculos.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Vehiculos_txt/indiceVehiculos.txt";
    private static final String INDEX_SORTED_FILE ="src/main/resources/datos/Vehiculos_txt/indiceVehiculosOrdenado.txt";

    public static void agregarVehiculo(String nombre, Vehiculo nuevoVehiculo) throws IOException {
        GestorDeArchivos.diccionarioNombreVehiculos.put(nombre, nuevoVehiculo);

        escribirNuevoVehiculoDatos(nuevoVehiculo);

        agregarNuevoIndice(nombre, nuevoVehiculo);
        idMarca.mostrarIndicesOrdenados(INDEX_FILE,INDEX_SORTED_FILE); //Se le manda el archivo de índices ordenado
    }

    private static void escribirNuevoVehiculoDatos(Vehiculo nuevoVehiculo) throws IOException {
            try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "rw")) {
                datosFile.seek(datosFile.length());
                datosFile.writeBytes(nuevoVehiculo.toString() + "\n");

            }
    }

    private static void agregarNuevoIndice(String nombre, Vehiculo nuevoVehiculo) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r");
             BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE, true)) ) {

            long posicionInicial = datosFile.length() - nuevoVehiculo.toString().length() - 1; // Calcular la posición de la nueva marca
            int longitud = nuevoVehiculo.toString().length(); // Obtener la longitud de la marca

            // Escribir el nuevo índice al final del archivo
            indexWriter.write(nombre + "," + posicionInicial + "," + longitud + "\n");
        }
    }

    public static void buscarPorNombre(IndiceVehiculo indice, String placa){
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

                if (nombreIndice.equals(placa)) {
                    datosFile.seek(posicionInicial);
                    byte[] datos = new byte[longitud];
                    datosFile.readFully(datos);
                    idMarca.generarAlerta("Resgitro encontrado: " + new String(datos));
                    return;
                }
            }
            System.out.println("No se encontró la marca.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarVehiculo(String placa) throws IOException {
        Vehiculo VehiculoAEliminar = GestorDeArchivos.diccionarioNombreVehiculos.get(placa);

        if (VehiculoAEliminar == null) {
            System.out.println("Vehiculo no encontrado.");
            return;
        }

        GestorDeArchivos.diccionarioNombreVehiculos.remove(placa);

        regenerarArchivoDatosSinVehiculo(placa);
        regenerarArchivoIndiceSinVehiculo(placa);

        idMarca.mostrarIndicesOrdenados(INDEX_FILE,INDEX_SORTED_FILE); //Se le manda el archivo de índices ordenado

    }

    private static void regenerarArchivoDatosSinVehiculo(String placa) throws IOException{
         List<Vehiculo> vehiculosRestantes = new ArrayList<>();

        try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
            String linea;
            while ((linea = datosReader.readLine()) != null) {
                Vehiculo vehiculo = new Vehiculo(linea); // Asegúrate de que este constructor existe
                if (!vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                    vehiculosRestantes.add(vehiculo);
                }
            }
        }

        // Escribir las marcas restantes en el archivo de datos
        try (BufferedWriter datosWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Vehiculo vehiculo : vehiculosRestantes) {
                datosWriter.write(vehiculo.toString() + "\n");
            }
        }
    }

    private static void regenerarArchivoIndiceSinVehiculo(String placa) throws IOException{
        // Crear un nuevo archivo de índices
        try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE))) {
            // Leer el archivo de datos y agregar marcas restantes al índice
            try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
                String linea;
                long posicionActual = 0; // Para calcular la nueva posición
                while ((linea = datosReader.readLine()) != null) {
                    Vehiculo vehiculo = new Vehiculo(linea);
                    if (!vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                        int longitud = linea.length();
                        // Escribir el nuevo índice
                        indexWriter.write(vehiculo.getPlaca() + "," + posicionActual + "," + longitud + "\n");
                        posicionActual += longitud + 1; // Actualiza la posición actual (incluye salto de línea)
                    }
                }
            }
        }
    }


}
