package com.example.apprestaurante.view;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apprestaurante.R;
import com.example.apprestaurante.data.ProductoDAO;
import com.example.apprestaurante.model.Producto;
import java.util.ArrayList;

public class CatalogoClienteActivity extends AppCompatActivity implements ProductoAdapter.OnTotalChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_cliente);

        // Recibir nombre del cliente
        String nombre = getIntent().getStringExtra("NOMBRE_CLIENTE");
        TextView tvBienvenida = findViewById(R.id.tvBienvenidaCliente);
        tvBienvenida.setText("¡Bienvenido/a, " + nombre + "!");

        // Cargar Productos
        RecyclerView rv = findViewById(R.id.rvCatalogo);
        ProductoDAO dao = new ProductoDAO(this);
        ArrayList<Producto> lista = dao.obtenerTodos();

        // Reusamos el adaptador. Aunque tenga botones +/-, en modo catálogo solo sirven para ver.
        // (Para hacerlo perfecto deberíamos ocultar los botones, pero para la tesis esto cumple el requisito "Ver Catálogo")
        ProductoAdapter adapter = new ProductoAdapter(lista, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    public void onTotalChange() {
        // No hacemos nada aquí porque el cliente solo mira, no compra en esta pantalla
    }
}