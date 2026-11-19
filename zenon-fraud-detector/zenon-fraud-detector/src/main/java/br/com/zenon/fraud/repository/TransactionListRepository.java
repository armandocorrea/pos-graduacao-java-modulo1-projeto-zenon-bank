package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionListRepositoryInterface {

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findByNameOrigin(String name) {
        return Optional.of(transactions.stream()
            .filter(transaction -> transaction.origin().name().equals(name))
            .findFirst().orElseThrow(() -> new RuntimeException("Transação não encontrada para o cliente: " + name)));
    }

    @Override
    public Optional<Transaction> findByLastTransactionByNameOrigin(String name) {
        List<Transaction> filtrado =
            transactions.stream()
                .filter(transaction -> transaction.origin().name().equals(name))
                .toList();

        return Optional.of(Optional.of(filtrado.get(filtrado.size() - 1))
            .orElseThrow(() -> new RuntimeException("Transação não encontrada para o cliente: " + name)));
    }


}
