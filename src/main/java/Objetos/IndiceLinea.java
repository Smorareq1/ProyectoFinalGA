package Objetos;

public class IndiceLinea {

    private String nombreLinea;
    private long indice;
    private int longitud;

    public IndiceLinea(String nombreLinea, long indice, int longitud) {
        this.nombreLinea = nombreLinea;
        this.indice = indice;
        this.longitud = longitud;
    }

    public String getNombreLinea() { return nombreLinea; }
    public long getIndice() { return indice; }
    public int getLongitud() { return longitud; }


    @Override
    public String toString() {
        return nombreLinea + "," + indice + "," + longitud;
    }
}
