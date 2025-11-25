package com.example.apprestaurante.model;

public class Pedido {
    private int id;
    private int idMesa;
    private int idUsuario;
    private double total;
    private String estado;
    private String fecha;

    public Pedido(int idMesa, int idUsuario, double total) {
        this.idMesa = idMesa;
        this.idUsuario = idUsuario;
        this.total = total;
        this.estado = "PENDIENTE";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdMesa() { return idMesa; }
    public int getIdUsuario() { return idUsuario; }
    public double getTotal() { return total; }
}
