package com.example.crud;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class CriarNovaPostagem extends AppCompatActivity {

    private EditText editTitulo, editDescricao;
    private Button buttonPublicar, buttonEscolherImagem;
    private ImageView imageViewVoltar, imageViewPostagem;
    private Database database;
    private Bitmap imagemSelecionada; // Para armazenar a imagem selecionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nova_postagem);

        // Inicializa o banco de dados
        database = new Database(this);

        // Referências aos componentes da interface
        editTitulo = findViewById(R.id.create_titulo);
        editDescricao = findViewById(R.id.createDescricao);
        buttonPublicar = findViewById(R.id.button_publicar);
        buttonEscolherImagem = findViewById(R.id.buttonUploadFoto);
        imageViewVoltar = findViewById(R.id.imageView4);
        imageViewPostagem = findViewById(R.id.fotoPostCreate);

        // Verifica se está editando uma postagem existente
        Intent intent = getIntent();
        if (intent.hasExtra("id")) { // Verifica se tem ID
            String titulo = intent.getStringExtra("title");
            String descricao = intent.getStringExtra("description");
            byte[] imagem = intent.getByteArrayExtra("image");

            // Preenche os campos
            editTitulo.setText(titulo);
            editDescricao.setText(descricao);

            // Se a imagem foi passada, configure-a
            if (imagem != null) {
                imagemSelecionada = Database.getBitmapFromBytes(imagem);
                imageViewPostagem.setImageBitmap(imagemSelecionada);
            }

            // Altera o título do botão para "Atualizar"
            buttonPublicar.setText("Atualizar");
        }

        // Listener para o botão de escolher imagem
        buttonEscolherImagem.setOnClickListener(view -> escolherImagem());

        // Listener para o botão de publicar
        buttonPublicar.setOnClickListener(view -> publicarPostagem());

        // Listener para o botão de voltar
        imageViewVoltar.setOnClickListener(view -> onBackPressed());

        // Ajuste de padding para insets do sistema
        View homeAppView = findViewById(R.id.main);
        if (homeAppView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(homeAppView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    /**
     * Método para escolher uma imagem da galeria.
     */
    private void escolherImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Escolha uma imagem"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                imagemSelecionada = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewPostagem.setImageBitmap(imagemSelecionada); // Exibe a imagem escolhida
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar a imagem.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método para publicar a nova postagem ou atualizar uma existente.
     */
    private void publicarPostagem() {
        String titulo = editTitulo.getText().toString().trim();
        String descricao = editDescricao.getText().toString().trim();

        // Validação dos campos
        if (TextUtils.isEmpty(titulo)) {
            editTitulo.setError("Título é obrigatório");
            editTitulo.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(descricao)) {
            editDescricao.setError("Descrição é obrigatória");
            editDescricao.requestFocus();
            return;
        }

        // Convertendo a imagem em byte[]
        byte[] imagemByteArray = null;
        if (imagemSelecionada != null) {
            imagemByteArray = Database.getBytesFromBitmap(imagemSelecionada);
        }

        // Verificando se é uma atualização ou uma nova postagem
        Intent intent = getIntent();
        if (intent.hasExtra("id")) { // Supondo que você tenha passado um ID da postagem
            int id = intent.getIntExtra("id", -1);
            int atualizado = database.updatePost(id, titulo, descricao, imagemByteArray); // Método para atualizar no banco

            if (atualizado <= 0) { // Se não foi atualizado com sucesso
                Toast.makeText(this, "Erro ao atualizar a postagem.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Postagem atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                // Retorna à tela inicial
                Intent homeIntent = new Intent(CriarNovaPostagem.this, Home.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish(); // Fecha a Activity atual
            }
        } else {
            // Inserção no banco de dados
            boolean sucesso = database.inserePost(titulo, descricao, imagemByteArray);
            if (sucesso) {
                Toast.makeText(this, "Postagem criada com sucesso!", Toast.LENGTH_SHORT).show();
                // Retorna à tela inicial
                Intent homeIntent = new Intent(CriarNovaPostagem.this, Home.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish(); // Fecha a Activity após a criação
            } else {
                Toast.makeText(this, "Erro ao criar a postagem.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
