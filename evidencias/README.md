# Evidencias para anexar

O enunciado solicita fotos reais da capacidade da maquina e das simulacoes.

```powershell
Get-CimInstance Win32_OperatingSystem | Select-Object Caption
Get-CimInstance Win32_Processor | Select-Object -First 1 Name
Get-CimInstance Win32_ComputerSystem | Select-Object @{Name='RAM_GB';Expression={[math]::Round($_.TotalPhysicalMemory/1GB,2)}}
```

```powershell
java -cp build br.com.dataflow.Experimento --ram 8
```

```powershell
java -cp build br.com.dataflow.Experimento --ram 16
```

```powershell
java -cp build br.com.dataflow.Experimento --ram 32
```
