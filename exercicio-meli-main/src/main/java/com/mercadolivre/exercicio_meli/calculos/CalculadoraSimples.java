package com.mercadolivre.exercicio_meli.calculos;

import java.util.Scanner;

public class CalculadoraSimples {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Digite o primeiro numero");
        int primeironumero = sc.nextInt();

        System.out.println("Escolha a operação matematica: 1 = adicao, 2 = multiplicação, 3 = divisao, 4 = subitracao");
        int escolha = sc.nextInt();

        System.out.println("Escolha o segundo numero");
        int segundonumero = sc.nextInt();
        int resultado = 0;

        switch (escolha) {
            case 1:
                resultado = primeironumero + segundonumero;
                break;
            case 2:
                resultado = primeironumero * segundonumero;
                break;
            case 3:
                resultado = primeironumero / segundonumero;
                break;
            case 4:
                resultado = primeironumero - segundonumero;
                break;
        }

        System.out.println("Resultado " + resultado);
        sc.close();

    }
}
