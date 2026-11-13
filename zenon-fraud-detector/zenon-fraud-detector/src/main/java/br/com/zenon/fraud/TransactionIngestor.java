package br.com.zenon.fraud;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TransactionIngestor {

    public static List<Transaction> retornarTransacoesOldSchool(String nomeArquivo) {
        List<Transaction> transactions = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(nomeArquivo);
             Scanner scanner = new Scanner(fileInputStream)) {

            var lineCount = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineCount++;

                if (lineCount == 1) {
                    continue;
                }

                if (lineCount > 1001) {
                    break;
                }

                String[] colunas = line.split(",");
                transactions.add(
                    new Transaction(
                        Integer.parseInt(colunas[0]),
                        Type.valueOf(colunas[1]),
                        new BigDecimal(colunas[2]),
                        new Customer(colunas[3], new BigDecimal(colunas[4]), new BigDecimal(colunas[5])),
                        new Customer(colunas[6], new BigDecimal(colunas[7]), new BigDecimal(colunas[8])),
                        Boolean.parseBoolean(colunas[9]),
                        Boolean.parseBoolean(colunas[10])
                    )
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }

        return transactions;
    }

    public static List<Transaction> retornarTransacoes(String nomeArquivo) {
        List<Transaction> transactions = new ArrayList<>();

        Path caminho = Path.of(nomeArquivo);
        try {
            List<String> linhas = Files.readAllLines(caminho);

            for (int i = 1; i <= 1000; i++) {
                String[] colunas = linhas.get(i).split(",");

                transactions.add(
                        new Transaction(
                                Integer.parseInt(colunas[0]),
                                Type.valueOf(colunas[1]),
                                new BigDecimal(colunas[2]),
                                new Customer(colunas[3], new BigDecimal(colunas[4]), new BigDecimal(colunas[5])),
                                new Customer(colunas[6], new BigDecimal(colunas[7]), new BigDecimal(colunas[8])),
                                Boolean.parseBoolean(colunas[9]),
                                Boolean.parseBoolean(colunas[10])
                        )
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }

        return transactions;
    }

    public static List<Transaction> retornarTransacoesModerno(String nomeArquivo)  {
        List<Transaction> transactions = new ArrayList<>();

        try (var linhas = Files.lines(Path.of(nomeArquivo))) {
            linhas.skip(1)
                .limit(1000)
                .map(linha -> linha.split(","))
                .forEach(colunas -> transactions.add(
                    new Transaction(
                        Integer.parseInt(colunas[0]),
                        Type.valueOf(colunas[1]),
                        new BigDecimal(colunas[2]),
                        new Customer(colunas[3], new BigDecimal(colunas[4]), new BigDecimal(colunas[5])),
                        new Customer(colunas[6], new BigDecimal(colunas[7]), new BigDecimal(colunas[8])),
                        Boolean.parseBoolean(colunas[9]),
                        Boolean.parseBoolean(colunas[10])
                    )
                ));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }

        return transactions;
    }

}
