package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.Optional;

public interface TransactionListRepositoryInterface {
    Optional<Transaction> findByNameOrigin(String name);
    Optional<Transaction> findByLastTransactionByNameOrigin(String name);
}
