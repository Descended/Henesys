package henesys.client.dao;

import henesys.client.character.avatar.AvatarLook;
import henesys.connection.db.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void saveEquips(int avatarLookId, List<Integer> equips) {
        String sql = "INSERT INTO hairEquip (avatarLookId, equipId) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            for (int equipId : equips) {
                statement.setInt(1, avatarLookId);
                statement.setInt(2, equipId);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Integer> getEquips(int avatarLookId) {
        String sql = "SELECT * FROM hairEquip WHERE avatarLookId = ?";
        List<Integer> equips = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, avatarLookId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                equips.add(rs.getInt("equipId"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return equips;
    }

    public AvatarLook findById(int characterId, int id) {
        String sql = "SELECT * FROM avatarLook WHERE id = ?";
        AvatarLook avatarLook = new AvatarLook();
        avatarLook.setCharacterId(characterId);
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                avatarLook.setId(rs.getInt("id"));
                avatarLook.setGender(rs.getInt("gender"));
                avatarLook.setSkin(rs.getInt("skin"));
                avatarLook.setFace(rs.getInt("face"));
                avatarLook.setHair(rs.getInt("hair"));
                avatarLook.setWeaponId(rs.getInt("weaponId"));
                avatarLook.setHairEquips(getEquips(avatarLook.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return avatarLook;
    }
}
