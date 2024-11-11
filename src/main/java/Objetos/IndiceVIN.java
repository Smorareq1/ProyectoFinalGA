package Objetos;

public class IndiceVIN {
    private String vin;
    private int posicionInicial;
    private int longitud;
    private int siguiente; // Posición en bytes del siguiente VIN en el archivo de índice

    public IndiceVIN(String vin, int posicionInicial, int longitud, int siguiente) {
        this.vin = vin;
        this.posicionInicial = posicionInicial;
        this.longitud = longitud;
        this.siguiente = siguiente;
    }

    public String getVin() {
        return vin;
    }

    public int getPosicionInicial() {
        return posicionInicial;
    }

    public int getLongitud() {
        return longitud;
    }

    public int getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(int siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return vin + "," + posicionInicial + "," + longitud + "," + siguiente;
    }
}
