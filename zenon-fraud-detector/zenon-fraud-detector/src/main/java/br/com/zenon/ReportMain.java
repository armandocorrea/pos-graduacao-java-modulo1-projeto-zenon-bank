package br.com.zenon;

import br.com.zenon.fraud.service.TransactionReport;
import br.com.zenon.fraud.service.TransactionReport.Statistics;

public class ReportMain {

    void main() {
        TransactionReport transactionReport = new TransactionReport("data/PS_20174392719_1491204439457_log.csv");
        Statistics statistics = transactionReport.generateReport();
        IO.println("""
            Total de Linhas: %d
            Total de Fraudes: %d
            Valor Total Transacionado: %.2f
        """.formatted(statistics.totalTransactions(), statistics.totalFrauds(), statistics.totalAmount()));
    }
}
