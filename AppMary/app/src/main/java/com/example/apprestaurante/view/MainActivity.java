package com.example.apprestaurante.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apprestaurante.R;

public class MainActivity extends AppCompatActivity {

    Button btnTomarPedido, btnMesas, btnAdminProductos, btnReportes, btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Vincular botones
        btnTomarPedido = findViewById(R.id.btnTomarPedido);
        btnMesas = findViewById(R.id.btnMesas);
        btnAdminProductos = findViewById(R.id.btnAdminProductos);
        btnReportes = findViewById(R.id.btnReportes);
        btnSalir = findViewById(R.id.btnSalir);

        // 2. Configurar Eventos Click

        // IR A TOMAR PEDIDO
        btnTomarPedido.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TomarPedidoActivity.class);
            startActivity(intent);
        });

        // IR A MONITOR DE MESAS
        btnMesas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MesasActivity.class);
            startActivity(intent);
        });

        // IR A ADMIN PRODUCTOS
        btnAdminProductos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminProductosActivity.class);
            startActivity(intent);
        });

        // IR A REPORTES
        btnReportes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReporteActivity.class);
            startActivity(intent);
        });

        // CERRAR SESIÓN
        btnSalir.setOnClickListener(v -> {
            // Volver al Login y borrar historial para que no pueda volver atrás con el botón físico
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
        });

        Button btnClientes = findViewById(R.id.btnClientes);
        btnClientes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegistroClienteActivity.class));
        });

    }
}