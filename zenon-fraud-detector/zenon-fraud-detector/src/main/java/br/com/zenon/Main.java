package br.com.zenon;

import br.com.zenon.fraud.Customer;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.Type;

import java.math.BigDecimal;

public class Main {

    void main() {
        var transaction01 = new Transaction(
                1,
                Type.PAYMENT,
                new BigDecimal("9839.64"),
                new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new Customer("M1979787155", new BigDecimal(0), new BigDecimal(0)),
                false,
                false);

        var transaction02 = new Transaction(
                743,
                Type.CASH_OUT,
                new BigDecimal("850002.52"),
                new Customer("C1280323807", new BigDecimal("850002.52"), new BigDecimal(0)),
                new Customer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true,
                false);

        IO.println("Transação 1: ");
        IO.println(transaction01.toString());

        IO.println("----");

        IO.println("Transação 2: ");
        IO.println(transaction02.toString());
    }
}