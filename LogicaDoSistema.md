# Processar e analisar grandes volumes de dados em tempo hábil.

### Esses processos são criticos e estão muito lentos:

1. Validação de rotas duplicadas – verificar se um novo pedido já existe no sistema. 
2. Organização de entregas por prioridade – ordenar entregas por distância ou
valor

### Solicitado pela diretora - Relatório
A diretoria solicitou um relatório técnico que comprove, com dados empíricos, como a
escolha de algoritmos impacta diretamente a performance do sistema.

## Objetivo

 1. Aplicar os conceitos de análise assintótica (O, Ω, Θ) na prática
 
     Usado como báse: https://users.ime.usp.br/~pf/analise_de_algoritmos/aulas/Oh.html

 2. Implementar e comparar algoritmos com diferentes complexidades: Busca Linear, Busca Binária, Selection Sort, Merge Sort.

 3. Coletar e interpretar dados experimentais de desempenho separados e juntos, simulando 3 tipos diferentes de memória.

    Usado como base: https://wutools.com/pt/hardware/performance/teste-velocidade-memoria

 4. Produzir um relatório técnico fundamentado em evidências: Fotos da capaciade da máquina usada no teste e fotos das simulações.

 5. Final: Determinar por meio dos testes a melhor solução para o problema da *DataFlow Corp*.     

## O que deve ter no código de cada algoritimo?

1. Validar o processamento do código e cada parte percorrente;
2. Operar sobre vetores de inteiros;
3. Contar o número de comparações e trocas/movimentos;
4. Simulação de memória ram de 8gb, 16gb e 32gb; - **Não é uma exigencia do trabalho, é apenas um experimento técnico para testar e enriquecer o projeto.**
5. Medir o tempo de execução;

## Percurso do experimento

### Tamanhos de entrada: 

n= 1.000 | 5.000 | 10.000 | 60.000 | 100.000 | 400.000 | 700.000

### Tipo de ordenação:

Aleatório e Ordenado


### Procedimento:

1. Para cada tamanho, gerar o vetor de dados;
2. Executar cada algoritmo 4 vezes;
3. Descartar a primeira execução (warm-up);
4. Calcular a média das 3 execuções restantes.
