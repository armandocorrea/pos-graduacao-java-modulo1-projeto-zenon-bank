package br.com.zenon;

import br.com.zenon.fraud.Customer;
import br.com.zenon.fraud.Destination;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.Type;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        Transaction transaction01 = new Transaction(
                1,
                Type.PAYMENT,
                new BigDecimal("9839.64"),
                new Customer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new Destination("M1979787155", new BigDecimal(0), new BigDecimal(0)),
                false,
                false);

        Transaction transaction02 = new Transaction(
                743,
                Type.CASH_OUT,
                new BigDecimal("850002.52"),
                new Customer("C1280323807", new BigDecimal("850002.52"), new BigDecimal(0)),
                new Destination("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true,
                false);

        System.out.println("Transação 1: ");
        System.out.println(" step: " + transaction01.step());
        System.out.println(" type: " + transaction01.type());
        System.out.println(" amount: " + transaction01.amount());
        System.out.println(" nameOrig: " + transaction01.customer().nameOrig());
        System.out.println(" oldbalanceOrg: " + transaction01.customer().oldBalanceOrg());
        System.out.println(" newbalanceOrig: " + transaction01.customer().newBalanceOrig());
        System.out.println(" nameDest: " + transaction01.destination().nameDest());
        System.out.println(" oldbalanceDest: " + transaction01.destination().oldBalanceDest());
        System.out.println(" newbalanceDest: " + transaction01.destination().newBalanceDest());
        System.out.println(" isFraud: " + transaction01.isFraud());
        System.out.println(" isFlaggedFraud: " + transaction01.isFlaggedFraud());

        System.out.println("----");

        System.out.println("Transação 2: ");
        System.out.println(" step: " + transaction02.step());
        System.out.println(" type: " + transaction02.type());
        System.out.println(" amount: " + transaction02.amount());
        System.out.println(" nameOrig: " + transaction02.customer().nameOrig());
        System.out.println(" oldbalanceOrg: " + transaction02.customer().oldBalanceOrg());
        System.out.println(" newbalanceOrig: " + transaction02.customer().newBalanceOrig());
        System.out.println(" nameDest: " + transaction02.destination().nameDest());
        System.out.println(" oldbalanceDest: " + transaction02.destination().oldBalanceDest());
        System.out.println(" newbalanceDest: " + transaction02.destination().newBalanceDest());
        System.out.println(" isFraud: " + transaction02.isFraud());
        System.out.println(" isFlaggedFraud: " + transaction02.isFlaggedFraud());
    }
}