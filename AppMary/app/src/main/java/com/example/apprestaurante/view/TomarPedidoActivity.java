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

import android.widget.RadioGroup;
import android.widget.RadioButton;
import com.example.apprestaurante.data.ClienteDAO;
import com.example.apprestaurante.model.Cliente;

public class TomarPedidoActivity extends AppCompatActivity implements ProductoAdapter.OnTotalChangeListener {

    Spinner spinnerMesas;
    RecyclerView rvProductos;
    TextView tvTotal;
    Button btnConfirmar;
    Spinner spinnerClientes; // Nuevo
    RadioGroup rgComprobante; // Nuevo


    ArrayList<Producto> listaProductos;
    ProductoAdapter adapter;
    ArrayList<String> listaMesas;
    ArrayList<Integer> listaIdMesas; // Para saber el ID real de la mesa seleccionada
    ArrayList<String> listaNombresClientes; // Para el spinner visual
    ArrayList<String> listaDnisClientes;   // Para saber cuál es cual


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_pedido);

        // 1. Vincular
        spinnerMesas = findViewById(R.id.spinnerMesas);
        rvProductos = findViewById(R.id.rvProductos);
        tvTotal = findViewById(R.id.tvTotalPedido);
        btnConfirmar = findViewById(R.id.btnConfirmarPedido);
        // 1. Vincular Nuevos Controles
        spinnerClientes = findViewById(R.id.spinnerClientes); // Nuevo
        rgComprobante = findViewById(R.id.rgComprobante);     // Nuevo


        // 2. Cargar Mesas (Spinner)
        cargarMesas();
        cargarClientes(); // Llamamos al nuevo método

        // 3. Cargar Productos (RecyclerView)
        ProductoDAO productoDAO = new ProductoDAO(this);
        listaProductos = productoDAO.obtenerTodos();

        adapter = new ProductoAdapter(listaProductos, this);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvProductos.setAdapter(adapter);

        // 4. Botón Confirmar
        btnConfirmar.setOnClickListener(v -> guardarPedido());
    }

    private void cargarClientes() {
        listaNombresClientes = new ArrayList<>();
        listaDnisClientes = new ArrayList<>();

        ClienteDAO clienteDAO = new ClienteDAO(this);
        ArrayList<Cliente> clientesBD = clienteDAO.listarClientes();

        for (Cliente c : clientesBD) {
            listaNombresClientes.add(c.getNombreRazonSocial());
            listaDnisClientes.add(c.getDniRuc());
        }

        ArrayAdapter<String> adapterCli = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaNombresClientes);
        spinnerClientes.setAdapter(adapterCli);
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
        // --- PARTE 1: Calcular Totales y Validar Productos ---
        double totalFinal = 0;
        ArrayList<DetallePedido> detalles = new ArrayList<>();

        // Recorremos la lista para ver qué pidió el usuario
        for (Producto p : listaProductos) {
            if (p.getCantidadSolicitada() > 0) {
                // Creamos el detalle
                detalles.add(new DetallePedido(p.getId(), p.getCantidadSolicitada(), p.getPrecio()));
                // Sumamos al total
                totalFinal += (p.getCantidadSolicitada() * p.getPrecio());
            }
        }

        // Validación: ¿Eligió algo?
        if (detalles.isEmpty()) {
            Toast.makeText(this, "Seleccione al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- PARTE 2: Obtener Datos del Formulario ---

        // A) Validar Tipo de Comprobante
        int idRadioSeleccionado = rgComprobante.getCheckedRadioButtonId();
        if (idRadioSeleccionado == -1) {
            Toast.makeText(this, "Seleccione tipo de comprobante", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton rbSeleccionado = findViewById(idRadioSeleccionado);
        String tipoComprobante = rbSeleccionado.getText().toString().toUpperCase(); // TICKET, BOLETA, FACTURA

        // B) Obtener Cliente Seleccionado
        int posCliente = spinnerClientes.getSelectedItemPosition();
        if (posCliente == -1 || listaDnisClientes.isEmpty()) {
            Toast.makeText(this, "Debe registrar clientes primero", Toast.LENGTH_SHORT).show();
            return;
        }
        String dniCliente = listaDnisClientes.get(posCliente);

        // C) Validación RUC para Factura
        if (tipoComprobante.equals("FACTURA")) {
            if (dniCliente.length() != 11) {
                Toast.makeText(this, "Para Factura se requiere un cliente con RUC (11 dígitos)", Toast.LENGTH_LONG).show();
                return; // Detiene el proceso
            }
        }

        // D) Obtener ID real del cliente
        ClienteDAO cliDao = new ClienteDAO(this);
        int idClienteReal = cliDao.obtenerIdPorDni(dniCliente);

        // E) Obtener Mesa
        int posMesa = spinnerMesas.getSelectedItemPosition();
        int idMesa = listaIdMesas.get(posMesa);

        // --- PARTE 3: Guardar en Base de Datos ---

        // Crear Objeto Pedido (Ahora con 5 argumentos)
        // 1:Mesa, 2:Usuario(Admin), 3:Cliente, 4:TipoDoc, 5:Total
        Pedido nuevoPedido = new Pedido(idMesa, 1, idClienteReal, tipoComprobante, totalFinal);

        // Llamar al DAO
        PedidoDAO pedidoDAO = new PedidoDAO(this);
        boolean exito = pedidoDAO.registrarPedidoCompleto(nuevoPedido, detalles);

        if (exito) {
            Toast.makeText(this, "¡Venta Registrada! (" + tipoComprobante + ")", Toast.LENGTH_LONG).show();
            finish(); // Cierra la pantalla
        } else {
            Toast.makeText(this, "Error al guardar en SQLite", Toast.LENGTH_SHORT).show();
        }
    }

}