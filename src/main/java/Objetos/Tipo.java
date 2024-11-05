package Objetos;

public class Tipo {

    public String nombreTipo;
    public String anioTipo;

    public Tipo(String nombreTipo, String anioTipo) {
        this.nombreTipo = nombreTipo;
        this.anioTipo = anioTipo;
    }

    public Tipo(String linea) {
        String[] partes = linea.split(",");
        this.nombreTipo = partes[0];
        this.anioTipo = partes[1];
    }

    // Getters y Setters
    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public String getAnioTipo() {
        return anioTipo;
    }

    public void setAnioTipo(String anioTipo) {
        this.anioTipo = anioTipo;
    }

    @Override
    public String toString() {
        return nombreTipo + "," + anioTipo;
    }
}
