package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Type;
import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TransactionIngestor {

    private static final Integer FRAUD_LIMIT = 100_000;

    public static List<Transaction> retornarTransacoes(String nomeArquivo)  {
        List<Transaction> transactions = new ArrayList<>();

        try {
            var linhas = Files.readAllLines(Path.of(nomeArquivo));
            linhas.stream()
                .skip(1)
                .limit(FRAUD_LIMIT)
                .forEach(linha -> addTransaction(linha, transactions));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }

        return transactions;
    }

    private static void addTransaction(String linha, List<Transaction> transactions) {
        try {
            String[] colunas = linha.split(",");
            transactions.add(
                new Transaction(
                    Integer.parseInt(colunas[0]),
                    Type.valueOf(colunas[1]),
                    new BigDecimal(colunas[2]),
                    new Customer(colunas[3], new BigDecimal(colunas[4]), new BigDecimal(colunas[5])),
                    new Customer(colunas[6], new BigDecimal(colunas[7]), new BigDecimal(colunas[8])),
                    (!Objects.equals(colunas[9], "0")),
                    (!Objects.equals(colunas[10], "0"))
                ));
        } catch (Exception e) {
            System.err.println("Erro: " + linha + " | " + e);
        }
    }

    /*public static List<Transaction> retornarTransacoesOldSchool(String nomeArquivo) {
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
                addTransaction(colunas, transactions);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }

        return transactions;
    }*/

    /*public static List<Transaction> retornarTransacoes(String nomeArquivo) {
        List<Transaction> transactions = new ArrayList<>();

        Path caminho = Path.of(nomeArquivo);
        try {
            List<String> linhas = Files.readAllLines(caminho);

            for (int i = 1; i <= 1000; i++) {
                String[] colunas = linhas.get(i).split(",");

                addTransaction(colunas, transactions);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }

        return transactions;
    }*/

}
