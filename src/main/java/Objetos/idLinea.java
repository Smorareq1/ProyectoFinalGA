package Objetos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

   public static void eliminarLinea(String nombreLinea) throws IOException{
        Linea lineaAEliminar = GestorDeArchivos.diccionarioNombreLineas.get(nombreLinea);
        if(lineaAEliminar == null){
            System.out.println("La linea no existe");
            return;
        }
        GestorDeArchivos.diccionarioNombreLineas.remove(nombreLinea);

        regenerarArchivoDatosSinLinea(nombreLinea);
        regenerarArchivoIndiceSinLinea(nombreLinea);

        idMarca.mostrarIndicesOrdenados(INDEX_FILE,INDEX_SORTED_FILE); //Se le manda el archivo de índices ordenado

   }

    private static void regenerarArchivoDatosSinLinea(String nombreLinea) throws IOException{
         List<Linea> lineasRestantes = new ArrayList<>();

        try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
            String linea;
            while ((linea = datosReader.readLine()) != null) {
                Linea lineaObjeto = new Linea(linea); // Asegúrate de que este constructor existe
                if (!lineaObjeto.getNombreLinea().equalsIgnoreCase(nombreLinea)) {
                    lineasRestantes.add(lineaObjeto);
                }
            }
        }

        // Escribir las marcas restantes en el archivo de datos
        try (BufferedWriter datosWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Linea lineaObj : lineasRestantes) {
                datosWriter.write(lineaObj.toString() + "\n");
            }
        }
    }

    private static void regenerarArchivoIndiceSinLinea(String nombreLinea) throws IOException{
        // Crear un nuevo archivo de índices
        try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE))) {
            // Leer el archivo de datos y agregar marcas restantes al índice
            try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
                String linea;
                long posicionActual = 0; // Para calcular la nueva posición
                while ((linea = datosReader.readLine()) != null) {
                    Linea LineaObj = new Linea(linea);
                    if (!LineaObj.getNombreLinea().equalsIgnoreCase(nombreLinea)) {
                        int longitud = linea.length();
                        // Escribir el nuevo índice
                        indexWriter.write(LineaObj.getNombreLinea() + "," + posicionActual + "," + longitud + "\n");
                        posicionActual += longitud + 1; // Actualiza la posición actual (incluye salto de línea)
                    }
                }
            }
        }
    }


}
