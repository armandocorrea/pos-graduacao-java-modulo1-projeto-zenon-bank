package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionListRepositoryPerformatico implements TransactionListRepositoryInterface {

    private Map<String, Transaction> mapTransaction = new HashMap<>();

    public TransactionListRepositoryPerformatico(List<Transaction> transaction) {
        transaction.forEach(t -> mapTransaction.put(t.origin().name(), t));
    }

    @Override
    public Optional<Transaction> findByNameOrigin(String name) {
        return Optional.of(Optional.ofNullable(mapTransaction.get(name))
            .orElseThrow(() -> new RuntimeException("Transação não encontrada para o cliente: " + name)));
    }

    @Override
    public Optional<Transaction> findByLastTransactionByNameOrigin(String name) {
        List<Transaction> filtrado =
            mapTransaction.entrySet()
                .stream()
                .filter(t -> t.getKey().equals(name))
                .map(Map.Entry::getValue)
                .toList();

        return Optional.of(Optional.of(filtrado.get(filtrado.size() - 1))
            .orElseThrow(() -> new RuntimeException("Transação não encontrada para o cliente: " + name)));
    }
}
