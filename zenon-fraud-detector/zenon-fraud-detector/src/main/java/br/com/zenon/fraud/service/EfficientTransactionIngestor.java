package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class EfficientTransactionIngestor {

    private static final Integer FRAUD_LIMIT = 10_000;
    private static final int BATCH_SIZE = 500;
    private static final int THREAD_POOL_SIZE = 4;

    public void readAsStream(String nomeArquivo, Consumer<Transaction> consumer) {
        try {
            var linhas = Files.readAllLines(Path.of(nomeArquivo));
            linhas.stream()
                .skip(1)
                .limit(FRAUD_LIMIT)
                .forEach(linha -> consumer.accept(parseTransaction(linha)));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public void readBatch(String nomeArquivo, Consumer<List<Transaction>> consumer) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<?>> futures = new ArrayList<>();
        List<Transaction> currentBatch = new ArrayList<>(BATCH_SIZE);

        try {
            var linhas = Files.readAllLines(Path.of(nomeArquivo));
            linhas.stream()
                .skip(1)
                //.limit(FRAUD_LIMIT)
                .forEach(linha -> {
                    addTransaction(linha, currentBatch);

                    if (currentBatch.size() >= BATCH_SIZE) {
                        List<Transaction> batchParaEnviar = new ArrayList<>(currentBatch);
                        currentBatch.clear();
                        futures.add(executor.submit(() -> salvarComRetry(batchParaEnviar, consumer)));
                    }
                });

            if (!currentBatch.isEmpty()) {
                List<Transaction> batchFinal = new ArrayList<>(currentBatch);
                futures.add(executor.submit(() -> salvarComRetry(batchFinal, consumer)));
            }

            for (Future<?> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private void addTransaction(String linha, List<Transaction> transactions) {
        transactions.add(this.parseTransaction(linha));
    }

    private Transaction parseTransaction(String linha) {
        try {
            String[] colunas = linha.split(",");
            return
                new Transaction(
                    Integer.parseInt(colunas[0]),
                    Type.valueOf(colunas[1]),
                    new BigDecimal(colunas[2]),
                    new Customer(colunas[3], new BigDecimal(colunas[4]), new BigDecimal(colunas[5])),
                    new Customer(colunas[6], new BigDecimal(colunas[7]), new BigDecimal(colunas[8])),
                    (!Objects.equals(colunas[9], "0")),
                    (!Objects.equals(colunas[10], "0"))
                );
        } catch (Exception e) {
            System.err.println("Erro: " + linha + " | " + e);
        }
        return null;
    }

    private void salvarComRetry(List<Transaction> batch, Consumer<List<Transaction>> consumer) {
        int maxTentativas = 3;
        int tentativa = 0;

        while (true) {
            try {
                consumer.accept(batch);
                return; // Sucesso, sai do loop
            } catch (Exception e) {
                tentativa++;
                // Verifica se a causa raiz é Deadlock (código de erro 1213 no MySQL)
                boolean isDeadlock = e.getMessage() != null && e.getMessage().contains("Deadlock found");

                if (!isDeadlock || tentativa >= maxTentativas) {
                    throw e; // Se não for deadlock ou estourou tentativas, lança o erro
                }

                try {
                    // Pausa aleatória (backoff) entre 50ms e 150ms para dessincronizar as threads
                    Thread.sleep(50L + (long) (Math.random() * 100));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrompida durante retry de deadlock", ie);
                }
            }
        }
    }

}
