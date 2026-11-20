package br.com.zenon;

import br.com.zenon.fraud.service.TransactionReport;
import br.com.zenon.fraud.service.TransactionReport.Statistics;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    void main(String[] args) {
        Locale locale = (args.length > 0 ? Locale.forLanguageTag(args[0]) : Locale.getDefault());

        ResourceBundle bundle = ResourceBundle.getBundle("reporties", locale);
        NumberFormat formatadorInteiro = NumberFormat.getIntegerInstance(locale);
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(locale);

        TransactionReport transactionReport = new TransactionReport("data/PS_20174392719_1491204439457_log.csv");
        Statistics statistics = transactionReport.generateReport();

        IO.println("""
            %s
            %s
            %s
        """.formatted(MessageFormat.format(bundle.getString("linhas"), formatadorInteiro.format(statistics.totalTransactions())),
            MessageFormat.format(bundle.getString("fraudes"), formatadorMoeda.format(statistics.totalFrauds())),
            MessageFormat.format(bundle.getString("total"), formatadorMoeda.format(statistics.totalAmount()))));
    }
}
