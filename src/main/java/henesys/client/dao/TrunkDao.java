package henesys.client.dao;

import henesys.client.Trunk;
import henesys.connection.db.DatabaseManager;

import java.sql.*;

public class TrunkDao {

    public int createTrunk(Trunk trunk) {
        String sql = "INSERT INTO trunk (slotCount, money) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, trunk.getSlotCount());
            statement.setInt(2, trunk.getMoney());
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
