package br.com.zenon.fraud.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private final String nomeArquivo;

    private record ReportTransaction(BigDecimal amount, boolean isFraude){

    }

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {

        private final static Statistics ZERO = new Statistics(0, 0, BigDecimal.ZERO);

        private Statistics addReportTransaction(ReportTransaction rt) {
            return new Statistics(
                totalTransactions + 1,
                totalFrauds + (rt.isFraude ? 1 : 0),
                totalAmount.add(rt.amount)
            );
        }

        private Statistics add(Statistics other) {
            return new Statistics(totalTransactions + other.totalTransactions,
                totalFrauds + other.totalFrauds,
                totalAmount.add(other.totalAmount)
            );
        }
    }

    public TransactionReport(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Statistics generateReport() {
        Path path = Path.of(nomeArquivo);

        try (Stream<String> lines = Files.lines(path)) {
            return lines
                .skip(1)
                .map(this::parseReportTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Statistics.ZERO, Statistics::addReportTransaction, Statistics::add);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private Optional<ReportTransaction> parseReportTransaction(String line) {
        try {
            String[] colunas = line.split(",");
            return Optional.of(new ReportTransaction(
                new BigDecimal(colunas[2]),
                (!Objects.equals(colunas[9], "0"))));
        } catch (Exception e) {
            System.err.println("Erro: " + line + " | " + e);
        }

        return Optional.empty();
    }
}
