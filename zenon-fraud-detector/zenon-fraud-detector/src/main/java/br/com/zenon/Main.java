package br.com.zenon;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;
import br.com.zenon.fraud.repository.TransactionListRepository;
import br.com.zenon.fraud.repository.TransactionListRepositoryPerformatico;
import br.com.zenon.fraud.repository.TransactionListRepositoryInterface;
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

        /*
        TransactionListRepositoryInterface transactionListRepository = new TransactionListRepository(transactions);
        IO.println(transactionListRepository.findByNameOrigin("C1231006815").get());
        //IO.println(transactionListRepository.findByNameOrigin("C12345").get());

        var inicio = System.nanoTime();
        IO.println(transactionListRepository.findByLastTransactionByNameOrigin("C1868032458").get());
        var total = System.nanoTime() - inicio;
        IO.println("Tempo: " + total);

        TransactionListRepositoryInterface transactionListRepository2 = new TransactionListRepositoryPerformatico(transactions);
        IO.println(transactionListRepository2.findByNameOrigin("C1231006815").get());
        //IO.println(transactionListRepository.findByNameOrigin("C12345").get());

        inicio = System.nanoTime();
        IO.println(transactionListRepository2.findByLastTransactionByNameOrigin("C1868032458").get());
        total = System.nanoTime() - inicio;
        IO.println("Tempo: " + total);*/
    }
}