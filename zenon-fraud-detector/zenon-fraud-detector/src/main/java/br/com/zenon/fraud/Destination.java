package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Destination(
        String nameDest,
        BigDecimal oldBalanceDest,
        BigDecimal newBalanceDest) {}
