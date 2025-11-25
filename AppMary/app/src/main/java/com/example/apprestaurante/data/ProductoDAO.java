package com.example.apprestaurante.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.apprestaurante.model.Producto;

public class ProductoDAO {
    private AdminSQLiteOpenHelper dbHelper;

    public ProductoDAO(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    public boolean registrarProducto(Producto producto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", producto.getNombre());
        values.put("precio", producto.getPrecio());
        values.put("stock", producto.getStock());
        values.put("id_categoria", producto.getIdCategoria());
        // url_imagen la dejamos null por ahora

        long resultado = db.insert("producto", null, values);
        db.close();

        // Si resultado es -1 hubo error, si es > 0 se guardó bien
        return resultado != -1;
    }


    // Método para listar productos en el RecyclerView
    public java.util.ArrayList<Producto> obtenerTodos() {
        java.util.ArrayList<Producto> lista = new java.util.ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM producto", null);

        if (cursor.moveToFirst()) {
            do {
                Producto p = new Producto();
                p.setId(cursor.getInt(0));
                p.setNombre(cursor.getString(1));
                p.setPrecio(cursor.getDouble(2));
                p.setStock(cursor.getInt(3));
                p.setIdCategoria(cursor.getInt(5));
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }


}