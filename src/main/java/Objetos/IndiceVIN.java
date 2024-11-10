package Objetos;

public class IndiceVIN {
    String VIN;
    long indice;
    int longitud;
    long siguiente;

    public IndiceVIN(String VIN, long indice, int longitud, long siguiente) {
        this.VIN = VIN;
        this.indice = indice;
        this.longitud = longitud;
        this.siguiente = siguiente;
    }

    public String getVIN() { return VIN; }
    public long getIndice() { return indice; }
    public int getLongitud() { return longitud; }
    public long getSiguiente() { return siguiente; }

    @Override
    public String toString() {
        return VIN + "," + indice + "," + longitud + "," + siguiente;
    }
}
