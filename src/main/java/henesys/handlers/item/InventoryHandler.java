package henesys.handlers.item;

import henesys.client.Client;
import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.packet.WvsContext;
import henesys.constants.GameConstants;
import henesys.constants.ItemConstants;
import henesys.enums.ChatType;
import henesys.enums.FieldOption;
import henesys.enums.InvType;
import henesys.enums.InventoryOperation;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;
import henesys.items.Inventory;
import henesys.items.Item;
import henesys.life.drop.Drop;
import henesys.loaders.ItemData;
import henesys.util.Position;
import henesys.world.field.Field;
import henesys.world.field.Foothold;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InventoryHandler {

    private static final Logger log = LogManager.getLogger(InventoryHandler.class);
    @Handler(op = InHeader.USER_CHANGE_SLOT_POSITION_REQUEST)
    public static void handleUserChangeSlotPositionRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // update tick
        InvType invType = InvType.getInvTypeByVal(inPacket.decodeByte());
        short oldPos = inPacket.decodeShort();
        short newPos = inPacket.decodeShort();
        short quantity = inPacket.decodeShort();
//        log.debug("Equipped old: {}", chr.getEquippedInventory());
//        log.debug("Equip old: {}", chr.getEquipInventory());
        InvType invTypeFrom = invType == InvType.EQUIP ? oldPos < 0 ? InvType.EQUIPPED : InvType.EQUIP : invType;
        InvType invTypeTo = invType == InvType.EQUIP ? newPos < 0 ? InvType.EQUIPPED : InvType.EQUIP : invType;
        Item item = chr.getInventoryByType(invTypeFrom).getItemBySlot(oldPos);
        if (item == null) {
            chr.dispose();
            return;
        }
        if (newPos == 0) { // Drop
            Field field = chr.getField();
            if ((field.getFieldLimit() & FieldOption.DropLimit.getVal()) > 0) {
                chr.dispose();
                return;
            }

            if (field.getDropsDisabled()) {
                chr.chatMessage(ChatType.AdminChat, "Drops are currently disabled in this map.");
                chr.dispose();
                return;
            }

            boolean fullDrop;
            Drop drop;
            if (!item.getInvType().isStackable() || quantity >= item.getQuantity() ||
                    ItemConstants.isThrowingStar(item.getItemId()) || ItemConstants.isBullet(item.getItemId())) {
                // Whole item is dropped (equip/stackable items with all their quantity)
                fullDrop = true;
                chr.getInventoryByType(invTypeFrom).removeItem(item);
                item.drop();
                drop = new Drop(-1, item);
            } else {
                // Part of the stack is dropped
                fullDrop = false;
                Item dropItem = ItemData.getItemDeepCopy(item.getItemId());
                dropItem.setQuantity(quantity);
                item.removeQuantity(quantity);
                drop = new Drop(-1, dropItem);
            }
            int x = chr.getPosition().getX();
            int y = chr.getPosition().getY();
            Foothold fh = chr.getField().findFootHoldBelow(new Position(x, y - GameConstants.DROP_HEIGHT));
//            chr.getField().drop(drop, chr.getPosition(), new Position(x, fh.getYFromX(x)));
            drop.setCanBePickedUpByPet(false);
            if (fullDrop) {
                c.write(WvsContext.inventoryOperation(true, false, InventoryOperation.Remove,
                        oldPos, newPos, 0, item));
            } else {
                c.write(WvsContext.inventoryOperation(true, false, InventoryOperation.UpdateQuantity,
                        oldPos, newPos, 0, item));
            }
        } else {
            Inventory inv = chr.getInventoryByType(invTypeTo);
            Item swapItem = inv.getItemBySlot(newPos);
            item.setBagIndex(newPos);
            int beforeSizeOn = chr.getEquippedInventory().getItems().size();
            int beforeSize = chr.getEquipInventory().getItems().size();
            if (invType == InvType.EQUIP && invTypeFrom != invTypeTo) {
                // We have to check if its full, because swapping items in EQUIP while its full causes data duplication
                if (chr.getEquipInventory().isFull() && swapItem != null) {
                    chr.chatMessage(ChatType.AdminChat, "Your inventory is full, cannot swap!");
                    chr.dispose();
                    return;
                }
                // TODO: verify job (see item.RequiredJob), level, stat, unique equip requirements
                // before we allow the player to equip this
                if (invTypeFrom == InvType.EQUIPPED) {
                    chr.unequip(item);
                } else {
                    if (swapItem != null) {
                        chr.unequip(swapItem);
                    }
                    chr.equip(item, newPos);
                }
            }
            if (swapItem != null) {
                int slotMax = 0;
                if (invType != InvType.EQUIP && invType != InvType.EQUIPPED) {
                    slotMax = ItemData.getItemInfoByID(swapItem.getItemId()).getSlotMax();
                }
                if (swapItem.getItemId() == item.getItemId() && swapItem.getQuantity() < slotMax && item.getQuantity() < slotMax
                        && swapItem.getDateExpire().isPermanent() && item.getDateExpire().isPermanent()
                        && invType != InvType.EQUIP && invType != InvType.EQUIPPED) {
                    int newQuantity = Math.min(swapItem.getQuantity() + item.getQuantity(), slotMax);
                    int deductAmount = Math.min(newQuantity - swapItem.getQuantity(), item.getQuantity());
                    if (item.getQuantity() == deductAmount) {
                        chr.getInventoryByType(invType).removeItem(item);
                        c.write(WvsContext.inventoryOperation(true, false, InventoryOperation.Remove, oldPos, newPos,
                                0, item));
                    } else {
                        item.removeQuantity(deductAmount);
                        item.setBagIndex(oldPos);
                        c.write(WvsContext.inventoryOperation(true, false,
                                InventoryOperation.UpdateQuantity, (short) item.getBagIndex(), (byte) -1,
                                0, item));
                    }
                    swapItem.setQuantity(newQuantity);
                    c.write(WvsContext.inventoryOperation(true, false,
                            InventoryOperation.UpdateQuantity, (short) swapItem.getBagIndex(), (byte) -1,
                            0, swapItem));
                    return;
                } else {
                    swapItem.setBagIndex(oldPos);
                }
//                log.debug("SwapItem after: {}", swapItem);
            }
            int afterSizeOn = chr.getEquippedInventory().getItems().size();
            int afterSize = chr.getEquipInventory().getItems().size();
            if (afterSize + afterSizeOn != beforeSize + beforeSizeOn) {
                throw new RuntimeException("Data duplication!");
            }
            c.write(WvsContext.inventoryOperation(true, false, InventoryOperation.Move, oldPos, newPos,
                    0, item));
            item.updateToChar(chr);
//            log.debug("Item before: {}", itemBefore);
//            log.debug("Item before: {}", item);
        }
//        log.debug("Equipped after: {}", chr.getEquippedInventory());
//        log.debug("Equip after: {}", chr.getEquipInventory());
        chr.setBulletIDForAttack(chr.calculateBulletIDForAttack(1));
    }
}
