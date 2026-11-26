package br.com.dataflow;

/** Contadores de operacoes elementares feitas por um algoritmo. */
public final class Metricas {
    private long comparacoes;
    private long movimentos;

    public long comparacoes() { return comparacoes; }
    public long movimentos() { return movimentos; }
    void comparar() { comparacoes++; }
    void mover(long quantidade) { movimentos += quantidade; }
}
