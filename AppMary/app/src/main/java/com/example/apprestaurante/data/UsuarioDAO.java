package com.example.apprestaurante.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apprestaurante.model.Usuario;

public class UsuarioDAO {
    private AdminSQLiteOpenHelper dbHelper;

    public UsuarioDAO(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    // Método para validar Login
    public Usuario login(String user, String pass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Usuario usuario = null;

        // Consulta SQL segura
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuario WHERE username = ? AND password = ?",
                new String[]{user, pass}
        );

        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(0)); // id_usuario
            usuario.setNombreCompleto(cursor.getString(1));
            usuario.setUsername(cursor.getString(2));
            usuario.setPassword(cursor.getString(3));
            usuario.setIdRol(cursor.getInt(4));
        }
        cursor.close();
        db.close();
        return usuario; // Retorna el objeto si existe, o null si falló
    }
}
