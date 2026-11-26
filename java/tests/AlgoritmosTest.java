package br.com.dataflow;

import java.util.Arrays;

/** Testes sem bibliotecas externas; falha lancando AssertionError. */
public final class AlgoritmosTest {
    private AlgoritmosTest() { }

    public static void main(String[] args) {
        int[] dados = {-4, 0, 3, 8, 12};
        exigir(Algoritmos.buscaLinear(dados, 8).indice() == 3, "linear existente");
        exigir(Algoritmos.buscaLinear(dados, 7).indice() == -1, "linear ausente");
        exigir(Algoritmos.buscaBinaria(dados, -4).indice() == 0, "binaria inicio");
        exigir(Algoritmos.buscaBinaria(dados, 12).indice() == 4, "binaria fim");
        exigir(Algoritmos.buscaBinaria(dados, 7).indice() == -1, "binaria ausente");
        exigir(Algoritmos.buscaLinear(new int[0], 1).indice() == -1, "linear vazio");
        exigir(Algoritmos.buscaBinaria(new int[0], 1).indice() == -1, "binaria vazia");

        int[] original = {5, -1, 5, 0, 99, -20};
        int[] esperado = {-20, -1, 0, 5, 5, 99};
        for (boolean merge : new boolean[]{false, true}) {
            ResultadoOrdenacao r = merge ? Algoritmos.mergeSort(original) : Algoritmos.selectionSort(original);
            exigir(Arrays.equals(r.valores(), esperado), "ordenacao crescente");
            exigir(r.metricas().comparacoes() >= 0 && r.metricas().movimentos() >= 0, "contadores nao negativos");
            exigir(Arrays.equals(original, new int[]{5, -1, 5, 0, 99, -20}), "entrada preservada");
        }
        exigir(Algoritmos.selectionSort(new int[0]).valores().length == 0, "selection vazio");
        exigir(Algoritmos.mergeSort(new int[]{7}).valores()[0] == 7, "merge unitario");
        exigir(!Algoritmos.estaOrdenado(new int[]{2, 1}), "validacao de precondicao");
        System.out.println("Todos os testes Java passaram.");
    }

    private static void exigir(boolean condicao, String caso) {
        if (!condicao) throw new AssertionError("Falha: " + caso);
    }
}
