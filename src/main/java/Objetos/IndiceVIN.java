package Objetos;

public class IndiceVIN {
    String vin;
    int posicionInicial;
    int longitud;
    int siguiente;

    public IndiceVIN(String vin, int posicionInicial, int longitud, int siguiente) {
        this.vin = vin;
        this.posicionInicial = posicionInicial;
        this.longitud = longitud;
        this.siguiente = siguiente;
    }

    public String getVin() {return vin;}
    public int getPosicionInicial() {return posicionInicial;}
    public int getLongitud() {return longitud;}
    public int getSiguiente() {return siguiente;}


    @Override
    public String toString() {
        return vin + "," + posicionInicial + "," + longitud + "," + siguiente;
    }
}
