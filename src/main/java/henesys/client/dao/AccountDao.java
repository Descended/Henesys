package henesys.client.dao;

import henesys.client.Account;
import henesys.client.User;
import henesys.connection.db.DatabaseManager;

import java.sql.*;

public class AccountDao {

    public Account findByUserAndWorld(User user, int worldId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE userId = ? AND worldId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            statement.setInt(2, worldId);
            ResultSet rs = statement.executeQuery();
            {
                if (rs.next()) {
                    account = new Account(user, worldId);
                    account.setId(rs.getInt("id"));
                    account.setWorldId(rs.getInt("worldId"));
                    account.setNxCredit(rs.getInt("nxCredit"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    public int createAccount(Account account, int userId, int worldId, int trunkId) {
        String sql = "INSERT INTO account (worldId, userId, trunkId, nxCredit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, worldId);
            statement.setInt(2, userId);
            statement.setInt(3, trunkId);
            statement.setInt(4, account.getNxCredit());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
