package com.example.apprestaurante.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apprestaurante.R;
import com.example.apprestaurante.data.ClienteDAO;
import com.example.apprestaurante.model.Cliente;

public class RegistroClienteActivity extends AppCompatActivity {

    EditText etNombre, etDni, etTel, etDir, etPass;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        etNombre = findViewById(R.id.etNombreCliente);
        etDni = findViewById(R.id.etDniRuc);
        etTel = findViewById(R.id.etTelefono);
        etDir = findViewById(R.id.etDireccion);
        etPass = findViewById(R.id.etPasswordCliente);
        btnGuardar = findViewById(R.id.btnGuardarCliente);

        btnGuardar.setOnClickListener(v -> guardarCliente());
    }

    private void guardarCliente() {
        String nom = etNombre.getText().toString();
        String dni = etDni.getText().toString();
        String pass = etPass.getText().toString();

        if (nom.isEmpty() || dni.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Nombre, DNI y Clave son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Cliente c = new Cliente(nom, dni, etTel.getText().toString(), etDir.getText().toString(), pass);
        ClienteDAO dao = new ClienteDAO(this);

        if (dao.registrarCliente(c)) {
            Toast.makeText(this, "Cliente Registrado", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error: El DNI ya existe", Toast.LENGTH_SHORT).show();
        }
    }
}