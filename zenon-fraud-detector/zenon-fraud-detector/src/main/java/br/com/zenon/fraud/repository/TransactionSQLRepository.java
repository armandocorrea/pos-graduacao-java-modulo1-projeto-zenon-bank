package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.dao.TransactionDAO;
import br.com.zenon.fraud.dao.ZenonConnection;
import br.com.zenon.fraud.model.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    private final ZenonConnection conn;
    private final TransactionDAO dao;

    public TransactionSQLRepository(ZenonConnection conn, TransactionDAO dao) {
        this.conn = conn;
        this.dao = dao;
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        try {
            return Optional.ofNullable(
                dao.listarTransaction(conn.zenonConnection(), originName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Transaction transaction) {
        try {
            dao.inserirTransaction(conn.zenonConnection(), transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(List<Transaction> transactions) {
        try {
            dao.inserirLoteTransaction(conn.zenonConnection(), transactions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
