package org.example.models;

public class Producto {
    private int idProducto;
    private String nombre;
    private double precioCatalogo;
    private double pesoEsperado;
    private Integer idCategoria; // Integer para permitir valores nulos si aplica

    public Producto() {}

    public Producto(int idProducto, String nombre, double precioCatalogo, double pesoEsperado, Integer idCategoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioCatalogo = precioCatalogo;
        this.pesoEsperado = pesoEsperado;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecioCatalogo() { return precioCatalogo; }
    public void setPrecioCatalogo(double precioCatalogo) { this.precioCatalogo = precioCatalogo; }

    public double getPesoEsperado() { return pesoEsperado; }
    public void setPesoEsperado(double pesoEsperado) { this.pesoEsperado = pesoEsperado; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
}