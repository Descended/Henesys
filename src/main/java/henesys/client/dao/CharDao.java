package henesys.client.dao;

import henesys.client.character.Char;
import henesys.connection.db.DatabaseManager;

import java.sql.*;

public class CharDao {

    public int createCharacter(int accountId, int characterStatId, int avatarLookId) {
        String sql = "INSERT INTO characters (accountId, characterStatId, avatarLookId) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, accountId);
            statement.setInt(2, characterStatId);
            statement.setInt(3, avatarLookId);
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
