package com.mercadolivre.exercicio_meli.praticapoo;

import java.util.ArrayList;

public class Bliblioteca {

    ArrayList<Livro> livros = new ArrayList<>();
    Livro livro = new Livro();

    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }
    public void listarLivro() {
        for (Livro livro : livros) {
            livro.exibirDetalhes();
            System.out.println("---------");
        }
    }
}
