package Objetos;

import java.io.IOException;

public class idLinea {

    private static final String DATA_FILE = "src/main/resources/datos/Lineas_txt/datosLineas.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/Lineas_txt/indiceLineas.txt";

    public static void agregarLinea(String nombre, Linea nuevaLinea) throws IOException {
        GestorDeArchivos.diccionarioNombreLineas.put(nombre, nuevaLinea);

        //escribirNuevaLineaDatos(nuevaLinea);

        //agregarNuevoIndice(nombre, nuevaLinea);
        //mostrarIndicesOrdenados();
    }

}
