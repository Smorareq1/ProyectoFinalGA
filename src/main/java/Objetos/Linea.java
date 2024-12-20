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

    public Linea(String linea) {
        String[] partes = linea.split(",");
        this.nombreMarcaDeLinea = partes[0];
        this.nombreLinea = partes[1];
        this.anioLinea = partes[2];
    }

    // Getters
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

    public Marca getMarca() {
        return marca;
    }

    //Setter
    public void setNombreLinea(String nombreLinea)
    {
        this.nombreLinea = nombreLinea;
    }

    public void setAnioLinea(String anioLinea)
    {
        this.anioLinea = anioLinea;
    }

    public void setNombreMarcaDeLinea(String nombreMarcaDeLinea) {
        this.nombreMarcaDeLinea = nombreMarcaDeLinea;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return nombreMarcaDeLinea + "," + nombreLinea + "," + anioLinea;
    }
}



