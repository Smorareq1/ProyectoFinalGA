package Objetos;


public class Vehiculo {

    // Se asocia por marca , linea y Tipo de forma interna
    public Marca marca;
    public Linea linea;
    public Tipo tipo;

    // Forma publica
    public String marcaNombreVehiculo;
    public String lineaNombreVehiculo;
    public String tipoNombreVehiculo;

    //Se pueden repetir
    public String modelo;
    public String color;
    public String numeroDeAsientos;


    //No se pueden repetir
    public String placa;
    public String chasis;
    public String motor;
    public String vin;


    public Vehiculo(Marca marca, Linea linea, Tipo tipo, String modelo, String color, String placa, String chasis, String motor, String vin, String numeroDeAsientos) {
        this.marca = marca;
        this.linea = linea;
        this.tipo = tipo;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.chasis = chasis;
        this.motor = motor;
        this.vin = vin;
        this.numeroDeAsientos = numeroDeAsientos;

        this.marcaNombreVehiculo = marca.getNombre();
        this.lineaNombreVehiculo = linea.getNombreLinea();
        this.tipoNombreVehiculo = tipo.getNombreTipo();
    }

    public Vehiculo(String linea){
        String[] partes = linea.split(",");
        this.marcaNombreVehiculo = partes[0];
        this.tipoNombreVehiculo = partes[1];
        this.lineaNombreVehiculo = partes[2];
        this.modelo = partes[3];
        this.color = partes[4];
        this.numeroDeAsientos = partes[5];
        this.placa = partes[6];
        this.chasis = partes[7];
        this.motor = partes[8];
        this.vin = partes[9];
    }

    // Getters y Setters

    public Marca getMarca() {return marca;}
    public void setMarca(Marca marca) {this.marca = marca;}
    public Linea getLinea() {return linea;}
    public void setLinea(Linea linea) {this.linea = linea;}
    public Tipo getTipo() {return tipo;}
    public void setTipo(Tipo tipo) {this.tipo = tipo;}
    public String getMarcaNombreVehiculo() {return marcaNombreVehiculo;}
    public void setMarcaNombreVehiculo(String marcaNombreVehiculo) {this.marcaNombreVehiculo = marcaNombreVehiculo;}
    public String getLineaNombreVehiculo() {return lineaNombreVehiculo;}
    public void setLineaNombreVehiculo(String lineaNombreVehiculo) {this.lineaNombreVehiculo = lineaNombreVehiculo;}
    public String getTipoNombreVehiculo() {return tipoNombreVehiculo;}
    public void setTipoNombreVehiculo(String tipoNombreVehiculo) {this.tipoNombreVehiculo = tipoNombreVehiculo;}
    public String getModelo() {return modelo;}
    public void setModelo(String modelo) {this.modelo = modelo;}
    public String getColor() {return color;}
    public void setColor(String color) {this.color = color;}
    public String getNumeroDeAsientos() {return numeroDeAsientos;}
    public void setNumeroDeAsientos(String numeroDeAsientos) {this.numeroDeAsientos = numeroDeAsientos;}
    public String getPlaca() {return placa;}
    public void setPlaca(String placa) {this.placa = placa;}
    public String getChasis() {return chasis;}
    public void setChasis(String chasis) {this.chasis = chasis;}
    public String getMotor() {return motor;}
    public void setMotor(String motor) {this.motor = motor;}
    public String getVin() {return vin;}
    public void setVin(String vin) {this.vin = vin;}

    @Override
    public String toString() {
        return marcaNombreVehiculo + "," + tipoNombreVehiculo + "," + lineaNombreVehiculo + "," + modelo + "," + color+ "," + numeroDeAsientos + "," + placa + "," + chasis + "," + motor + "," + vin;
    }
}
