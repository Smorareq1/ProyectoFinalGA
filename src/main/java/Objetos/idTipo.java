package Objetos;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;

public class idTipo {

    private static final String DATA_FILE = "src/main/resources/datos/Tipos_txt/datosTipos.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Tipos_txt/indiceTipos.txt";
    private static final String INDEX_SORTED_FILE = "src/main/resources/datos/Tipos_txt/indiceTiposOrdenados.txt";

    //////////////////////////////////////////////////////// AGREGAR //////////////////////////////////////////////////////////

    // Método para agregar un nuevo tipo sin alterar los índices existentes
    public static void agregarTipo(String nombre, Tipo nuevoTipo) throws IOException {
        // Añadir el nuevo tipo al diccionario
        GestorDeArchivos.diccionarioNombreTipos.put(nombre, nuevoTipo);

        // Escribir solo el nuevo tipo al final del archivo de datos
        escribirNuevoTipoDatos(nuevoTipo);

        // Actualizar el archivo de índices con el nuevo tipo al final
        agregarNuevoIndice(nombre, nuevoTipo);
        idMarca.mostrarIndicesOrdenados(INDEX_FILE, INDEX_SORTED_FILE);
    }

    // Escribir solo el nuevo tipo al final del archivo de datos
    public static void escribirNuevoTipoDatos(Tipo nuevoTipo) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "rw")) {
            datosFile.seek(datosFile.length());
            datosFile.writeBytes(nuevoTipo.toString() + "\n");
        }
    }

    // Añadir el nuevo índice al final del archivo de índices
    public static void agregarNuevoIndice(String nombre, Tipo nuevoTipo) throws IOException {
        try (RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r");
             BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE, true))) {

            long posicionInicial = datosFile.length() - nuevoTipo.toString().length() - 1; // Calcular la posición del nuevo tipo
            int longitud = nuevoTipo.toString().length(); // Obtener la longitud del tipo

            // Escribir el nuevo índice al final del archivo
            indexWriter.write(nombre + "," + posicionInicial + "," + longitud + "\n");
        }
    }

    //////////////////////////////////////////////////////// ELIMINAR  //////////////////////////////////////////////////////////

    // Método para eliminar un tipo por nombre y actualizar archivos
    public static void eliminarTipo(String nombreTipo) throws IOException {
        // Verificar si el tipo existe en el diccionario
        Tipo tipoAEliminar = GestorDeArchivos.diccionarioNombreTipos.remove(nombreTipo);
        if (tipoAEliminar == null) {
            System.out.println("Tipo no encontrado.");
            return;
        }

        // Regenerar el archivo de datos sin el tipo eliminado
        regenerarArchivoDatosSinTipo(nombreTipo);

        // Regenerar el archivo de índices sin el tipo eliminado
        regenerarArchivoIndicesSinTipo(nombreTipo);

        // Mostrar los índices ordenados después de la eliminación
        idMarca.mostrarIndicesOrdenados(INDEX_FILE, INDEX_SORTED_FILE);

        //Verificar si hay vehículos con el tipo eliminado
        List<String> vehiculosConTipoEliminado = new ArrayList<>();
        for (Vehiculo vehiculo : GestorDeArchivos.diccionarioNombreVehiculos.values()) {
            if (vehiculo.getTipo().getNombreTipo().equalsIgnoreCase(nombreTipo)) {
                vehiculosConTipoEliminado.add(vehiculo.getPlaca());
            }
        }

        //Eliminar los vehículos con el tipo eliminado
        for (String placa : vehiculosConTipoEliminado) {
            idVehiculos.eliminarVehiculo(placa);
        }
    }

    // Regenerar el archivo de datos sin el tipo eliminado
    private static void regenerarArchivoDatosSinTipo(String nombreTipo) throws IOException {
        List<Tipo> tiposRestantes = new ArrayList<>();

        try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
            String linea;
            while ((linea = datosReader.readLine()) != null) {
                Tipo tipo = new Tipo(linea);
                if (!tipo.getNombreTipo().equalsIgnoreCase(nombreTipo)) {
                    tiposRestantes.add(tipo);
                }
            }
        }

        // Escribir los tipos restantes en el archivo de datos
        try (BufferedWriter datosWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Tipo tipo : tiposRestantes) {
                datosWriter.write(tipo.toString() + "\n");
            }
        }
    }

    // Regenerar el archivo de índices sin el tipo eliminado
    private static void regenerarArchivoIndicesSinTipo(String nombreTipo) throws IOException {
        try (BufferedWriter indexWriter = new BufferedWriter(new FileWriter(INDEX_FILE))) {
            try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
                String linea;
                long posicionActual = 0;
                while ((linea = datosReader.readLine()) != null) {
                    Tipo tipo = new Tipo(linea);
                    if (!tipo.getNombreTipo().equalsIgnoreCase(nombreTipo)) {
                        int longitud = linea.length();
                        indexWriter.write(tipo.getNombreTipo() + "," + posicionActual + "," + longitud + "\n");
                        posicionActual += longitud + 1;
                    }
                }
            }
        }
    }

    //////////////////////////////////////////////////////// BUSQUEDA //////////////////////////////////////////////////////////
    // Método para buscar por nombre de tipo en el archivo de índices
    public static void buscarPorNombre(IndiceTipo indice, String nombreTipo) throws IOException {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_FILE));
             RandomAccessFile datosFile = new RandomAccessFile(DATA_FILE, "r")) {

            String linea;
            while ((linea = indexReader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length != 3) {
                    System.out.println("Formato Incorrecto.");
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
                if (nombreIndice.equalsIgnoreCase(nombreTipo)) {
                    // Lee el registro desde el archivo de datos usando posición y longitud del índice
                    datosFile.seek(posicionInicial);
                    byte[] registro = new byte[longitud];
                    datosFile.readFully(registro);

                    // Muestra el registro encontrado
                    generarAlerta("Registro encontrado: " + new String(registro));
                    return;
                }
            }
            System.out.println("Tipo no encontrado.");
        }
    }

    // Método para generar una alerta con un mensaje
    public static void generarAlerta(String mensaje) {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle("Alerta");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    //////////////////////////////////////////////////////// EDITAR //////////////////////////////////////////////////////////
    public static void editarMarcaEnDatos(String nombreAntiguo, Tipo nuevoTipo, Tipo tipoAnterior) throws IOException {
        // Lista para almacenar las marcas actualizadas
        List<Tipo> tiposActualizados = new ArrayList<>();
        int marcaAnteriorLongitud = tipoAnterior.toString().length();

        // Leer el archivo de datos y actualizar la marca correspondiente
        try (BufferedReader datosReader = new BufferedReader(new FileReader(DATA_FILE))) {
            String linea;
            while ((linea = datosReader.readLine()) != null) {
                Tipo tipo = new Tipo(linea);
                if (tipo.getNombreTipo().equalsIgnoreCase(nombreAntiguo)) {
                    tiposActualizados.add(nuevoTipo); // Reemplaza con la nueva marca
                } else {
                    tiposActualizados.add(tipo);
                }
            }
        }

        // Escribir las marcas actualizadas en el archivo de datos
        try (BufferedWriter datosWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Tipo tipo : tiposActualizados) {
                datosWriter.write(tipo.toString() + "\n");
            }
        }

        // Regenerar el archivo de índices con los cambios en la marca
        regenerarArchivoIndicesConMarcaEditada(nombreAntiguo, nuevoTipo, marcaAnteriorLongitud);
    }

    private static void regenerarArchivoIndicesConMarcaEditada(String nombreAntiguo, Tipo nuevoTipo, int marcaAnteriorLongitud) throws IOException {
        List<String> indicesActualizados = new ArrayList<>();
        int desplazamiento = nuevoTipo.toString().length() - marcaAnteriorLongitud;
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
                    indicesActualizados.add(nuevoTipo.getNombreTipo() + "," + posicionInicio + "," + nuevoTipo.toString().length());
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
