package com.example.apprestaurante.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "marys_db.db";
    private static final int DATABASE_VERSION = 2; // Subimos la versión por si acaso

    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // --- TABLAS EXISTENTES (Mantenemos la lógica) ---

        db.execSQL("CREATE TABLE rol (id_rol INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");

        db.execSQL("CREATE TABLE usuario (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre_completo TEXT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "id_rol INTEGER, " +
                "FOREIGN KEY(id_rol) REFERENCES rol(id_rol))");

        db.execSQL("CREATE TABLE mesa (id_mesa INTEGER PRIMARY KEY AUTOINCREMENT, numero_mesa TEXT NOT NULL, estado TEXT DEFAULT 'LIBRE')");
        db.execSQL("CREATE TABLE categoria (id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");
        db.execSQL("CREATE TABLE producto (" +
                "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "precio REAL NOT NULL, " +
                "stock INTEGER DEFAULT 0, " +
                "url_imagen TEXT, " +
                "id_categoria INTEGER, " +
                "FOREIGN KEY(id_categoria) REFERENCES categoria(id_categoria))");
        db.execSQL("CREATE TABLE metodo_pago (id_metodo INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");

        // --- NUEVAS TABLAS Y MODIFICACIONES ---

        // 1. NUEVA TABLA: CLIENTE (Para el Formulario de Clientes y Login Cliente)
        db.execSQL("CREATE TABLE cliente (" +
                "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre_razon_social TEXT NOT NULL, " +
                "dni_ruc TEXT UNIQUE NOT NULL, " +
                "telefono TEXT, " +
                "direccion TEXT, " +
                "password TEXT)"); // Agregamos password por si quieres simular Login de Cliente

        // 2. TABLA PEDIDO ACTUALIZADA (Soporte para Boleta/Factura y Cliente)
        db.execSQL("CREATE TABLE pedido (" +
                "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha_creacion TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "estado TEXT DEFAULT 'PENDIENTE', " +
                "total_final REAL DEFAULT 0, " +
                "tipo_comprobante TEXT DEFAULT 'TICKET', " + // Valores: TICKET, BOLETA, FACTURA
                "id_mesa INTEGER, " +
                "id_usuario INTEGER, " + // El mesero que atendió
                "id_cliente INTEGER, " + // El cliente que compró (NUEVO)
                "id_metodo INTEGER, " +
                "FOREIGN KEY(id_mesa) REFERENCES mesa(id_mesa), " +
                "FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario), " +
                "FOREIGN KEY(id_cliente) REFERENCES cliente(id_cliente), " +
                "FOREIGN KEY(id_metodo) REFERENCES metodo_pago(id_metodo))");

        db.execSQL("CREATE TABLE detalle_pedido (" +
                "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_pedido INTEGER, " +
                "id_producto INTEGER, " +
                "cantidad INTEGER NOT NULL, " +
                "precio_unitario REAL NOT NULL, " +
                "subtotal REAL NOT NULL, " +
                "FOREIGN KEY(id_pedido) REFERENCES pedido(id_pedido), " +
                "FOREIGN KEY(id_producto) REFERENCES producto(id_producto))");

        // --- DATOS SEMILLA ---
        insertarDatosIniciales(db);
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        // Roles
        db.execSQL("INSERT INTO rol (nombre) VALUES ('ADMINISTRADOR')");
        db.execSQL("INSERT INTO rol (nombre) VALUES ('MESERO')");
        db.execSQL("INSERT INTO rol (nombre) VALUES ('CLIENTE')"); // Nuevo Rol

        // Usuarios
        db.execSQL("INSERT INTO usuario (nombre_completo, username, password, id_rol) VALUES ('Admin Principal', 'admin', '1234', 1)");

        // Cliente Genérico (Público General) para ventas rápidas
        db.execSQL("INSERT INTO cliente (nombre_razon_social, dni_ruc, direccion, password) VALUES ('Público General', '00000000', '-', '1234')");

        // Mesas, Categorías, Pagos (Igual que antes)
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 1'), ('Mesa 2'), ('Mesa 3'), ('Mesa 4'), ('Mesa 5'), ('Mesa 6')");
        db.execSQL("INSERT INTO categoria (nombre) VALUES ('Caldos'), ('Segundos'), ('Bebidas')");
        db.execSQL("INSERT INTO metodo_pago (nombre) VALUES ('EFECTIVO'), ('YAPE'), ('TARJETA')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borrar todo si cambia versión
        db.execSQL("DROP TABLE IF EXISTS detalle_pedido");
        db.execSQL("DROP TABLE IF EXISTS pedido");
        db.execSQL("DROP TABLE IF EXISTS cliente"); // Nueva
        db.execSQL("DROP TABLE IF EXISTS producto");
        db.execSQL("DROP TABLE IF EXISTS categoria");
        db.execSQL("DROP TABLE IF EXISTS mesa");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS rol");
        db.execSQL("DROP TABLE IF EXISTS metodo_pago");
        onCreate(db);
    }
}