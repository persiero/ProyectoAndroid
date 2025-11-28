package com.example.apprestaurante.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.apprestaurante.model.DetallePedido;
import com.example.apprestaurante.model.Pedido;
import java.util.ArrayList;

public class PedidoDAO {
    private AdminSQLiteOpenHelper dbHelper;

    public PedidoDAO(Context context) {
        dbHelper = new AdminSQLiteOpenHelper(context);
    }

    public boolean registrarPedidoCompleto(Pedido pedido, ArrayList<DetallePedido> detalles) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // INICIO DE TRANSACCIÓN (Para asegurar que se guarde todo o nada)
        db.beginTransaction();
        try {
            // 1. Insertar Cabecera (Tabla PEDIDO)
            ContentValues valoresPedido = new ContentValues();
            valoresPedido.put("id_mesa", pedido.getIdMesa());
            valoresPedido.put("id_usuario", pedido.getIdUsuario());
            valoresPedido.put("id_cliente", pedido.getIdCliente()); // NUEVO
            valoresPedido.put("tipo_comprobante", pedido.getTipoComprobante()); // NUEVO
            valoresPedido.put("total_final", pedido.getTotal());
            valoresPedido.put("estado", "PENDIENTE");

            long idPedidoGenerado = db.insert("pedido", null, valoresPedido);

            if (idPedidoGenerado == -1) {
                throw new Exception("Error al crear pedido");
            }

            // 2. Insertar Detalles (Tabla DETALLE_PEDIDO) y Descontar Stock
            for (DetallePedido det : detalles) {
                ContentValues valoresDetalle = new ContentValues();
                valoresDetalle.put("id_pedido", idPedidoGenerado);
                valoresDetalle.put("id_producto", det.getIdProducto());
                valoresDetalle.put("cantidad", det.getCantidad());
                valoresDetalle.put("precio_unitario", det.getPrecioUnitario());
                valoresDetalle.put("subtotal", det.getSubtotal());

                db.insert("detalle_pedido", null, valoresDetalle);

                // 3. Actualizar Stock
                db.execSQL("UPDATE producto SET stock = stock - " + det.getCantidad() +
                        " WHERE id_producto = " + det.getIdProducto());
            }

            // 4. Cambiar estado de Mesa a OCUPADA
            db.execSQL("UPDATE mesa SET estado = 'OCUPADA' WHERE id_mesa = " + pedido.getIdMesa());

            db.setTransactionSuccessful(); // ¡Éxito!
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction(); // Cierra transacción
            db.close();
        }
    }



    // Método para reporte por rango de fechas
    // Formato de fechas esperado: "YYYY-MM-DD" (Ej: "2023-11-27")
    public ArrayList<Pedido> buscarPedidosPorFecha(String fechaInicio, String fechaFin) {
        ArrayList<Pedido> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // SQL: Usamos la función date() de SQLite para comparar solo la parte de la fecha (ignorando la hora)
        String sql = "SELECT * FROM pedido WHERE date(fecha_creacion) BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(sql, new String[]{fechaInicio, fechaFin});

        if (cursor.moveToFirst()) {
            do {
                // Recuperamos los datos (0:id, 1:fecha, 2:estado, 3:total, 4:tipo, etc...)
                // Nota: Ajusta los índices según tu tabla.
                // En el último script: 0:id, 1:fecha, 2:estado, 3:total, 4:tipo, 5:mesa, 6:user, 7:cliente, 8:metodo

                // Creamos un pedido temporal solo con los datos necesarios para el reporte
                Pedido p = new Pedido(0, 0, 0, cursor.getString(4), cursor.getDouble(3));
                p.setId(cursor.getInt(0)); // ID del pedido
                // Usamos el campo "estado" temporalmente para guardar la fecha en el objeto y mostrarla
                // (O podrías agregar un campo fecha al modelo Pedido si prefieres, pero esto es un truco rápido)

                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }


}
