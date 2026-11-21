package br.com.zenon;

import br.com.zenon.fraud.dao.CustomerDAO;
import br.com.zenon.fraud.dao.TransactionDAO;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.service.EfficientTransactionIngestor;

public class IngestionMain {

    void main() {
        var inicio = System.nanoTime();

        EfficientTransactionIngestor efficientTransactionIngestor = new EfficientTransactionIngestor();

        /*efficientTransactionIngestor.readAsStream("data/PS_20174392719_1491204439457_log.csv", (transaction) -> {
            repository().save(transaction);
        });*/

        efficientTransactionIngestor.readBatch("data/PS_20174392719_1491204439457_log.csv", (transactions) -> {
            repository().save(transactions);
        });

        var fim = (System.nanoTime() - inicio) / 1_000_000.0;
        IO.println("Tempo total de inserção (ms): " + fim);
    }

    private TransactionRepository repository() {
        CustomerDAO customerDAO = new CustomerDAO();
        TransactionDAO transactionDAO = new TransactionDAO(customerDAO);
        return new TransactionSQLRepository(transactionDAO);
    }
}
