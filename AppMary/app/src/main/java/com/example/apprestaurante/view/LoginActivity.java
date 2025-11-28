package com.example.apprestaurante.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apprestaurante.R; // Asegúrate de que esta R sea la de tu paquete
import com.example.apprestaurante.data.UsuarioDAO;
import com.example.apprestaurante.model.Usuario;

// ... imports ...
import android.widget.Switch;
import com.example.apprestaurante.data.ClienteDAO;
import com.example.apprestaurante.model.Cliente;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnIngresar;
    private Switch switchCliente; // Nuevo
    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO; // Nuevo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);
        switchCliente = findViewById(R.id.switchModoCliente); // Nuevo

        usuarioDAO = new UsuarioDAO(this);
        clienteDAO = new ClienteDAO(this); // Nuevo

        // Cambio visual pequeño
        switchCliente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etUsuario.setHint("Ingrese su DNI");
                etUsuario.setText(""); // Limpiar
                etPassword.setText("");
            } else {
                etUsuario.setHint("Nombre de Usuario (Admin)");
            }
        });

        btnIngresar.setOnClickListener(v -> validarLogin());
    }

    private void validarLogin() {
        String user = etUsuario.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (switchCliente.isChecked()) {
            // --- MODO CLIENTE ---
            Cliente cliente = clienteDAO.loginCliente(user, pass);
            if (cliente != null) {
                Toast.makeText(this, "Acceso Cliente Correcto", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, CatalogoClienteActivity.class);
                intent.putExtra("NOMBRE_CLIENTE", cliente.getNombreRazonSocial());
                startActivity(intent);
                // No cerramos el login (finish) para que pueda volver atrás y entrar como admin si quiere
            } else {
                Toast.makeText(this, "DNI o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }

        } else {
            // --- MODO ADMINISTRADOR (Tu código anterior) ---
            Usuario usuario = usuarioDAO.login(user, pass);
            if (usuario != null) {
                Toast.makeText(this, "Bienvenido " + usuario.getNombreCompleto(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}