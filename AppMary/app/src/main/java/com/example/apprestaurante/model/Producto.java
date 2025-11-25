package com.example.apprestaurante.model;
public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int stock;
    private String urlImagen; // Por ahora no la usaremos, pero la dejamos lista
    private int idCategoria;

    public Producto() {
    }

    public Producto(String nombre, double precio, int stock, int idCategoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    // Campo auxiliar para la UI (No se guarda en la tabla Producto, solo sirve para tomar el pedido)
    private int cantidadSolicitada = 0;

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }


}