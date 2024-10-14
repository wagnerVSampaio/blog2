package com.example.crud;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<Post> posts;
    private final Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adapter_posts, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tituloPostCard.setText(post.getTitulo());

        // Truncar a descrição a 10 palavras
        String descricaoCompleta = post.getPost();
        String descricaoTruncada = truncateDescription(descricaoCompleta, 10);
        holder.descPostCard.setText(descricaoTruncada);

        // Definir a imagem (se houver)
        if (post.getImagem() != null && post.getImagem().length > 0) {
            holder.imagePostCard.setImageBitmap(BitmapFactory.decodeByteArray(post.getImagem(), 0, post.getImagem().length));
        } else {
            holder.imagePostCard.setImageResource(R.drawable.foto_post_list); // Imagem padrão
        }

        // Adicionar evento de clique para abrir a tela de detalhes
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, detalhes_post.class);
            // Passar as informações do post para a tela de detalhes
            intent.putExtra("title", post.getTitulo());
            intent.putExtra("description", post.getPost());
            intent.putExtra("image", post.getImagem()); // Passar a imagem como byte[]
            intent.putExtra("id", post.getId()); // Passar o ID para facilitar a edição/exclusão
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    /**
     * Método para truncar a descrição a um número específico de palavras.
     *
     * @param descricao    A descrição completa.
     * @param maxPalavras  O número máximo de palavras desejadas.
     * @return A descrição truncada.
     */
    private String truncateDescription(String descricao, int maxPalavras) {
        if (descricao == null || descricao.isEmpty()) {
            return "";
        }

        String[] palavras = descricao.split("\\s+"); // Divide por espaços
        if (palavras.length <= maxPalavras) {
            return descricao;
        }

        StringBuilder descricaoTruncada = new StringBuilder();
        for (int i = 0; i < maxPalavras; i++) {
            descricaoTruncada.append(palavras[i]).append(" ");
        }
        descricaoTruncada.append("..."); // Adiciona reticências no final
        return descricaoTruncada.toString().trim();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tituloPostCard;
        TextView descPostCard;
        ImageView imagePostCard;

        PostViewHolder(View itemView) {
            super(itemView);
            tituloPostCard = itemView.findViewById(R.id.tituloPostCard);
            descPostCard = itemView.findViewById(R.id.descPostCard);
            imagePostCard = itemView.findViewById(R.id.imagePostCard);
        }
    }
}
