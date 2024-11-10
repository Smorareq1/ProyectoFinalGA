package Objetos;

import java.util.ArrayList;
import java.util.List;

public class idVines {

    private static final String DATA_FILE = "src/main/resources/datos/VIN_txt/datosVIN.txt";
    private static final String INDEX_FILE = "src/main/resources/datos/VIN_txt/indiceVIN.txt";
    private static final String INDEX_SORTED_FILE ="src/main/resources/datos/VIN_txt/indiceVINOrdenado.txt";

    //Listas para la fase 3
    public static List<idVines> listaIDVinesSecuencial = new ArrayList<>();
    public static List<idVines> listaIDVinesConexionAlfabetica = new ArrayList<>();
    public static List<datosVines> listaDatosVines = new ArrayList<>();

}
