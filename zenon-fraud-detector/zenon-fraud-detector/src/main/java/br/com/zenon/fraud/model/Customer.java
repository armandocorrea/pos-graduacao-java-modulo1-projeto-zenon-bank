package br.com.zenon.fraud.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Customer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance) {

    public Customer {
        Objects.requireNonNull(name);
        Objects.requireNonNull(oldBalance);
        Objects.requireNonNull(newBalance);

        if (name.trim().isEmpty()) throw new IllegalArgumentException("name should not be empty");
        if (newBalance.floatValue() < 0) throw new IllegalArgumentException("newBalance should be positive: " + newBalance());
        if (oldBalance.floatValue() < 0) throw new IllegalArgumentException("oldBalance should be positive: " + oldBalance());
    }

}
