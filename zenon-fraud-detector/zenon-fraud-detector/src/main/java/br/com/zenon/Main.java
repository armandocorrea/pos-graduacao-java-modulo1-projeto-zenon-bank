package br.com.zenon;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;
import br.com.zenon.fraud.repository.TransactionListRepository;
import br.com.zenon.fraud.repository.TransactionMapRepository;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.service.FraudAnalyzer;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {

    void main() {
        List<Transaction> transactionsWithErrors =
            TransactionIngestor.retornarTransacoes("data/paysim_with_bad_data.csv");
        IO.println(transactionsWithErrors.size());
        transactionsWithErrors.forEach(IO::println);

        List<Transaction> transactions =
          TransactionIngestor.retornarTransacoes("data/PS_20174392719_1491204439457_log.csv");

        FraudAnalyzer fraudAnalyzer = new FraudAnalyzer(transactions);
        IO.println("Total de fraudes: " + fraudAnalyzer.countFraudes());

        List<BigDecimal> highestFraudsAmounts = fraudAnalyzer.findHighestValueFraudsAmounts(3);
        IO.println("Top 3 Fraudes de Maior Valor: ");
        highestFraudsAmounts.forEach(amount -> IO.println("- %.2f".formatted(amount)));

        List<String> suspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
        IO.println("Top 5 Clientes Suspeitos: ");
        suspiciousClients.forEach(IO::println);

        BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
        IO.println("Prejuízo Total: " + totalFraudLoss);

        Map<Type, Long> fraudCountByType = fraudAnalyzer.countFraudsByType();
        IO.println("Prejuízo Total: ");
        fraudCountByType.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));

        IO.println("");
        IO.println("======BENCHMARK======");

        TransactionRepository transactionRepository;

        transactionRepository = new TransactionListRepository(transactions);
        transactionRepository.findByOriginName("C1231006815")
            .ifPresentOrElse(IO::println,
                () -> IO.println("Transação não encontrada para o OriginName: C1231006815"));
        transactionRepository.findByOriginName("C12345")
            .ifPresentOrElse(IO::println,
                () -> IO.println("Transação não encontrada para o OriginName: C12345"));

        var inicio = System.nanoTime();
        transactionRepository.findByOriginName("C1868032458")
            .ifPresentOrElse(IO::println,
                () -> IO.println("Transação não encontrada para o OriginName: C1868032458"));
        var total = (System.nanoTime() - inicio) / 1_000_000.0;
        IO.println("Tempo List em (ms): " + total);

        IO.println("");

        transactionRepository = new TransactionMapRepository(transactions);
        transactionRepository.findByOriginName("C1231006815")
            .ifPresentOrElse(IO::println,
                () -> IO.println("Transação não encontrada para o OriginName: C1231006815"));
        transactionRepository.findByOriginName("C12345")
            .ifPresentOrElse(IO::println,
                () -> IO.println("Transação não encontrada para o OriginName: C12345"));

        inicio = System.nanoTime();
        transactionRepository.findByOriginName("C1868032458")
            .ifPresentOrElse(IO::println,
                () -> IO.println("Transação não encontrada para o OriginName: C1868032458"));
        total = (System.nanoTime() - inicio) / 1_000_000.0;
        IO.println("Tempo Map em (ms): " + total);
    }
}