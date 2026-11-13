package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.util.List;

public class Main {

    void main() {

        List<Transaction> transactions =
            TransactionIngestor.retornarTransacoes("data/PS_20174392719_1491204439457_log.csv");

        List<Transaction> transactions2 =
            TransactionIngestor.retornarTransacoesModerno("data/PS_20174392719_1491204439457_log.csv");

        List<Transaction> transactions3 =
            TransactionIngestor.retornarTransacoesOldSchool("data/PS_20174392719_1491204439457_log.csv");


        IO.println("Antigo");
        transactions.stream().limit(10).forEach(IO::println);

        IO.println("Moderno");
        transactions2.stream().limit(10).forEach(IO::println);

        IO.println("Bem Antigo");
        transactions3.stream().limit(10).forEach(IO::println);
    }
}