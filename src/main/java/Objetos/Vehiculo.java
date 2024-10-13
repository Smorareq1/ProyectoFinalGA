package Objetos;

public class Vehiculo {

    // Se asocia por marca
    public Marca marca;
    public Linea linea;
    public Tipo tipo;
    public String modelo;
    public String color;


    //No se pueden repetir
    public String placa;
    public String chasis;
    public String motor;
    public String vin;

    public Vehiculo(Marca marca, Linea linea, Tipo tipo, String modelo, String color, String placa, String chasis, String motor, String vin) {
        this.marca = marca;
        this.linea = linea;
        this.tipo = tipo;
        this.modelo = modelo;
        this.color = color;
        this.placa = placa;
        this.chasis = chasis;
        this.motor = motor;
        this.vin = vin;
    }
}
