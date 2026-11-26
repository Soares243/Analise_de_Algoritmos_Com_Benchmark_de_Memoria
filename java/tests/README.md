# Projeto Java — DataFlow Corp.

Implementacao em Java 24, sem dependencias externas. Veja o README da raiz para contexto e interpretacao do experimento.

## Compilar e executar

No diretorio raiz do projeto:

```powershell
New-Item -ItemType Directory -Force build | Out-Null
javac -d build java/src/main/java/br/com/dataflow/*.java java/tests/AlgoritmosTest.java
java -cp build br.com.dataflow.AlgoritmosTest
java -cp build br.com.dataflow.Experimento --ram 8
java -cp build br.com.dataflow.Experimento --ram 16
java -cp build br.com.dataflow.Experimento --ram 32
```

Para uma coleta curta: `java -cp build br.com.dataflow.Experimento --ram 16 --sizes 100 1000`.

Cada busca cobre alvo presente e ausente. Selection Sort e medido ate n=10.000; para os tamanhos maiores, o CSV registra a omissao por custo quadratico. A primeira de quatro rodadas e descartada. A correcao e conferida fora da regiao cronometrada.
