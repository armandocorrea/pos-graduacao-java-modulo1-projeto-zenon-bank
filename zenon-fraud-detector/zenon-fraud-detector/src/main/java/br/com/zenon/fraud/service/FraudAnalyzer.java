package br.com.zenon.fraud.service;

import br.com.zenon.fraud.model.Type;
import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    public Long countFraudes() {
        return fraundStream()
            .count();
    }

    public List<BigDecimal> findHighestValueFraudsAmounts(int limit) {
        return highValueFraudStream()
            .map(Transaction::amount)
            .limit(limit)
            .toList();
    }

    public List<String> findTopSuspiciousClients(int limit) {
        return highValueFraudStream()
            .map(transaction -> transaction.origin().name())
            .distinct()
            .limit(limit)
            .toList();
    }

    public BigDecimal calculateTotalFraudLoss() {
        return fraundStream()
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Type, Long> countFraudsByType() {
        return fraundStream()
            .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }

    private Stream<Transaction> highValueFraudStream() {
        return fraundStream()
            .sorted(Comparator.comparing(Transaction::amount).reversed());
    }

    private Stream<Transaction> fraundStream() {
        return transactions
            .stream()
            .filter(Transaction::isFraud);
    }
}
