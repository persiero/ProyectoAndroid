package com.example.apprestaurante.model;

public class Pedido {
    private int id;
    private int idMesa;
    private int idUsuario;
    private int idCliente;
    private String tipoComprobante; // TICKET, BOLETA, FACTURA
    private double total;
    private String estado;

    // Constructor Completo (Para guardar nuevo pedido)
    public Pedido(int idMesa, int idUsuario, int idCliente, String tipoComprobante, double total) {
        this.idMesa = idMesa;
        this.idUsuario = idUsuario;
        this.idCliente = idCliente;
        this.tipoComprobante = tipoComprobante;
        this.total = total;
        this.estado = "PENDIENTE";
    }

    // --- GETTERS Y SETTERS (Esto es lo que te faltaba) ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; } // <--- ¡AQUÍ ESTÁ EL QUE FALTABA!

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}