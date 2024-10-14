package com.example.crud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class Database extends SQLiteOpenHelper {

    private static final String NOME = "blog.db";
    private static final int VERSION = 4;  // Atualizando a versão do banco para 4

    private static final String TABELA_POSTS = "posts";
    private static final String ID = "id";
    private static final String TITULO = "titulo";
    private static final String POST = "post";
    private static final String IMAGEM = "imagem";  // Nova coluna para a imagem

    // Criação da tabela com a nova coluna de imagem
    private static final String CREATE_TABLE = "CREATE TABLE " + TABELA_POSTS + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITULO + " TEXT, " +
            POST + " TEXT, " +
            IMAGEM + " BLOB)";

    public Database(Context context) {
        super(context, NOME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.i("Tabela", "Tabela de posts criada!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 4) {  // Modifiquei para comparar com a nova versão
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELA_POSTS); // Exclui a tabela antiga
            onCreate(sqLiteDatabase); // Cria a nova tabela
        }
    }

    // Método para inserir um post com uma imagem
    public boolean inserePost(String titulo, String post, byte[] imagem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put(TITULO, titulo);
        dados.put(POST, post);
        dados.put(IMAGEM, imagem);  // Adicionando imagem como BLOB

        long resultado = db.insert(TABELA_POSTS, null, dados);
        if (resultado != -1) {
            Log.i("resultado do insert", "Dados inseridos retornaram " + resultado);
        } else {
            Log.e("insert", "Erro ao inserir dados");
        }
        db.close();

        return resultado != -1;
    }

    // Método para buscar todos os posts
    public Cursor getPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultados = db.rawQuery("SELECT * FROM " + TABELA_POSTS, null);

        if (resultados != null && resultados.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = resultados.getInt(resultados.getColumnIndex(ID));
                @SuppressLint("Range") String titulo = resultados.getString(resultados.getColumnIndex(TITULO));
                @SuppressLint("Range") String post = resultados.getString(resultados.getColumnIndex(POST));
                @SuppressLint("Range") byte[] imagem = resultados.getBlob(resultados.getColumnIndex(IMAGEM));  // Recuperando imagem

                Log.d("resultados", "ID: " + id + ", Título: " + titulo + ", Texto do post: " + post + ", Imagem: " + (imagem != null ? "Sim" : "Não"));
            } while (resultados.moveToNext());
        } else {
            Log.e("getPost", "Nenhum dado encontrado");
        }

        return resultados; // Retornando o cursor
    }

    // Método para atualizar um post, incluindo a imagem
    public int updatePost(int id, String titulo, String post, byte[] imagem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues dados = new ContentValues();

        dados.put(TITULO, titulo);
        dados.put(POST, post);
        dados.put(IMAGEM, imagem);  // Atualizando a imagem

        int resultado = db.update(TABELA_POSTS, dados, ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        return resultado;
    }

    // Método para deletar um post
    public boolean deletePost(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int retorno = db.delete(TABELA_POSTS, ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return retorno > 0;
    }

    // Método para converter byte[] em Bitmap
    public static Bitmap getBitmapFromBytes(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    // Método para converter Bitmap em byte[]
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
