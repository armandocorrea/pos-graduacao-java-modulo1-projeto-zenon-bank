package br.com.zenon.fraud.dao;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private final CustomerDAO customerDAO;

    private static final String SQL_INSERT = """
                                                 INSERT INTO TRANSACTION (step, type, amount, id_origin, id_recipient, isFraud, isflaggedFraud)
                                                                  VALUES (?, ?, ?, ?, ?, ?, ?)
                                             """;
    private static final String SQL_SELECT = """
                                                  SELECT TRANSACTION.id, step, type, amount, id_origin, id_recipient, isFraud, isflaggedFraud,
                                                         origin.name name_origin, origin.oldbalance oldbalance_origin, origin.newbalance newbalance_origin,
                                                         recipient.name name_recipient, recipient.oldbalance oldbalance_recipient, recipient.newbalance newbalance_recipient
                                                    FROM TRANSACTION
                                                   INNER JOIN CUSTOMER origin on origin.id = TRANSACTION.id_origin
                                                   INNER JOIN CUSTOMER recipient on recipient.id = TRANSACTION.id_recipient
                                                   WHERE origin.name = ?
                                                   LIMIT 1
                                             """;

    public TransactionDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void inserirLoteTransaction(Connection conn, List<Transaction> transactionList) throws SQLException {
        inserirOrigins(conn, transactionList);
        inserirRecipients(conn, transactionList);

        try (conn;
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < transactionList.size(); i++) {
                long id_origin = getIdCustomer(conn, transactionList.get(i).origin());
                long id_recipient = getIdCustomer(conn, transactionList.get(i).recipient());

                ps.setInt(1, transactionList.get(i).step());
                ps.setString(2, transactionList.get(i).type().name());
                ps.setBigDecimal(3, transactionList.get(i).amount());
                ps.setLong(4, id_origin);
                ps.setLong(5, id_recipient);
                ps.setBoolean(6, transactionList.get(i).isFraud());
                ps.setBoolean(7, transactionList.get(i).isFlaggedFraud());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.clearBatch();
            conn.commit();
        }
    }

    private void inserirOrigins(Connection conn, List<Transaction> transactionList) throws SQLException {
        List<Customer> origins = new ArrayList<>();
        transactionList
            .forEach(transaction -> {
                origins.add(new Customer(
                    transaction.origin().name(),
                    transaction.origin().oldBalance(),
                    transaction.origin().newBalance()
                ));
            });
        customerDAO.inserirLoteCustomer(conn, origins);
    }

    private void inserirRecipients(Connection conn, List<Transaction> transactionList) throws SQLException {
        List<Customer> recipients = new ArrayList<>();
        transactionList
            .forEach(transaction -> {
                recipients.add(new Customer(
                    transaction.recipient().name(),
                    transaction.recipient().oldBalance(),
                    transaction.recipient().newBalance()
                ));
            });
        customerDAO.inserirLoteCustomer(conn, recipients);
    }

    public void inserirTransaction(Connection conn, Transaction transaction) throws SQLException {
        try (conn) {
            long id_origin = getIdCustomer(conn, transaction.origin());
            long id_recipient = getIdCustomer(conn, transaction.recipient());

            try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, transaction.step());
                stmt.setString(2, transaction.type().name());
                stmt.setBigDecimal(3, transaction.amount());
                stmt.setLong(4, id_origin);
                stmt.setLong(5, id_recipient);
                stmt.setBoolean(6, transaction.isFraud());
                stmt.setBoolean(7, transaction.isFlaggedFraud());

                stmt.executeUpdate();
            }
        }
    }

    public Transaction listarTransaction(Connection conn, String originName) throws SQLException {
        try (conn) {
            try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {
                stmt.setString(1, originName);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Transaction(
                            rs.getInt("step"),
                            Type.valueOf(rs.getString("type")),
                            rs.getBigDecimal("amount"),
                            new Customer(
                                rs.getString("name_origin"),
                                rs.getBigDecimal("oldbalance_origin"),
                                rs.getBigDecimal("newbalance_origin")),
                            new Customer(
                                rs.getString("name_recipient"),
                                rs.getBigDecimal("oldbalance_recipient"),
                                rs.getBigDecimal("newbalance_recipient")),
                            (rs.getBoolean("isFraud")),
                            (rs.getBoolean("isflaggedFraud"))
                        );
                    }
                }
            }
        }

        return null;
    }

    private long getIdCustomer(Connection conn, Customer customer) throws SQLException {
        long id = customerDAO.listarCustomer(conn, customer.name());
        if (id == 0) {
            throw new SQLException("Customer não encontrado.");
        }
        return id;
    }
}
