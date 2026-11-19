package br.com.zenon;

import br.com.zenon.fraud.*;

import java.util.List;

public class Main {

    void main() {

       /* List<Transaction> transactions =
            TransactionIngestor.retornarTransacoes("data/PS_20174392719_1491204439457_log.csv");*/

       List<Transaction> transactions2 =
            TransactionIngestor.retornarTransacoesModerno("data/PS_20174392719_1491204439457_log.csv");

       /* List<Transaction> transactions3 =
            TransactionIngestor.retornarTransacoesOldSchool("data/PS_20174392719_1491204439457_log.csv");*/

       /* List<Transaction> transactions4 =
            TransactionIngestor.retornarTransacoesModerno("data/paysim_with_bad_data.csv");*/

       /* IO.println("Antigo");
        transactions.stream().limit(10).forEach(IO::println);

        IO.println("Moderno");
        transactions2.stream().limit(10).forEach(IO::println);

        IO.println("Bem Antigo");
        transactions3.stream().limit(10).forEach(IO::println);*/

        /*IO.println(transactions4.size());
        transactions4.forEach(IO::println);*/

        /*IO.println("Moderno");
        transactions2.stream().limit(10).forEach(IO::println);
        FraudAnalyzer fraudAnalyzer = new FraudAnalyzer(transactions2);*/

        TransactionListRepositoryInterface transactionListRepository = new TransactionListRepository(transactions2);
        IO.println(transactionListRepository.findByNameOrigin("C1231006815").get());
        //IO.println(transactionListRepository.findByNameOrigin("C12345").get());

        var inicio = System.nanoTime();
        IO.println(transactionListRepository.findByLastTransactionByNameOrigin("C1868032458").get());
        var total = System.nanoTime() - inicio;
        IO.println("Tempo: " + total);

        TransactionListRepositoryInterface transactionListRepository2 = new TransactionListRepository2(transactions2);
        IO.println(transactionListRepository2.findByNameOrigin("C1231006815").get());
        //IO.println(transactionListRepository.findByNameOrigin("C12345").get());

        inicio = System.nanoTime();
        IO.println(transactionListRepository2.findByLastTransactionByNameOrigin("C1868032458").get());
        total = System.nanoTime() - inicio;
        IO.println("Tempo: " + total);
    }
}