package com.example.apprestaurante.model;

public class Mesa {
    private int id;
    private String numeroMesa; // Ej: "Mesa 1"
    private String estado;     // "LIBRE" o "OCUPADA"

    public Mesa(int id, String numeroMesa, String estado) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getNumeroMesa() { return numeroMesa; }
    public String getEstado() { return estado; }
}
