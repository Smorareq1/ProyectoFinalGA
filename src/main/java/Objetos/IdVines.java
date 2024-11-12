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
            cargarIndice(); // Asegúrate de que listaIndiceVines esté actualizada

            // Inicializa vinUnicos a partir de listaIndiceVines para asegurar que estén sincronizados
            vinUnicos.clear();
            for (IndiceVIN indice : listaIndiceVines) {
                vinUnicos.add(indice.getVin());
            }

            // Prepara el archivo para agregar nuevos VINs
            FileWriter dataWriter = new FileWriter(DATA_FILE, true);
            long posicionInicial = new File(DATA_FILE).length();

            for (DatosVines vin : vins) {
                // Verifica si el VIN ya existe en vinUnicos y en listaIndiceVines antes de agregar
                if (vinUnicos.contains(vin.getVin())) {
                    System.out.println("El VIN " + vin.getVin() + " ya existe en el índice. No se agregará duplicado.");
                    continue; // Saltar a la siguiente iteración si ya existe
                }

                // Agrega el VIN a vinUnicos para evitar duplicados
                vinUnicos.add(vin.getVin());

                // Escribe el VIN en el archivo de datos
                String entradaDatos = vin.getNombreMarca() + "," + vin.getNombreLinea() + "," + vin.getPlaca() + "," + vin.getVin();
                dataWriter.write(entradaDatos + "\n");

                // Calcula la longitud y añade el índice a listaIndiceVines
                int longitudRegistro = entradaDatos.length();
                listaIndiceVines.add(new IndiceVIN(vin.getVin(), (int) posicionInicial, longitudRegistro, -1));
                posicionInicial += longitudRegistro + 1;
            }

            dataWriter.close();

            // Ajusta las posiciones de los índices y guarda los cambios
            ajustarPosicionesSiguientes();
            guardarIndice();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void cargarIndice() {
        listaIndiceVines.clear(); // Limpia la lista para evitar duplicados

        try {
            File indexFile = new File(INDEX_FILE);
            if (indexFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(INDEX_FILE))) {
                    String line;
                    int lineCount = 0;
                    while ((line = reader.readLine()) != null) {
                        // Omitir líneas vacías o solo con espacios en blanco
                        if (line.trim().isEmpty()) {
                            continue;
                        }

                        if (lineCount == 0) {
                            primeraPosicion = Integer.parseInt(line);
                        } else {
                            String[] data = line.split(",");

                            // Check if the line has the expected number of elements
                            if (data.length < 4) {
                                System.out.println("Formato incorrecto en la línea del archivo de índice: " + line);
                                continue; // Skip this line if it doesn’t have enough data
                            }

                            String vin = data[0];
                            int posicionInicial = Integer.parseInt(data[1]);
                            int longitud = Integer.parseInt(data[2]);
                            int siguiente = Integer.parseInt(data[3]);
                            listaIndiceVines.add(new IndiceVIN(vin, posicionInicial, longitud, siguiente));
                            vinUnicos.add(vin); // Actualiza el set para evitar duplicados
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
        List<String> vinOrdenados = new ArrayList<>(vinUnicos);
        Collections.sort(vinOrdenados);

        Map<String, Integer> vinToBytePosition = new HashMap<>();
        int bytePosition = 0;

        for (IndiceVIN indice : listaIndiceVines) {
            vinToBytePosition.put(indice.getVin(), bytePosition);
            bytePosition += indice.toString().length() + 1;
        }

        for (int i = 0; i < vinOrdenados.size(); i++) {
            String vinActual = vinOrdenados.get(i);
            IndiceVIN currentIndice = obtenerIndicePorVin(vinActual);

            if (i < vinOrdenados.size() - 1) {
                String vinSiguiente = vinOrdenados.get(i + 1);
                currentIndice.setSiguiente(vinToBytePosition.get(vinSiguiente));
            } else {
                currentIndice.setSiguiente(-1);
            }
        }
    }

    private static IndiceVIN obtenerIndicePorVin(String vin) {
        for (IndiceVIN indice : listaIndiceVines) {
            if (indice.getVin().equals(vin)) {
                return indice;
            }
        }
        return null;
    }

    public static void guardarIndice() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INDEX_FILE))) {
            writer.println("0");

            for (IndiceVIN indice : listaIndiceVines) {
                writer.println(indice.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int posicionInicial = encontrarPosicionMasPequenaEnArchivo();
        actualizarPosicionInicialEnArchivo(posicionInicial);
    }

    private static void actualizarPosicionInicialEnArchivo(int posicionInicial) {
        try (RandomAccessFile raf = new RandomAccessFile(INDEX_FILE, "rw")) {
            raf.writeBytes(String.valueOf(posicionInicial) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int encontrarPosicionMasPequenaEnArchivo() {
        List<String> vinOrdenados = new ArrayList<>(vinUnicos);
        Collections.sort(vinOrdenados);
        String vinMasPequeno = vinOrdenados.get(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(INDEX_FILE))) {
            String line;
            int posicionCaracter = 0;

            while ((line = reader.readLine()) != null) {
                String[] datos = line.split(",");
                if (datos[0].equals(vinMasPequeno)) {
                    return posicionCaracter;
                }
                posicionCaracter += line.length() + 1;
            }

            System.out.println("El VIN más pequeño no se encontró en el archivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static DatosVines buscarVin(String vinBuscado) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE))) {
            String line;

            // Recorre el archivo de índice para encontrar el VIN buscado
            while ((line = indexReader.readLine()) != null) {
                String[] datos = line.split(",");

                if (datos.length >= 4 && datos[0].equals(vinBuscado)) {
                    // VIN encontrado, obtiene posición y longitud
                    int posicionInicial = Integer.parseInt(datos[1]);
                    int longitud = Integer.parseInt(datos[2]);
                    int siguiente = Integer.parseInt(datos[3]);

                    IndiceVIN indiceEnontrado = new IndiceVIN(datos[0], posicionInicial, longitud, siguiente);

                    // Abre el archivo de datos y utiliza seek para acceder a la posición
                    try (RandomAccessFile dataFile = new RandomAccessFile(DATA_FILE, "r")) {
                        dataFile.seek(posicionInicial);

                        // Lee el registro completo con base en la longitud obtenida
                        byte[] registroBytes = new byte[longitud];
                        dataFile.read(registroBytes);

                        String registro = new String(registroBytes);

                        // Retorna una lista con los datos del índice y el contenido del registro
                        DatosVines resultado = new DatosVines(registro);
                        return resultado;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Si el VIN no se encuentra, retorna mensaje indicando "no encontrado"
        DatosVines vinNoEncontrado = new DatosVines("No encontrado", "", "", "");
        return vinNoEncontrado;
    }

    public static void main(String[] args) {
        List<DatosVines> vins = new ArrayList<>();
        vins.add(new DatosVines("Honda", "Hrv", "P057GMF", "VIN123"));
        vins.add(new DatosVines("GMC", "Chevrolet", "P145RTM", "VIN456"));
        vins.add(new DatosVines("Toyota", "Yaris", "P856HQR", "VIN278"));
        vins.add(new DatosVines("Ford", "Fiesta", "P123ABC", "VIN0065"));

        DatosVines vinencontrado = buscarVin("VIN0065");
        System.out.println(vinencontrado.toString());

        //agregarNuevosVins(vins);
    }
}
