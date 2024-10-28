package Objetos;

public class IndiceMarca {

    private String marca;
    private long indice;
    private int longitud;

    // Constructor, getters y setters
    public IndiceMarca(String marca, long indice, int longitud) {
        this.marca = marca;
        this.indice = indice;
        this.longitud = longitud;
    }

    public String getMarca() { return marca; }
    public long getIndice() { return indice; }
    public int getLongitud() { return longitud; }

}
