package com.example.apprestaurante.data;

import android.content.ContentValues;
import android.content.Context;
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
}
