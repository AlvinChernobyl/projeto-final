package com.mercadolivre.exercicio_meli.praticapoo;

public class Livro {

    String titulo = "Gatos";
    String autor = "David";
    int anoPublicacao = 2019;

    public void exibirDetalhes() {
        System.out.println("Titulo: " +  titulo );
        System.out.println("Autor: " + autor);
        System.out.println("Ano Publicao: " + anoPublicacao);
    }
}
