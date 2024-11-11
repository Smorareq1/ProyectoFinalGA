package Objetos;

public class DatosVines {

    String nombreMarca;
    String nombreLinea;
    String Placa;
    String Vin;

    public DatosVines(String nombreMarca, String nombreLinea, String Placa, String Vin) {
        this.nombreMarca = nombreMarca;
        this.nombreLinea = nombreLinea;
        this.Placa = Placa;
        this.Vin = Vin;
    }

    public String getNombreMarca() {return nombreMarca;}
    public String getNombreLinea() {return nombreLinea;}
    public String getPlaca() {return Placa;}
    public String getVin() {return Vin;}

    @Override
    public String toString() {
        return nombreMarca + "," + nombreLinea + "," + Placa + "," + Vin;

    }


}
