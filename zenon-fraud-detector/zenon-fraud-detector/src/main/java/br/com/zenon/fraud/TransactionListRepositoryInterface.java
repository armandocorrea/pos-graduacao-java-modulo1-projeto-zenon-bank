package br.com.zenon.fraud;

import java.util.Optional;

public interface TransactionListRepositoryInterface {
    Optional<Transaction> findByNameOrigin(String name);
    Optional<Transaction> findByLastTransactionByNameOrigin(String name);
}
