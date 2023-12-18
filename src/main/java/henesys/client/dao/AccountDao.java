package henesys.client.dao;

import henesys.client.Account;
import henesys.client.User;
import henesys.connection.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
