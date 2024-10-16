package Objetos;

public class Linea {

    private String nombreMarcaDeLinea;
    private String nombreLinea;
    private String anioLinea;
    private Marca marca;

    // Constructor
    public Linea(Marca marca, String nombreLinea, String anioLinea) {
        this.marca = marca;

        // Guardamos solo el nombre de la marca
        this.nombreMarcaDeLinea = marca.getNombre();
        this.nombreLinea = nombreLinea;
        this.anioLinea = anioLinea;
    }

    // Getter para el nombre de la marca

    public String getNombreMarcaDeLinea() {
        return this.nombreMarcaDeLinea;
    }


    public String getNombreLinea()
    {
        return nombreLinea;
    }

    public String getAnioLinea()
    {
        return anioLinea;
    }

    public void setNombreLinea(String nombreLinea)
    {
        this.nombreLinea = nombreLinea;
    }

    public void setAnioLinea(String anioLinea)
    {
        this.anioLinea = anioLinea;
    }


    @Override
    public String toString() {
        return "Linea{" +
                "nombreMarcaDeLinea='" + nombreMarcaDeLinea + '\'' +
                ", nombreLinea='" + nombreLinea + '\'' +
                ", anioLinea='" + anioLinea + '\'' +
                '}';
    }
}



