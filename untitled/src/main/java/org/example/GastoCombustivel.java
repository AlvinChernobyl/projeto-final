package org.example;

import java.util.Scanner;

import java.util.Scanner;

public class GastoCombustivel {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int tempo = sc.nextInt();
        int velocidade = sc.nextInt();

        int distancia = tempo * velocidade;
        double litrosUsados = distancia / 12.0;

        System.out.printf("%.3f%n", litrosUsados);

        sc.close(); // boa prática: fechar o scanner
    }
}

