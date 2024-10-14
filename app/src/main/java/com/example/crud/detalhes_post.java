package com.example.crud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class detalhes_post extends AppCompatActivity {

    private TextView tituloTextView;
    private TextView descricaoTextView;
    private ImageView imagemView;
    private ImageView editarButton, deleteButton;
    private LinearLayout layoutBackground;
    private int postId; // Para identificar o post

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_post);

        // Pega os dados da Intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        byte[] imagemByteArray = getIntent().getByteArrayExtra("image");
        postId = getIntent().getIntExtra("id", -1); // Recupera o ID do post

        // Exibe os dados na tela
        tituloTextView = findViewById(R.id.titulo);
        descricaoTextView = findViewById(R.id.descricao);
        imagemView = findViewById(R.id.Imagen_detalhe);
        editarButton = findViewById(R.id.editar);
        deleteButton = findViewById(R.id.delete);
        layoutBackground = findViewById(R.id.linearLayout); // O layout que você quer alterar o background

        tituloTextView.setText(title);
        descricaoTextView.setText(description);

        // Configurar a imagem
        if (imagemByteArray != null && imagemByteArray.length > 0) {
            Bitmap bitmap = Database.getBitmapFromBytes(imagemByteArray);

            // Definindo o Bitmap como background do layout
            BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
            layoutBackground.setBackground(drawable);

            // Opcional: também mostrar a imagem no ImageView
            imagemView.setImageBitmap(bitmap);
        } else {
            imagemView.setImageResource(R.drawable.foto_post_list); // Imagem padrão
        }

        // Configurando o botão de editar
        editarButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(detalhes_post.this, CriarNovaPostagem.class);
            editIntent.putExtra("id", postId);
            editIntent.putExtra("title", title);
            editIntent.putExtra("description", description);
            editIntent.putExtra("image", imagemByteArray);
            startActivity(editIntent);
        });

        // Configurando o botão de excluir
        deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog();
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Você tem certeza que deseja excluir este item?")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    // Lógica para excluir o item
                    deleteItem();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteItem() {
        Database database = new Database(this);
        boolean deletado = database.deletePost(postId);
        if (deletado) {
            Toast.makeText(this, "Postagem excluída com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(detalhes_post.this, Home.class); // Altere para sua Activity de lista
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpa a pilha de atividades
            startActivity(intent);
            finish(); // Fecha a Activity atual
        } else {
            Toast.makeText(this, "Erro ao excluir a postagem.", Toast.LENGTH_SHORT).show();
        }
    }

}
