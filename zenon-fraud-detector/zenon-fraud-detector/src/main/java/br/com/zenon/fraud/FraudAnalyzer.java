package br.com.zenon.fraud;

import java.util.*;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    private List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
        this.loadFraudes();
    }

    private void loadFraudes() {
        List<Transaction> fraudes = transactions.stream().filter(Transaction::isFraud).toList();

        IO.println("1. Total de Fraudes: " + fraudes.size());

        IO.println("2. Top 3 Fraudes de Maior Valor: ");
        fraudes.stream()
            .sorted(Comparator.comparing(Transaction::amount).reversed())
            .limit(3)
            .forEach(transaction -> IO.println(transaction.amount()));

        IO.println("3. Clientes Suspeitos: ");
        fraudes.stream()
            .sorted(Comparator.comparing(Transaction::amount).reversed())
            .limit(5)
            .forEach(transaction -> IO.println(transaction.origin().name()));

        IO.println("4. Prejuízo Total: " +
        ((Double) fraudes.stream().mapToDouble(transaction -> transaction.amount().floatValue()).sum()).doubleValue());

        IO.println("5. Fraudes por Tipo: ");
        Map<Type, Long> fraudesTipo =
            fraudes.stream().collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
        fraudesTipo.forEach((key, value) -> IO.println(" - " + key + ": " + value));
    }



}
