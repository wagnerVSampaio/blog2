package com.example.crud;

public class Post {
    private final int id;
    private final String titulo;
    private final String post;
    private final byte[] imagem;

    public Post(int id, String titulo, String post, byte[] imagem) {
        this.id = id;
        this.titulo = titulo;
        this.post = post;
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPost() {
        return post;
    }

    public byte[] getImagem() {
        return imagem;
    }
}
