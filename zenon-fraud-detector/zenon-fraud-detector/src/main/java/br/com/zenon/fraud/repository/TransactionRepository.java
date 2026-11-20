package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String originName);
    void save(Transaction transaction);
    void save(List<Transaction> transactions);
}
