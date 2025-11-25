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

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnIngresar;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Vincular vistas
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);

        // 2. Inicializar DAO
        usuarioDAO = new UsuarioDAO(this);

        // 3. Configurar Botón
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarLogin();
            }
        });
    }

    private void validarLogin() {
        String user = etUsuario.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar a la BD
        Usuario usuarioLogueado = usuarioDAO.login(user, pass);

        if (usuarioLogueado != null) {
            Toast.makeText(this, "Bienvenido " + usuarioLogueado.getNombreCompleto(), Toast.LENGTH_LONG).show();

            // Navegar al Menú Principal (MainActivity)
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra el login para que no puedan volver atrás
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}