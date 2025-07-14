package com.mercadolivre.exercicio_meli.calculos;

import java.util.Scanner;

public class Fatorial {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite um numero");
        int numero = sc.nextInt();
        long fatorial = 1;

        for (int i = 1; i <= numero; i++) {
            fatorial *= i;
        }
        System.out.println("Fatorial de " + numero + " e " + fatorial);
        sc.close();
    }
}
