package Objetos;

public class Linea {

    private String nombreMarca;  // Cambiado a solo nombre de marca
    private String nombreLinea;
    private String anioLinea;

    // Constructor
    public Linea(Marca marca, String nombreLinea, String anioLinea) {
        this.nombreMarca = marca.getNombre();  // Guardamos solo el nombre de la marca
        this.nombreLinea = nombreLinea;
        this.anioLinea = anioLinea;
    }

    // Getter para el nombre de la marca
    public String getNombreMarca() {
        return nombreMarca;
    }

    public String getNombreLinea()
    {
        return nombreLinea;
    }
    public void setNombreLinea(String nombreLinea)
    {
        this.nombreLinea = nombreLinea;
    }

    public String getAnioLinea()
    {
        return anioLinea;
    }

    public void setAnioLinea(String anioLinea)
    {
        this.anioLinea = anioLinea;
    }
}



