package org.example.models;

public class CarritoInteligente {
    private int idCarrito;
    private int estadoBateria;
    private String estadoOperativo;
    private Integer idCliente; // Puede ser nulo si el carrito no tiene cliente asignado aún

    public CarritoInteligente() {}

    public CarritoInteligente(int idCarrito, int estadoBateria, String estadoOperativo, Integer idCliente) {
        this.idCarrito = idCarrito;
        this.estadoBateria = estadoBateria;
        this.estadoOperativo = estadoOperativo;
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public int getIdCarrito() { return idCarrito; }
    public void setIdCarrito(int idCarrito) { this.idCarrito = idCarrito; }

    public int getEstadoBateria() { return estadoBateria; }
    public void setEstadoBateria(int estadoBateria) { this.estadoBateria = estadoBateria; }

    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
}