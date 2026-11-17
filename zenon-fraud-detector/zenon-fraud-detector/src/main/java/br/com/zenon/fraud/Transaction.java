package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
        int step,
        Type type,
        BigDecimal amount,
        Customer origin,
        Customer recipient,
        boolean isFraud,
        boolean isFlaggedFraud) {

    public Transaction {
        if (step < 1) {
            throw new IllegalArgumentException("step should be positive: " + step);
        }
        if (amount.floatValue() < 0) {
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }
        if (origin.name() == null || origin.name().isBlank()) {
            throw new IllegalArgumentException("name should not be empty");
        }
        if (origin.newBalance().floatValue() < 0) {
            throw new IllegalArgumentException("newBalance should be positive: " + origin.newBalance());
        }
        if (origin.oldBalance().floatValue() < 0) {
            throw new IllegalArgumentException("oldBalance should be positive: " + origin.oldBalance());
        }
        if (recipient.name() == null || recipient.name().isBlank()) {
            throw new IllegalArgumentException("name should not be empty");
        }
        if (recipient.newBalance().floatValue() < 0) {
            throw new IllegalArgumentException("newBalance should be positive: " + recipient.newBalance());
        }
        if (recipient.oldBalance().floatValue() < 0) {
            throw new IllegalArgumentException("oldBalance should be positive: " + recipient.oldBalance());
        }
    }

}