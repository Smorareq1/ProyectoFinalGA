package Objetos;

public class Marca {
    public String nombre;
    public String anioDeCreacion;
    public String fundador;

    public Marca(String nombre, String anioDeCreacion, String fundador) {
        this.nombre = nombre;
        this.anioDeCreacion = anioDeCreacion;
        this.fundador = fundador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAnioDeCreacion() {
        return anioDeCreacion;
    }

    public String getFundador() {
        return fundador;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAnioDeCreacion(String anioDeCreacion) {
        this.anioDeCreacion = anioDeCreacion;
    }

    public void setFundador(String fundador) {
        this.fundador = fundador;
    }
}
