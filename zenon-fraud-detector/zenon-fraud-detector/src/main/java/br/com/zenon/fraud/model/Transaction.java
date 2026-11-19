package br.com.zenon.fraud.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(
        int step,
        Type type,
        BigDecimal amount,
        Customer origin,
        Customer recipient,
        boolean isFraud,
        boolean isFlaggedFraud) {

    public Transaction {
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if (step < 1) throw new IllegalArgumentException("step should be positive: " + step);
        if (amount.floatValue() < 0) throw new IllegalArgumentException("amount should be positive: " + amount);
    }

}