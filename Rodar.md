# Como compilar e executar

Requisito: JDK 24 (ou superior).

No PowerShell, a partir da pasta raiz:

```powershell
New-Item -ItemType Directory -Force build | Out-Null
javac -d build java/src/main/java/br/com/dataflow/*.java java/tests/AlgoritmosTest.java
java -cp build br.com.dataflow.AlgoritmosTest
```

Executar os benchmarks completos:

```powershell
java -cp build br.com.dataflow.Experimento --ram 8
java -cp build br.com.dataflow.Experimento --ram 16
java -cp build br.com.dataflow.Experimento --ram 32
```

Para uma execução curta, acrescente `--sizes 100 1000`. Os perfis de RAM são rótulos de simulação; os testes usam a mesma máquina física.