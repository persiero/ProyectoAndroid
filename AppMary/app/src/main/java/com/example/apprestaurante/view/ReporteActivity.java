package com.example.apprestaurante.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apprestaurante.R;
import com.example.apprestaurante.data.PedidoDAO;
import com.example.apprestaurante.model.Pedido;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReporteActivity extends AppCompatActivity {

    EditText etInicio, etFin;
    Button btnBuscar;
    RecyclerView rv;
    TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        etInicio = findViewById(R.id.etFechaInicio);
        etFin = findViewById(R.id.etFechaFin);
        btnBuscar = findViewById(R.id.btnBuscarReporte);
        rv = findViewById(R.id.rvReporte);
        tvTotal = findViewById(R.id.tvTotalReporte);

        rv.setLayoutManager(new LinearLayoutManager(this));

        // Poner fecha de hoy por defecto para no tener que escribir tanto
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etInicio.setText(fechaHoy);
        etFin.setText(fechaHoy);

        btnBuscar.setOnClickListener(v -> generarReporte());
    }

    private void generarReporte() {
        String ini = etInicio.getText().toString();
        String fin = etFin.getText().toString();

        PedidoDAO dao = new PedidoDAO(this);
        ArrayList<Pedido> lista = dao.buscarPedidosPorFecha(ini, fin);

        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay ventas en ese rango", Toast.LENGTH_SHORT).show();
        }

        // Mostrar Lista
        ReporteAdapter adapter = new ReporteAdapter(lista);
        rv.setAdapter(adapter);

        // Calcular Suma Total
        double suma = 0;
        for (Pedido p : lista) {
            suma += p.getTotal();
        }
        tvTotal.setText("S/. " + String.format("%.2f", suma));
    }
}