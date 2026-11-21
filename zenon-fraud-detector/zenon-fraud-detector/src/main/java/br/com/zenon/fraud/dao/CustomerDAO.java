package br.com.zenon.fraud.dao;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;

import java.sql.*;
import java.util.List;

public class CustomerDAO {

    private static final String SQL_INSERT = "INSERT IGNORE INTO CUSTOMER (name, oldBalance, newBalance) VALUES (?, ?, ?)";
    private static final String SQL_SELECT = """
                                                  SELECT id
                                                    FROM CUSTOMER
                                                   WHERE CUSTOMER.name = ?
                                             """;

    public void inserirLoteCustomer(Connection conn, List<Customer> customerList) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < customerList.size(); i++) {
                ps.setString(1, customerList.get(i).name());
                ps.setBigDecimal(2, customerList.get(i).newBalance());
                ps.setBigDecimal(3, customerList.get(i).oldBalance());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.clearBatch();
            conn.commit();
        }
    }

    public long inserirCustomer(Connection conn, Customer customer) throws SQLException {
        long id = 0;
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.name());
            stmt.setBigDecimal(2, customer.newBalance());
            stmt.setBigDecimal(3, customer.oldBalance());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    return (rs.next() ? rs.getLong(1): 0);
                }
            }
        }
        return id;
    }

    public long listarCustomer(Connection conn, String originName) throws SQLException {
        long id = 0;
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT)) {
            stmt.setString(1, originName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return id;
    }
}
