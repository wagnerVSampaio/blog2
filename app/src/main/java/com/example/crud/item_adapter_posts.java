package com.example.crud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class item_adapter_posts extends RecyclerView.Adapter<item_adapter_posts.PostViewHolder> {

    private final Context context;
    private final List<Post> postList;

    public item_adapter_posts(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adapter_posts, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.titleTextView.setText(post.getTitulo());
        holder.descriptionTextView.setText(post.getPost());

        // Adiciona o clique no item para abrir a tela de detalhes
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, detalhes_post.class);
            // Passa os detalhes do post para a tela de detalhes
            intent.putExtra("title", post.getTitulo());
            intent.putExtra("description", post.getPost());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titulo);
            descriptionTextView = itemView.findViewById(R.id.descricao);
        }
    }
}
