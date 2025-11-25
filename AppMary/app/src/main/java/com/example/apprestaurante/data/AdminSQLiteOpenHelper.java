package com.example.apprestaurante.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Nombre y Versión de la Base de Datos
    private static final String DATABASE_NAME = "marys_db.db";
    private static final int DATABASE_VERSION = 1;

    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. CREAR TABLAS

        // Tabla ROL
        db.execSQL("CREATE TABLE rol (id_rol INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");

        // Tabla USUARIO
        db.execSQL("CREATE TABLE usuario (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre_completo TEXT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "id_rol INTEGER, " +
                "FOREIGN KEY(id_rol) REFERENCES rol(id_rol))");

        // Tabla MESA
        db.execSQL("CREATE TABLE mesa (id_mesa INTEGER PRIMARY KEY AUTOINCREMENT, numero_mesa TEXT NOT NULL, estado TEXT DEFAULT 'LIBRE')");

        // Tabla CATEGORIA
        db.execSQL("CREATE TABLE categoria (id_categoria INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");

        // Tabla PRODUCTO
        db.execSQL("CREATE TABLE producto (" +
                "id_producto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "precio REAL NOT NULL, " +
                "stock INTEGER DEFAULT 0, " +
                "url_imagen TEXT, " +
                "id_categoria INTEGER, " +
                "FOREIGN KEY(id_categoria) REFERENCES categoria(id_categoria))");

        // Tabla METODO PAGO
        db.execSQL("CREATE TABLE metodo_pago (id_metodo INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL)");

        // Tabla PEDIDO
        db.execSQL("CREATE TABLE pedido (" +
                "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha_creacion TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "estado TEXT DEFAULT 'PENDIENTE', " +
                "total_final REAL DEFAULT 0, " +
                "id_mesa INTEGER, " +
                "id_usuario INTEGER, " +
                "id_metodo INTEGER, " +
                "FOREIGN KEY(id_mesa) REFERENCES mesa(id_mesa), " +
                "FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario), " +
                "FOREIGN KEY(id_metodo) REFERENCES metodo_pago(id_metodo))");

        // Tabla DETALLE PEDIDO
        db.execSQL("CREATE TABLE detalle_pedido (" +
                "id_detalle INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_pedido INTEGER, " +
                "id_producto INTEGER, " +
                "cantidad INTEGER NOT NULL, " +
                "precio_unitario REAL NOT NULL, " +
                "subtotal REAL NOT NULL, " +
                "FOREIGN KEY(id_pedido) REFERENCES pedido(id_pedido), " +
                "FOREIGN KEY(id_producto) REFERENCES producto(id_producto))");

        // 2. INSERTAR DATOS INICIALES (SEMILLA)
        insertarDatosIniciales(db);
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        // Roles
        db.execSQL("INSERT INTO rol (nombre) VALUES ('ADMINISTRADOR')");
        db.execSQL("INSERT INTO rol (nombre) VALUES ('MESERO')");

        // Usuario Admin (User: admin, Pass: 1234)
        db.execSQL("INSERT INTO usuario (nombre_completo, username, password, id_rol) VALUES ('Administrador Principal', 'admin', '1234', 1)");

        // Mesas (6 mesas)
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 1')");
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 2')");
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 3')");
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 4')");
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 5')");
        db.execSQL("INSERT INTO mesa (numero_mesa) VALUES ('Mesa 6')");

        // Categorías
        db.execSQL("INSERT INTO categoria (nombre) VALUES ('Caldos')");
        db.execSQL("INSERT INTO categoria (nombre) VALUES ('Segundos')");
        db.execSQL("INSERT INTO categoria (nombre) VALUES ('Bebidas')");

        // Métodos de Pago
        db.execSQL("INSERT INTO metodo_pago (nombre) VALUES ('EFECTIVO')");
        db.execSQL("INSERT INTO metodo_pago (nombre) VALUES ('YAPE')");
        db.execSQL("INSERT INTO metodo_pago (nombre) VALUES ('TARJETA')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En desarrollo, si cambias la versión, borramos todo y creamos de nuevo
        db.execSQL("DROP TABLE IF EXISTS detalle_pedido");
        db.execSQL("DROP TABLE IF EXISTS pedido");
        db.execSQL("DROP TABLE IF EXISTS producto");
        db.execSQL("DROP TABLE IF EXISTS categoria");
        db.execSQL("DROP TABLE IF EXISTS mesa");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS rol");
        db.execSQL("DROP TABLE IF EXISTS metodo_pago");
        onCreate(db);
    }
}
