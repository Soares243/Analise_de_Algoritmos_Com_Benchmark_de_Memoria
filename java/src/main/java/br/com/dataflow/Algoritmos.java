package br.com.dataflow;

import java.util.Arrays;

/** Implementacoes didaticas de busca e ordenacao em vetores de inteiros. */
public final class Algoritmos {
    private Algoritmos() { }

    public static ResultadoBusca buscaLinear(int[] valores, int alvo) {
        validar(valores);
        Metricas m = new Metricas();
        for (int i = 0; i < valores.length; i++) {
            m.comparar();
            if (valores[i] == alvo) return new ResultadoBusca(i, m);
        }
        return new ResultadoBusca(-1, m);
    }

    /** O chamador deve fornecer vetor crescente; validar fora do cronometro. */
    public static ResultadoBusca buscaBinaria(int[] valores, int alvo) {
        validar(valores);
        Metricas m = new Metricas();
        int inicio = 0, fim = valores.length - 1;
        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;
            m.comparar();
            if (valores[meio] == alvo) return new ResultadoBusca(meio, m);
            m.comparar();
            if (valores[meio] < alvo) inicio = meio + 1;
            else fim = meio - 1;
        }
        return new ResultadoBusca(-1, m);
    }

    public static ResultadoOrdenacao selectionSort(int[] entrada) {
        validar(entrada);
        int[] a = Arrays.copyOf(entrada, entrada.length);
        Metricas m = new Metricas();
        for (int i = 0; i < a.length - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < a.length; j++) {
                m.comparar();
                if (a[j] < a[menor]) menor = j;
            }
            if (menor != i) {
                int temp = a[i]; a[i] = a[menor]; a[menor] = temp;
                m.mover(3); // duas atribuicoes ao vetor e uma variavel temporaria
            }
        }
        return new ResultadoOrdenacao(a, m);
    }

    public static ResultadoOrdenacao mergeSort(int[] entrada) {
        validar(entrada);
        Metricas m = new Metricas();
        int[] copia = Arrays.copyOf(entrada, entrada.length);
        int[] auxiliar = new int[entrada.length];
        ordenar(copia, auxiliar, 0, copia.length, m);
        return new ResultadoOrdenacao(copia, m);
    }

    private static void ordenar(int[] a, int[] aux, int inicio, int fim, Metricas m) {
        if (fim - inicio < 2) return;
        int meio = inicio + (fim - inicio) / 2;
        ordenar(a, aux, inicio, meio, m);
        ordenar(a, aux, meio, fim, m);
        int i = inicio, j = meio, k = inicio;
        while (i < meio && j < fim) {
            m.comparar();
            aux[k++] = a[i] <= a[j] ? a[i++] : a[j++];
            m.mover(1);
        }
        while (i < meio) { aux[k++] = a[i++]; m.mover(1); }
        while (j < fim) { aux[k++] = a[j++]; m.mover(1); }
        for (int p = inicio; p < fim; p++) { a[p] = aux[p]; m.mover(1); }
    }

    private static void validar(int[] valores) {
        if (valores == null) throw new IllegalArgumentException("Vetor nao pode ser nulo");
    }

    /** Verifica a precondicao da busca binaria fora da regiao cronometrada. */
    public static boolean estaOrdenado(int[] valores) {
        validar(valores);
        for (int i = 1; i < valores.length; i++) if (valores[i - 1] > valores[i]) return false;
        return true;
    }
}
