package Objetos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class idLinea {

    private static final String DATA_FILE = "src/main/resources/datos/Lineas_txt/datosLineas.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Lineas_txt/indiceLineas.txt";
    private static final String INDEX_SORTED_FILE ="src/main/resources/datos/Lineas_txt/indiceLineasOrdenado.txt";

    ////////////////////////////////////////// AGREGAR ///////////////////////////////////////////////////

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

    ////////////////////////////////////////// BUSQUEDA ///////////////////////////////////////////////////

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

   ////////////////////////////////////////// ELIMINAR ///////////////////////////////////////////////////

   public static void eliminarLinea(String nombreLinea) throws IOException {
       Linea lineaAEliminar = GestorDeArchivos.diccionarioNombreLineas.get(nombreLinea);
       if (lineaAEliminar == null) {
           System.out.println("La linea no existe");
           return;
       }
       GestorDeArchivos.diccionarioNombreLineas.remove(nombreLinea);

       regenerarArchivoDatosSinLinea(nombreLinea);
       regenerarArchivoIndiceSinLinea(nombreLinea);

       idMarca.mostrarIndicesOrdenados(INDEX_FILE, INDEX_SORTED_FILE); //Se le manda el archivo de índices ordenado

       //Verificar si hay vehiculos con la linea a eliminar
       List<String> vehiculosAEliminar = new ArrayList<>();
       for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
           if (vehiculo.getLinea().getNombreLinea().equalsIgnoreCase(nombreLinea)) {
               vehiculosAEliminar.add(vehiculo.getPlaca());
           }
       }

       //Eliminar los vehiculos
       for (String placa : vehiculosAEliminar) {
           idVehiculos.eliminarVehiculo(placa);
       }

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

    ////////////////////////////////////////// EDITAR ///////////////////////////////////////////////////
    public static void editarLineaEnDatos(String nombreAntiguo, Linea nuevaLinea, Linea lineaAnterior) throws IOException {
        // Lista para almacenar las marcas actualizadas
        List<Linea> lineasActualizadas = new ArrayList<>();
        int lineaLongitudAnterior = lineaAnterior.toString().length();

        // Leer el archivo de datos y actualizar la marca correspondiente
        try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
            String linea;
            while ((linea = datosReader.readLine()) != null) {
                Linea LineaObj = new Linea(linea);
                if (LineaObj.getNombreLinea().equalsIgnoreCase(nombreAntiguo)) {
                    lineasActualizadas.add(nuevaLinea);
                } else {
                    lineasActualizadas.add(LineaObj);
                }
            }
        }

        // Escribir las marcas actualizadas en el archivo de datos
        try (BufferedWriter datosWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Linea LineaObj : lineasActualizadas) {
                datosWriter.write(LineaObj.toString() + "\n");
            }
        }

        // Regenerar el archivo de índices con los cambios en la marca
        regenerarArchivoIndicesConMarcaEditada(nombreAntiguo, nuevaLinea, lineaLongitudAnterior);
    }

    private static void regenerarArchivoIndicesConMarcaEditada(String nombreAntiguo, Linea nuevaLinea, int lineaLongitudAnterior) throws IOException {
        List<String> indicesActualizados = new ArrayList<>();
        int desplazamiento = nuevaLinea.toString().length() - lineaLongitudAnterior;
        boolean actualizarSiguientes = false;

        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE))) {
            String linea;

            while ((linea = indexReader.readLine()) != null) {
                String[] partes = linea.split(",");
                String nombreIndice = partes[0].trim();
                int posicionInicio = Integer.parseInt(partes[1].trim());
                int longitud = Integer.parseInt(partes[2].trim());

                if (nombreIndice.equalsIgnoreCase(nombreAntiguo)) {
                    // Actualiza la marca editada con la nueva longitud y posición
                    indicesActualizados.add(nuevaLinea.getNombreLinea() + "," + posicionInicio + "," + nuevaLinea.toString().length());
                    actualizarSiguientes = true; // Activa la actualización para los siguientes índices
                } else {
                    // Si es un registro posterior y se requiere ajuste, aplica el desplazamiento
                    if (actualizarSiguientes) {
                        posicionInicio += desplazamiento;
                    }
                    indicesActualizados.add(nombreIndice + "," + posicionInicio + "," + longitud);
                }
            }
        }

        // Escribir los índices actualizados en el archivo de índice
        try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE))) {
            for (String indice : indicesActualizados) {
                indexWriter.write(indice + "\n");
            }
        }

        // Mostrar los índices ordenados después de la edición
        idMarca.mostrarIndicesOrdenados(INDEX_FILE,INDEX_SORTED_FILE);
    }


}
