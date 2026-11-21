package br.com.zenon;

import br.com.zenon.fraud.dao.CustomerDAO;
import br.com.zenon.fraud.dao.TransactionDAO;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;
import br.com.zenon.fraud.service.TransactionIngestor;

import java.util.List;

public class DBMain {

    void main () {
        List<Transaction> transactions =
            TransactionIngestor.retornarTransacoes("data/PS_20174392719_1491204439457_log.csv");

        var inicio = System.nanoTime();
        //transactions.forEach(this::addTransaction);
        addTransactions(transactions);
        var fim = (System.nanoTime() - inicio) / 1_000_000.0;
        IO.println("Tempo total de inserção (ms): " + fim);

        findTransation("C1231006815");
        findTransation("C12345");
    }

    private void addTransactions(List<Transaction> transactions) {
        repository().save(transactions);
    }

    private void addTransaction(Transaction transaction) {
        repository().save(transaction);
    }

    private void findTransation(String name) {
        repository().findByOriginName(name)
            .ifPresentOrElse(IO::println, ()-> IO.println("Transação não encontrada!"));
    }

    private TransactionRepository repository() {
        CustomerDAO customerDAO = new CustomerDAO();
        TransactionDAO transactionDAO = new TransactionDAO(customerDAO);
        return new TransactionSQLRepository(transactionDAO);
    }
}
