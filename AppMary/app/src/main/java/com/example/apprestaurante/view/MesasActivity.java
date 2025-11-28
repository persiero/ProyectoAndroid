package com.example.apprestaurante.view;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprestaurante.R;
import com.example.apprestaurante.data.AdminSQLiteOpenHelper;
import com.example.apprestaurante.model.Mesa;

import java.util.ArrayList;

public class MesasActivity extends AppCompatActivity implements MesaAdapter.OnMesaClickListener {

    RecyclerView rvMesas;
    ArrayList<Mesa> listaMesas;
    AdminSQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        rvMesas = findViewById(R.id.rvMesas);
        // Usamos GridLayoutManager para que se vea como cuadricula (2 columnas)
        rvMesas.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new AdminSQLiteOpenHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMesas(); // Recargar siempre que entramos a la pantalla
    }

    private void cargarMesas() {
        listaMesas = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id_mesa, numero_mesa, estado FROM mesa", null);
        if (cursor.moveToFirst()) {
            do {
                listaMesas.add(new Mesa(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        MesaAdapter adapter = new MesaAdapter(listaMesas, this);
        rvMesas.setAdapter(adapter);
    }

    @Override
    public void onMesaClick(Mesa mesa) {
        if (mesa.getEstado().equals("LIBRE")) {
            Toast.makeText(this, "Esta mesa está libre", Toast.LENGTH_SHORT).show();
        } else {
            // Si está OCUPADA, mostramos diálogo para COBRAR
            mostrarDialogoCobro(mesa);
        }
    }

    private void mostrarDialogoCobro(Mesa mesa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Cuenta - " + mesa.getNumeroMesa());
        builder.setMessage("¿Desea finalizar la atención, cobrar y liberar esta mesa?");

        builder.setPositiveButton("SI, COBRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                liberarMesa(mesa.getId());
            }
        });

        builder.setNegativeButton("CANCELAR", null);
        builder.show();
    }

    private void liberarMesa(int idMesa) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 1. Actualizar estado de la MESA a LIBRE
        db.execSQL("UPDATE mesa SET estado = 'LIBRE' WHERE id_mesa = " + idMesa);

        // 2. Actualizar el PEDIDO a PAGADO (Buscamos el pedido pendiente de esa mesa)
        db.execSQL("UPDATE pedido SET estado = 'PAGADO', id_metodo = 1 WHERE id_mesa = " + idMesa + " AND estado = 'PENDIENTE'");

        db.close();

        Toast.makeText(this, "¡Cobro registrado! Mesa liberada.", Toast.LENGTH_LONG).show();
        cargarMesas(); // Refrescar pantalla para que se ponga verde
    }
}