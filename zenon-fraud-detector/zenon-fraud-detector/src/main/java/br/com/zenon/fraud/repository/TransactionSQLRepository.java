package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.dao.TransactionDAO;
import br.com.zenon.fraud.dao.ZenonConnection;
import br.com.zenon.fraud.model.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    private final TransactionDAO dao;

    public TransactionSQLRepository(TransactionDAO dao) {
        this.dao = dao;
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        try {
            return Optional.ofNullable(
                dao.listarTransaction(ZenonConnection.getConnection(), originName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Transaction transaction) {
        try {
            dao.inserirTransaction(ZenonConnection.getConnection(), transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(List<Transaction> transactions) {
        try {
            dao.inserirLoteTransaction(ZenonConnection.getConnection(), transactions);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
