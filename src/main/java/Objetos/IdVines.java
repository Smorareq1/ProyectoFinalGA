package Objetos;

import java.util.*;
import java.io.*;

public class IdVines {

private static final String DATA_FILE = "src/main/resources/datos/VIN_txt/datosVIN.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/VIN_txt/indiceVIN.txt";
    private static int primeraPosicion = -1;

    public static Set<String> vinUnicos = new HashSet<>();
    public static List<IndiceVIN> listaIndiceVines = new ArrayList<>();

    public static void agregarNuevosVins(List<DatosVines> vins) {
        try {
            cargarIndice();

            // Se prepara el archivo para agregar nuevos VINs
            FileWriter dataWriter = new FileWriter(DATA_FILE, true);
            long posicionInicial = new File(DATA_FILE).length(); // Calcular la posición inicial

            // Agregar cada VIN nuevo
            for (DatosVines vin : vins) {
                if (!vinUnicos.contains(vin.getVin())) {
                    vinUnicos.add(vin.getVin());

                    // Escribimos el VIN en el archivo de datos con sus detalles
                    String entradaDatos = vin.getNombreMarca() + "," + vin.getNombreLinea() + "," + vin.getPlaca() + "," + vin.getVin();
                    dataWriter.write(entradaDatos + "\n");

                    // Almacenamos la posición inicial y longitud de cada registro
                    int longitudRegistro = entradaDatos.length();
                    listaIndiceVines.add(new IndiceVIN(vin.getVin(), (int) posicionInicial, longitudRegistro, -1)); // Inicializamos siguiente como -1
                    posicionInicial += longitudRegistro + 1; // Sumamos longitud del registro y el salto de línea
                } else {
                    System.out.println("El VIN " + vin.getVin() + " ya existe en el índice. No se agregará duplicado.");
                }
            }
            dataWriter.close();

            ajustarPosicionesSiguientes(); // Ajustamos las posiciones "siguiente"
            guardarIndice();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cargarIndice() {
        try {
            File indexFile = new File(INDEX_FILE);
            if (indexFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(INDEX_FILE))) {
                    String line;
                    int lineCount = 0;
                    while ((line = reader.readLine()) != null) {
                        if (lineCount == 0) {
                            primeraPosicion = Integer.parseInt(line); // Primera posición (aunque no se usa directamente)
                        } else {
                            String[] data = line.split(",");
                            String vin = data[0];
                            int posicionInicial = Integer.parseInt(data[1]);
                            int longitud = Integer.parseInt(data[2]);
                            int siguiente = Integer.parseInt(data[3]);
                            listaIndiceVines.add(new IndiceVIN(vin, posicionInicial, longitud, siguiente));
                            vinUnicos.add(vin);
                        }
                        lineCount++;
                    }
                }
            } else {
                primeraPosicion = -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ajustarPosicionesSiguientes() {
        // Primero, ordenamos los VINs alfabéticamente
        List<String> vinOrdenados = new ArrayList<>(vinUnicos);
        Collections.sort(vinOrdenados);

        // Ahora, ajustamos el campo "siguiente" basado en la posición de cada VIN en el archivo de datos
        for (int i = 0; i < vinOrdenados.size(); i++) {
            String vinActual = vinOrdenados.get(i);

            // Obtenemos el índice en listaIndiceVines del VIN actual
            IndiceVIN currentIndice = obtenerIndicePorVin(vinActual);

            // Si no es el último VIN, calculamos el siguiente VIN
            if (i < vinOrdenados.size() - 1) {
                String vinSiguiente = vinOrdenados.get(i + 1);
                IndiceVIN siguienteIndice = obtenerIndicePorVin(vinSiguiente);

                // Actualizamos el "siguiente" con la posición del siguiente VIN
                currentIndice.siguiente = siguienteIndice.posicionInicial;
            } else {
                // Si es el último VIN, asignamos -1
                currentIndice.siguiente = -1;
            }
        }
    }

    private static IndiceVIN obtenerIndicePorVin(String vin) {
        // Obtenemos el índice de un VIN en la lista de índices
        for (IndiceVIN indice : listaIndiceVines) {
            if (indice.getVin().equals(vin)) {
                return indice;
            }
        }
        return null;
    }

    public static void guardarIndice() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INDEX_FILE))) {
            writer.println("0"); // Temporalmente escribe "0" como posición inicial (ajustaremos esto más tarde)

            // Escribir todos los registros en el archivo
            for (IndiceVIN indice : listaIndiceVines) {
                writer.println(indice.toString()); // Guardamos los registros en el formato adecuado
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ahora, después de haber escrito todos los registros, encontrar la posición del VIN más pequeño
        int posicionInicial = encontrarPosicionMasPequenaEnArchivo();

        // Reescribir la primera línea del archivo con la posición correcta
        actualizarPosicionInicialEnArchivo(posicionInicial);
    }

    private static void actualizarPosicionInicialEnArchivo(int posicionInicial) {
        try {
            List<String> lineas = new ArrayList<>();

            // Leer el archivo y almacenar todas las líneas en una lista
            try (BufferedReader reader = new BufferedReader(new FileReader(INDEX_FILE))) {
                String linea;
                boolean primeraLinea = true;
                while ((linea = reader.readLine()) != null) {
                    if (primeraLinea) {
                        lineas.add(String.valueOf(posicionInicial)); // Agregar la posición inicial en la primera línea
                        primeraLinea = false;
                    } else {
                        lineas.add(linea); // Agregar el resto de las líneas sin cambios
                    }
                }
            }

            // Escribir todas las líneas de nuevo al archivo con la primera línea actualizada
            try (PrintWriter writer = new PrintWriter(new FileWriter(INDEX_FILE))) {
                for (String linea : lineas) {
                    writer.println(linea);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int encontrarPosicionMasPequenaEnArchivo() {
        List<String> vinOrdenados = new ArrayList<>(vinUnicos);
        Collections.sort(vinOrdenados); // Ordena los VINs alfabéticamente
        String vinMasPequeno = vinOrdenados.get(0); // Obtiene el VIN más pequeño

        try (BufferedReader reader = new BufferedReader(new FileReader(INDEX_FILE))) {
            String line;
            int posicionCaracter = 0; // Posición acumulada en caracteres

            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(","); // Asumiendo que los datos están separados por comas
                if (datos[0].equals(vinMasPequeno)) { // Compara el VIN de la línea actual con el más pequeño
                    return posicionCaracter; // Retorna la posición en caracteres del inicio del VIN más pequeño
                }
                // Sumar la longitud de la línea y el salto de línea al contador de posición
                posicionCaracter += line.length();
            }

            System.out.println("El VIN más pequeño no se encontró en el archivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1; // Si no se encontró el VIN más pequeño, retorna -1
    }



    public static void main(String[] args) {
        List<DatosVines> vins = new ArrayList<>();
        vins.add(new DatosVines("Honda", "Hrv", "P057GMF", "VIN123"));
        vins.add(new DatosVines("GMC", "Chevrolet", "P145RTM", "VIN456"));
        vins.add(new DatosVines("Toyota", "Yaris", "P856HQR", "VIN278"));
        //vins.add(new DatosVines("Ford", "Fiesta", "P123ABC", "VIN0065"));

        agregarNuevosVins(vins);
    }
}
