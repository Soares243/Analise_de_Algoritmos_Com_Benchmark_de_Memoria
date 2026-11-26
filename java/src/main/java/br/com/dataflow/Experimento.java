package br.com.dataflow;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/** Runner de benchmark: quatro rodadas, primeira descartada. */
public final class Experimento {
    private static final int[] TAMANHOS = {100, 1_000, 5_000, 10_000, 50_000,
            60_000, 100_000, 400_000, 500_000, 700_000};
    private static final int LIMITE_SELECTION = 10_000;
    private static final Path RAIZ = Path.of(System.getProperty("user.dir")).toAbsolutePath();

    private Experimento() { }

    public static void main(String[] args) throws IOException {
        int ram = 16;
        int[] tamanhos = TAMANHOS;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--ram") && i + 1 < args.length) ram = Integer.parseInt(args[++i]);
            else if (args[i].equals("--sizes") && i + 1 < args.length) {
                tamanhos = Arrays.stream(Arrays.copyOfRange(args, i + 1, args.length))
                        .mapToInt(Integer::parseInt).toArray(); break;
            } else throw new IllegalArgumentException("Uso: --ram 8|16|32 [--sizes n ...]");
        }
        if (ram != 8 && ram != 16 && ram != 32) throw new IllegalArgumentException("RAM simulada deve ser 8, 16 ou 32");
        var osBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.printf(Locale.ROOT, "Sistema: %s %s | Java %s | CPUs logicas: %d%n",
                System.getProperty("os.name"), System.getProperty("os.version"),
                Runtime.version(), osBean.getAvailableProcessors());
        Path saida = executar(tamanhos, ram);
        System.out.println("Resultados gravados em " + saida);
    }

    static Path executar(int[] tamanhos, int perfilRam) throws IOException {
        Path pasta = RAIZ.resolve("dados");
        Files.createDirectories(pasta);
        Path arquivo = pasta.resolve("resultados_" + perfilRam + "gb.csv");
        List<String> linhas = new ArrayList<>();
        Random gerador = new Random(20261125L);
        for (int n : tamanhos) {
            int[] aleatorio = new int[n];
            for (int i = 0; i < n; i++) aleatorio[i] = gerador.nextInt(Math.max(n * 10, 1));
            int[] ordenado = Arrays.copyOf(aleatorio, n);
            Arrays.sort(ordenado);
            executarCenario(linhas, perfilRam, n, "aleatorio", aleatorio, ordenado);
            executarCenario(linhas, perfilRam, n, "ordenado", ordenado, ordenado);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            writer.write("ram_configurada_gb,n,distribuicao,consulta,algoritmo,tempo_medio_ms,comparacoes_media,movimentos_media,status\n");
            for (String linha : linhas) { writer.write(linha); writer.newLine(); }
        }
        return arquivo;
    }

    private static void executarCenario(List<String> linhas, int ram, int n, String distribuicao,
                                        int[] vetor, int[] crescente) {
        if (!Algoritmos.estaOrdenado(crescente)) throw new IllegalStateException("Vetor de busca binaria fora de ordem");
        for (String consulta : List.of("existente", "ausente")) {
            int alvo = consulta.equals("existente") && n > 0 ? vetor[n - 1] : -1;
            adicionarBusca(linhas, ram, n, distribuicao, consulta, "Busca Linear", vetor, alvo);
            adicionarBusca(linhas, ram, n, distribuicao, consulta, "Busca Binaria", crescente, alvo);
        }
        adicionarOrdenacao(linhas, ram, n, distribuicao, "Selection Sort", vetor, n > LIMITE_SELECTION);
        adicionarOrdenacao(linhas, ram, n, distribuicao, "Merge Sort", vetor, false);
    }

    private static void adicionarBusca(List<String> linhas, int ram, int n, String dist,
                                       String consulta, String algoritmo, int[] dados, int alvo) {
        long tempoTotal = 0, comparacoesTotal = 0, movimentosTotal = 0;
        for (int rodada = 0; rodada < 4; rodada++) {
            long inicio = System.nanoTime();
            ResultadoBusca r = algoritmo.equals("Busca Linear")
                    ? Algoritmos.buscaLinear(dados, alvo) : Algoritmos.buscaBinaria(dados, alvo);
            long decorrido = System.nanoTime() - inicio;
            boolean achou = r.indice() >= 0;
            if (achou != consulta.equals("existente")) throw new IllegalStateException("Busca retornou indice incorreto");
            if (rodada > 0) {
                tempoTotal += decorrido;
                comparacoesTotal += r.metricas().comparacoes();
                movimentosTotal += r.metricas().movimentos();
            }
        }
        linhas.add(linha(ram, n, dist, consulta, algoritmo, tempoTotal, comparacoesTotal, movimentosTotal, "executado"));
    }

    private static void adicionarOrdenacao(List<String> linhas, int ram, int n, String dist,
                                           String algoritmo, int[] dados, boolean omitir) {
        if (omitir) {
            linhas.add(ram + "," + n + "," + dist + ",nao_aplicavel," + algoritmo + ",,,,omitido: custo O(n^2)");
            return;
        }
        int[] esperado = Arrays.copyOf(dados, dados.length);
        Arrays.sort(esperado);
        long tempoTotal = 0, comparacoesTotal = 0, movimentosTotal = 0;
        for (int rodada = 0; rodada < 4; rodada++) {
            long inicio = System.nanoTime();
            ResultadoOrdenacao r = algoritmo.equals("Selection Sort")
                    ? Algoritmos.selectionSort(dados) : Algoritmos.mergeSort(dados);
            long decorrido = System.nanoTime() - inicio;
            if (!Arrays.equals(r.valores(), esperado)) throw new IllegalStateException(algoritmo + " produziu ordem incorreta");
            if (rodada > 0) {
                tempoTotal += decorrido;
                comparacoesTotal += r.metricas().comparacoes();
                movimentosTotal += r.metricas().movimentos();
            }
        }
        linhas.add(linha(ram, n, dist, "nao_aplicavel", algoritmo, tempoTotal, comparacoesTotal, movimentosTotal, "executado"));
    }

    private static String linha(int ram, int n, String dist, String consulta, String algoritmo,
                                long nanos, long comparacoes, long movimentos, String status) {
        double tempoMs = nanos / 3.0 / 1_000_000.0;
        return String.format(Locale.ROOT, "%d,%d,%s,%s,%s,%.6f,%.2f,%.2f,%s",
                ram, n, dist, consulta, algoritmo, tempoMs, comparacoes / 3.0, movimentos / 3.0, status);
    }
}
