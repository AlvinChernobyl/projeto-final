package com.mercadolivre.exercicio_meli.calculos;

import java.util.Scanner;

public class ParImpar {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite um numero");
        int numero = sc.nextInt();

        if (numero % 2 == 0) {
            System.out.println("Numero PAR");
        } else {
            System.out.println("Numero IMPAR");
        }
        sc.close();
    }
}
