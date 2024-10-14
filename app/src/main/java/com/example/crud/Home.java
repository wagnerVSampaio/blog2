package com.example.crud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button navigateButton = findViewById(R.id.navigateButton);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia a Activity de CriarNovaPostagem
                Intent intent = new Intent(Home.this, CriarNovaPostagem.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        posts = new ArrayList<>();

        loadPosts();
    }

    private void loadPosts() {
        Database database = new Database(this);
        Cursor cursor = database.getPosts();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                @SuppressLint("Range") String post = cursor.getString(cursor.getColumnIndex("post"));
                @SuppressLint("Range") byte[] imagem = cursor.getBlob(cursor.getColumnIndex("imagem"));

                posts.add(new Post(id, titulo, post, imagem));
            } while (cursor.moveToNext());
        }

        postAdapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(postAdapter);
    }
}
