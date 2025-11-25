package com.example.apprestaurante.model;

public class DetallePedido {
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetallePedido(int idProducto, int cantidad, double precioUnitario) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    // Getters
    public int getIdProducto() { return idProducto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getSubtotal() { return subtotal; }
}
