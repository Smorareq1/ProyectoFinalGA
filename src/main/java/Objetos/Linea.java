package Objetos;

public class Linea {
    public String nombreMarca;
    public String nombreLinea;
    public String anioLinea;
    public Marca marca;


    public Linea(Marca marca, String nombreLinea, String anioLinea) {
        this.nombreMarca = marca.getNombre();
        this.nombreLinea = nombreLinea;
        this.anioLinea = anioLinea;
        this.marca = marca;
    }

}
