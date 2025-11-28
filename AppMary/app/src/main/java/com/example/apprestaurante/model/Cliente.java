package com.example.apprestaurante.model;

public class Cliente {
    private int id;
    private String nombreRazonSocial;
    private String dniRuc;
    private String telefono;
    private String direccion;
    private String password;

    public Cliente(String nombreRazonSocial, String dniRuc, String telefono, String direccion, String password) {
        this.nombreRazonSocial = nombreRazonSocial;
        this.dniRuc = dniRuc;
        this.telefono = telefono;
        this.direccion = direccion;
        this.password = password;
    }

    // Getters
    public String getNombreRazonSocial() { return nombreRazonSocial; }
    public String getDniRuc() { return dniRuc; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getPassword() { return password; }
}
