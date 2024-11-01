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

    public Marca(String linea) {
        String[] partes = linea.split(",");
        this.nombre = partes[0];
        this.anioDeCreacion = partes[1];
        this.fundador = partes[2];
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

    @Override
        public String toString() {
            return nombre + "," + anioDeCreacion + "," + fundador;
        }
}
