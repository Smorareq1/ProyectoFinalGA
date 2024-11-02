package Objetos;

public class IndiceVehiculo {
    private String placa;
    private long indice;
    private int longitud;

    public IndiceVehiculo(String placa, long indice, int longitud) {
        this.placa = placa;
        this.indice = indice;
        this.longitud = longitud;
    }

    public String getPlaca() { return placa; }
    public long getIndice() { return indice; }
    public int getLongitud() { return longitud; }

    @Override
    public String toString() {
        return placa + "," + indice + "," + longitud;
    }
}
