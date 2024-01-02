package henesys.items.dao;

import henesys.client.Trunk;
import henesys.connection.db.DatabaseManager;
import henesys.enums.InvType;
import henesys.items.Inventory;
import henesys.items.Item;
import henesys.util.FileTime;

import java.sql.*;
import java.util.List;

public class ItemDao {

    public long getHighestItemId() {
        String sql = "SELECT MAX(id) FROM item";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public List<Item> getItemsByInventory(Inventory inventory) {
        String sql = "SELECT * FROM item WHERE inventoryId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, inventory.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getLong("id"));
                item.setItemId(rs.getInt("itemId"));
                item.setBagIndex(rs.getInt("bagIndex"));
                item.setCashItemSerialNumber(rs.getLong("cashItemSerialNumber"));
                item.setDateExpire(FileTime.fromEpochMillis(rs.getLong("dateExpire")));
                item.setInvType(InvType.getInvTypeByVal(rs.getInt("invType")));
                item.setType(Item.Type.getTypeById(rs.getInt("type")));
                item.setCash(rs.getBoolean("isCash"));
                item.setQuantity(rs.getInt("quantity"));
                item.setOwner(rs.getString("owner"));
                item.setInventoryId(rs.getInt("inventoryId"));
                inventory.getItems().add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return inventory.getItems();
    }

    public void saveItems(Inventory inventory) {
        // TODO: Save trunk items
        String sqlInsert = "INSERT INTO item (id, itemId, quantity, bagIndex, trunkId, inventoryId, dateExpire) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE item SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement updateStatement = conn.prepareStatement(sqlUpdate)) {
            for (Item item : inventory.getItems()) {
                boolean exists = checkItemExists(item.getId(), conn);

                if (!exists) { // Insert new item
                    insertStatement.setLong(1, item.getId());
                    insertStatement.setInt(2, item.getItemId());
                    insertStatement.setInt(3, item.getQuantity());
                    insertStatement.setInt(4, item.getBagIndex());
                    insertStatement.setInt(5, item.getTrunkId());
                    insertStatement.setInt(6, item.getInventoryId());
                    insertStatement.setTimestamp(7, Timestamp.valueOf(item.getDateExpire().toLocalDateTime()));
                    insertStatement.addBatch();
                } else { // Update existing item
                    updateStatement.setInt(1, item.getQuantity());
                    updateStatement.addBatch();
                }
            }

            insertStatement.executeBatch();
            updateStatement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkItemExists(long id, Connection conn) throws SQLException {
        String sqlCheck = "SELECT 1 FROM item WHERE id = ?";
        try (PreparedStatement checkStatement = conn.prepareStatement(sqlCheck)) {
            checkStatement.setLong(1, id);
            ResultSet rs = checkStatement.executeQuery();
            return rs.next(); // True if item exists, false otherwise
        }
    }
}
