package br.com.zenon.fraud.dao;

import br.com.zenon.fraud.model.Customer;

import java.sql.*;

public class CustomerDAO {

    private static final String SQL_INSERT = "INSERT INTO CUSTOMER (name, oldBalance, newBalance) VALUES (?, ?, ?)";
    private static final String SQL_SELECT = """
                                                  SELECT id
                                                    FROM CUSTOMER
                                                   WHERE CUSTOMER.name = ?
                                             """;

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
