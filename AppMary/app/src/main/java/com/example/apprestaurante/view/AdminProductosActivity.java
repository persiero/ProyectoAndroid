package com.example.apprestaurante.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apprestaurante.R;
import com.example.apprestaurante.data.ProductoDAO;
import com.example.apprestaurante.model.Producto;

public class AdminProductosActivity extends AppCompatActivity {

    EditText etNombre, etPrecio, etStock;
    Spinner spinnerCategoria;
    Button btnGuardar;
    ProductoDAO productoDAO;

    // Categorías predefinidas (Deben coincidir en orden con la BD: 1=Caldos, 2=Segundos, 3=Bebidas)
    String[] categorias = {"Caldos", "Segundos", "Bebidas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_productos);

        // 1. Vincular vistas
        etNombre = findViewById(R.id.etNombreProducto);
        etPrecio = findViewById(R.id.etPrecioProducto);
        etStock = findViewById(R.id.etStockProducto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar = findViewById(R.id.btnGuardarProducto);

        // 2. Configurar Spinner (Lista desplegable)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategoria.setAdapter(adapter);

        // 3. Inicializar DAO
        productoDAO = new ProductoDAO(this);

        // 4. Botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarProducto();
            }
        });
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString();
        String precioStr = etPrecio.getText().toString();
        String stockStr = etStock.getText().toString();

        if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener ID de categoría (El Spinner da la posición 0, 1, 2... sumamos 1 para que sea ID de BD 1, 2, 3)
        int idCategoria = spinnerCategoria.getSelectedItemPosition() + 1;

        Producto nuevoProducto = new Producto(
                nombre,
                Double.parseDouble(precioStr),
                Integer.parseInt(stockStr),
                idCategoria
        );

        boolean exito = productoDAO.registrarProducto(nuevoProducto);

        if (exito) {
            Toast.makeText(this, "Producto Registrado Correctamente", Toast.LENGTH_LONG).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al guardar en SQLite", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etPrecio.setText("");
        etStock.setText("");
        etNombre.requestFocus();
    }
}