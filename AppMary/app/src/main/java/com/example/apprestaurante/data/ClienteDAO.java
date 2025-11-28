package com.example.apprestaurante.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.apprestaurante.model.Cliente;

public class ClienteDAO {
    private AdminSQLiteOpenHelper dbHelper;

    public ClienteDAO(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    public boolean registrarCliente(Cliente cliente) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre_razon_social", cliente.getNombreRazonSocial());
        values.put("dni_ruc", cliente.getDniRuc());
        values.put("telefono", cliente.getTelefono());
        values.put("direccion", cliente.getDireccion());
        values.put("password", cliente.getPassword()); // Para el login futuro

        long res = db.insert("cliente", null, values);
        db.close();
        return res != -1;
    }


    // Método para llenar el Spinner de clientes en la venta
    public java.util.ArrayList<Cliente> listarClientes() {
        java.util.ArrayList<Cliente> lista = new java.util.ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Traemos ID y Nombre (y RUC para validar factura)
        android.database.Cursor cursor = db.rawQuery("SELECT * FROM cliente", null);

        if (cursor.moveToFirst()) {
            do {
                // Constructor: nombre, dni, tel, dir, pass
                // Ojo: Aquí usaremos un truco, guardaremos el ID en una variable auxiliar o usaremos el orden
                // Para hacerlo simple y rápido, crearemos el objeto completo
                // Estructura tabla: 0:id, 1:nombre, 2:dni, 3:tel, 4:dir, 5:pass
                Cliente c = new Cliente(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                // Necesitamos setear el ID manualmente, agreguemos un setter en el modelo Cliente si no tiene,
                // o asumiremos el orden de la lista.
                // MEJOR OPCIÓN: Vamos a devolver solo Strings para el Spinner y tener una lista paralela de IDs en la Activity.
                lista.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Método auxiliar para obtener ID de cliente por DNI (útil para la lógica)
    public int obtenerIdPorDni(String dni) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor c = db.rawQuery("SELECT id_cliente FROM cliente WHERE dni_ruc = ?", new String[]{dni});
        int id = 1; // Default: Público General
        if (c.moveToFirst()) {
            id = c.getInt(0);
        }
        c.close();
        db.close();
        return id;
    }


    // Método para validar Login de Cliente
    public com.example.apprestaurante.model.Cliente loginCliente(String dni, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(
                "SELECT * FROM cliente WHERE dni_ruc = ? AND password = ?",
                new String[]{dni, password}
        );

        com.example.apprestaurante.model.Cliente cliente = null;
        if (cursor.moveToFirst()) {
            // Recuperamos datos: 1:nombre, 2:dni, 3:tel, 4:dir, 5:pass
            cliente = new com.example.apprestaurante.model.Cliente(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        db.close();
        return cliente;
    }


}
