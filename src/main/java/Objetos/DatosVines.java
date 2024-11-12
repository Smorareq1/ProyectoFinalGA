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

    public DatosVines(String linea){
        String[] datos = linea.split(",");
        this.nombreMarca = datos[0];
        this.nombreLinea = datos[1];
        this.Placa = datos[2];
        this.Vin = datos[3];
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
