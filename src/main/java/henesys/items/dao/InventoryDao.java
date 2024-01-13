package henesys.items.dao;

import henesys.connection.db.DatabaseManager;
import henesys.enums.InvType;
import henesys.items.Inventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDao {

    public List<Inventory> getInventoriesByCharacterId(int characterId) {
        String sql = "SELECT * FROM inventory WHERE charactersId = ?";
        ArrayList<Inventory> inventories = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, characterId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setId(rs.getInt("id"));
                inventory.setType(InvType.getInvTypeByVal(rs.getInt("type")));
                inventory.setSlots((byte) rs.getInt("slots"));
                inventory.setCharacterId(rs.getInt("charactersId"));
                inventories.add(inventory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inventories;
    }

    public int createInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (type, slots, charactersId) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, inventory.getType().getVal());
            statement.setInt(2, inventory.getSlots());
            statement.setInt(3, inventory.getCharacterId());
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

    public void updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET slots = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, inventory.getSlots());
            statement.setInt(2, inventory.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
