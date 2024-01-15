package henesys.connection.packet;

import henesys.connection.OutPacket;
import henesys.enums.InvType;
import henesys.enums.InventoryOperation;
import henesys.handlers.header.OutHeader;
import henesys.items.Equip;
import henesys.items.Item;

public class WvsContext {

    public static OutPacket exclRequest() {
//        return new OutPacket(OutHeader.EXCL_REQUEST);
        return null;
    }

    public static OutPacket inventoryOperation(boolean exclRequestSent, boolean notRemoveAddInfo, InventoryOperation type, short oldPos, short newPos,
                                               int bagPos, Item item) {
        // logic like this in packets :(
        InvType invType = item.getInvType();
        if ((oldPos > 0 && newPos < 0 && invType == InvType.EQUIPPED) || (invType == InvType.EQUIPPED && oldPos < 0)) {
            invType = InvType.EQUIP;
        }

        OutPacket outPacket = new OutPacket(OutHeader.INVENTORY_OPERATION);

        outPacket.encodeByte(exclRequestSent);
        outPacket.encodeByte(notRemoveAddInfo);

        outPacket.encodeByte(type.getVal());
        outPacket.encodeByte(invType.getVal());
        outPacket.encodeShort(oldPos);
        switch (type) {
            case Add:
            case BagNewItem:
                item.encode(outPacket);
                break;
            case UpdateQuantity:
                outPacket.encodeShort(item.getQuantity());
                break;
            case Move:
                outPacket.encodeShort(newPos);
                if (invType == InvType.EQUIP && (oldPos < 0 || newPos < 0)) {
                    outPacket.encodeByte(item.getCashItemSerialNumber() > 0);
                }
                break;
            case Remove:
                outPacket.encodeByte(0);
                break;
            case ItemExp:
                outPacket.encodeLong(((Equip) item).getExp());
                break;
            case UpdateBagPos:
                outPacket.encodeInt(bagPos);
                break;
            case UpdateBagQuantity:
                outPacket.encodeShort(newPos);
                break;
            case BagRemoveSlot:
                break;
        }
        return outPacket;
    }
}
