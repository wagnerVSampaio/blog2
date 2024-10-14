package com.example.crud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Sign_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        // Configuração do layout para ajustar com as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeApp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para ser chamado no clique do botão "ENTRAR"
    public void goToNextActivity(View view) {
        // Criando um Intent para navegar para a próxima tela
        Intent intent = new Intent(Sign_in.this, Home.class);
        startActivity(intent);
    }
}
