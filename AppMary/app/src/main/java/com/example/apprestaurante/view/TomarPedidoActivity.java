package com.example.apprestaurante.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apprestaurante.R;
import com.example.apprestaurante.data.AdminSQLiteOpenHelper;
import com.example.apprestaurante.data.PedidoDAO;
import com.example.apprestaurante.data.ProductoDAO;
import com.example.apprestaurante.model.DetallePedido;
import com.example.apprestaurante.model.Pedido;
import com.example.apprestaurante.model.Producto;

import java.util.ArrayList;

public class TomarPedidoActivity extends AppCompatActivity implements ProductoAdapter.OnTotalChangeListener {

    Spinner spinnerMesas;
    RecyclerView rvProductos;
    TextView tvTotal;
    Button btnConfirmar;

    ArrayList<Producto> listaProductos;
    ProductoAdapter adapter;
    ArrayList<String> listaMesas;
    ArrayList<Integer> listaIdMesas; // Para saber el ID real de la mesa seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_pedido);

        // 1. Vincular
        spinnerMesas = findViewById(R.id.spinnerMesas);
        rvProductos = findViewById(R.id.rvProductos);
        tvTotal = findViewById(R.id.tvTotalPedido);
        btnConfirmar = findViewById(R.id.btnConfirmarPedido);

        // 2. Cargar Mesas (Spinner)
        cargarMesas();

        // 3. Cargar Productos (RecyclerView)
        ProductoDAO productoDAO = new ProductoDAO(this);
        listaProductos = productoDAO.obtenerTodos();

        adapter = new ProductoAdapter(listaProductos, this);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(adapter);

        // 4. Botón Confirmar
        btnConfirmar.setOnClickListener(v -> guardarPedido());
    }

    private void cargarMesas() {
        listaMesas = new ArrayList<>();
        listaIdMesas = new ArrayList<>();

        // Consulta rápida directa (Para ahorrar crear MesaDAO completo por ahora)
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id_mesa, numero_mesa FROM mesa WHERE estado = 'LIBRE'", null);

        if (cursor.moveToFirst()) {
            do {
                listaIdMesas.add(cursor.getInt(0));
                listaMesas.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // Llenar Spinner
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaMesas);
        spinnerMesas.setAdapter(adapterSpinner);

        if (listaMesas.isEmpty()) {
            Toast.makeText(this, "No hay mesas libres", Toast.LENGTH_LONG).show();
            btnConfirmar.setEnabled(false);
        }
    }

    // Método que se llama cuando presionan + o - en el adaptador
    @Override
    public void onTotalChange() {
        double total = 0;
        for (Producto p : listaProductos) {
            total += (p.getCantidadSolicitada() * p.getPrecio());
        }
        tvTotal.setText("S/. " + String.format("%.2f", total));
    }

    private void guardarPedido() {
        // 1. Calcular total real y filtrar productos con cantidad > 0
        double totalFinal = 0;
        ArrayList<DetallePedido> detalles = new ArrayList<>();

        for (Producto p : listaProductos) {
            if (p.getCantidadSolicitada() > 0) {
                detalles.add(new DetallePedido(p.getId(), p.getCantidadSolicitada(), p.getPrecio()));
                totalFinal += (p.getCantidadSolicitada() * p.getPrecio());
            }
        }

        if (detalles.isEmpty()) {
            Toast.makeText(this, "Seleccione al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Obtener Mesa Seleccionada
        int pos = spinnerMesas.getSelectedItemPosition();
        if (pos == -1) return; // Validación extra
        int idMesa = listaIdMesas.get(pos);

        // 3. Crear Objeto Pedido (Usuario ID 1 Admin por defecto)
        Pedido nuevoPedido = new Pedido(idMesa, 1, totalFinal);

        // 4. Guardar en BD
        PedidoDAO pedidoDAO = new PedidoDAO(this);
        boolean exito = pedidoDAO.registrarPedidoCompleto(nuevoPedido, detalles);

        if (exito) {
            Toast.makeText(this, "¡Pedido Enviado a Cocina!", Toast.LENGTH_LONG).show();
            finish(); // Cierra la pantalla y vuelve al menú
        } else {
            Toast.makeText(this, "Error al guardar pedido", Toast.LENGTH_SHORT).show();
        }
    }
}