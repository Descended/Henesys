package henesys.client.dao;

import henesys.client.character.avatar.AvatarLook;
import henesys.connection.db.DatabaseManager;

import java.sql.*;

public class AvatarLookDao {

    public int createAvatarLook(AvatarLook avatarLook) {
        String sql = "INSERT INTO avatarLook (gender, skin, face, hair, weaponId) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setByte(1, avatarLook.getGender());
            statement.setByte(2, avatarLook.getSkin());
            statement.setInt(3, avatarLook.getFace());
            statement.setInt(4, avatarLook.getHair());
            statement.setInt(5, avatarLook.getWeaponId());
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
