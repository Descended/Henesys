package henesys.items.dao;

import henesys.client.Trunk;
import henesys.connection.db.DatabaseManager;
import henesys.constants.ItemConstants;
import henesys.enums.InvType;
import henesys.items.Equip;
import henesys.items.Inventory;
import henesys.items.Item;
import henesys.util.FileTime;

import java.sql.*;
import java.util.ArrayList;
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
        inventory.setItems(new ArrayList<>());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, inventory.getId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (inventory.getType() == InvType.EQUIP || inventory.getType() == InvType.EQUIPPED) {
                    Equip equip = new Equip();
                    equip.setId(rs.getLong("id"));
                    equip.setItemId(rs.getInt("itemId"));
                    equip.setBagIndex(rs.getInt("bagIndex"));
                    equip.setDateExpire(FileTime.fromTimestamp(rs.getTimestamp("dateExpire")));
                    equip.setQuantity(rs.getInt("quantity"));
                    equip.setInventoryId(rs.getInt("inventoryId"));

                    // Fetch additional stats from equip table using equip.getId() and set these stats to equip object
                    String equipSql = "SELECT * FROM equip WHERE itemId = ?";
                    try (PreparedStatement equipStatement = conn.prepareStatement(equipSql)) {
                        equipStatement.setLong(1, equip.getId());
                        ResultSet equipRs = equipStatement.executeQuery();
                        if (equipRs.next()) {
                            equip.setTitle(equipRs.getString("title"));
                            equip.setTuc(equipRs.getShort("tuc"));
                            equip.setCuc(equipRs.getShort("cuc"));
                            equip.setiStr(equipRs.getShort("iStr"));
                            equip.setiDex(equipRs.getShort("iDex"));
                            equip.setiInt(equipRs.getShort("iInt"));
                            equip.setiLuk(equipRs.getShort("iLuk"));
                            equip.setiMaxHp(equipRs.getShort("iMaxHp"));
                            equip.setiMaxMp(equipRs.getShort("iMaxMp"));
                            equip.setiPad(equipRs.getShort("iPad"));
                            equip.setiMad(equipRs.getShort("iMad"));
                            equip.setiPDD(equipRs.getShort("iPDD"));
                            equip.setiMDD(equipRs.getShort("iMDD"));
                            equip.setiAcc(equipRs.getShort("iAcc"));
                            equip.setiEva(equipRs.getShort("iEva"));
                            equip.setiCraft(equipRs.getShort("iCraft"));
                            equip.setiSpeed(equipRs.getShort("iSpeed"));
                            equip.setiJump(equipRs.getShort("iJump"));
                            equip.setLevel(equipRs.getShort("level"));
                            equip.setExp(equipRs.getShort("exp"));
                            equip.setDurability(equipRs.getShort("durability"));
                            equip.setOptions(equipRs.getString("options"));
                            equip.setSockets(equipRs.getString("sockets"));
                        }
                    }
                    inventory.getItems().add(equip);
                } else {
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
        String equipSqlInsert = "INSERT INTO equip (itemId, title, tuc, cuc, iStr, iDex, iInt, iLuk, iMaxHp, iMaxMp, iPad, " +
                "iMad, iPDD, iMDD, iAcc, iEva, iCraft, iSpeed, iJump, level, exp, durability, options, sockets) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sqlInsert);
             PreparedStatement equipStatement = conn.prepareStatement(equipSqlInsert);
             PreparedStatement updateStatement = conn.prepareStatement(sqlUpdate)) {
            for (Item item : inventory.getItems()) {
                boolean exists = checkItemExists(item.getId(), conn);

                if (!exists) { // Insert new item
                    insertStatement.setLong(1, item.getId());
                    insertStatement.setInt(2, item.getItemId());
                    insertStatement.setInt(3, item.getQuantity());
                    insertStatement.setInt(4, item.getBagIndex());
                    insertStatement.setInt(5, item.getTrunkId()); // TODO: Save trunk items
                    insertStatement.setInt(6, item.getInventoryId());
                    Timestamp dateExpire = item.getDateExpire().toTimestamp();
                    if (dateExpire.after(Timestamp.valueOf("9999-12-31 23:59:59"))) {
                        dateExpire = Timestamp.valueOf("9999-12-31 23:59:59");
                    } else if (dateExpire.before(Timestamp.valueOf("1000-01-01 00:00:00"))) {
                        dateExpire = Timestamp.valueOf("1000-01-01 00:00:00");
                    }
                    insertStatement.setTimestamp(7, dateExpire);
                    insertStatement.addBatch();
                    if(item.getInvType() == InvType.EQUIP) {
                        Equip equip = (Equip) item;
                        try (equipStatement) {
                            equipStatement.setInt(1, equip.getItemId());
                            equipStatement.setString(2, equip.getTitle());
                            equipStatement.setShort(3, equip.getTuc());
                            equipStatement.setShort(4, equip.getCuc());
                            equipStatement.setShort(5, equip.getiStr());
                            equipStatement.setShort(6, equip.getiDex());
                            equipStatement.setShort(7, equip.getiInt());
                            equipStatement.setShort(8, equip.getiLuk());
                            equipStatement.setShort(9, equip.getiMaxHp());
                            equipStatement.setShort(10, equip.getiMaxMp());
                            equipStatement.setShort(11, equip.getiPad());
                            equipStatement.setShort(12, equip.getiMad());
                            equipStatement.setShort(13, equip.getiPDD());
                            equipStatement.setShort(14, equip.getiMDD());
                            equipStatement.setShort(15, equip.getiAcc());
                            equipStatement.setShort(16, equip.getiEva());
                            equipStatement.setShort(17, equip.getiCraft());
                            equipStatement.setShort(18, equip.getiSpeed());
                            equipStatement.setShort(19, equip.getiJump());
                            equipStatement.setShort(20, equip.getLevel());
                            equipStatement.setShort(21, equip.getExp());
                            equipStatement.setShort(22, equip.getDurability());
                            equipStatement.setString(23, equip.getOptions().toString());
                            equipStatement.setString(24, equip.getSockets().toString());
                            equipStatement.addBatch();
                        }
                    }
                } else { // Update existing item
                    updateStatement.setInt(1, item.getQuantity());
                    // if equip, update equip table todo
                    updateStatement.addBatch();
                }
            }

            insertStatement.executeBatch();
            updateStatement.executeBatch();
            equipStatement.executeBatch();
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
