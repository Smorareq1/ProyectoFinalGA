package Objetos;

public class IndiceTipo {

    private String nombreTipo;
    private long indice;
    private int longitud;

    public IndiceTipo(String nombreTipo, long indice, int longitud) {
        this.nombreTipo = nombreTipo;
        this.indice = indice;
        this.longitud = longitud;

    }

    public String getNombreTipo() { return nombreTipo; }
    public long getIndice() { return indice; }
    public int getLongitud() { return longitud; }

    @Override
    public String toString() {
        return nombreTipo + "," + indice + "," + longitud;
    }
}
