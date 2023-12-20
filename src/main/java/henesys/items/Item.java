package henesys.items;

import henesys.client.character.Char;
import henesys.connection.Encodable;
import henesys.connection.OutPacket;
import henesys.constants.ItemConstants;
import henesys.enums.InvType;
import henesys.loaders.ItemData;
import henesys.util.FileTime;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * GW_ItemSlotBase
 * Created on 18/12/2023
 */
public class Item implements Serializable, Encodable {

    private long id;
    protected int itemId;
    protected int bagIndex;
    protected long cashItemSerialNumber;
    protected FileTime dateExpire = FileTime.fromType(FileTime.Type.MAX_TIME);
    protected InvType invType;
    protected Type type;
    protected boolean isCash;
    protected int quantity;
    private String owner = "";

    public boolean isTradable() {
        return !ItemData.getItemInfoByID(getItemId()).isTradeBlock();
    }

    public enum Type {
        EQUIP(1),
        ITEM(2),
        PET(3);

        private final byte val;

        Type(byte val) {
            this.val = val;
        }

        Type(int val) {
            this((byte) val);
        }

        public byte getVal() {
            return val;
        }

        public static Type getTypeById(int id) {
            return Arrays.stream(Type.values()).filter(type -> type.getVal() == id).findFirst().orElse(null);
        }
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void drop() {
        setBagIndex(0);
    }

    public void addQuantity(int amount) {
        if (amount > 0 && amount + getQuantity() > 0) {
            setQuantity(getQuantity() + amount);
        }
    }

    public void removeQuantity(int amount) {
        if (amount > 0) {
            setQuantity(Math.max(0, getQuantity() - amount));
        }
    }

    public Item() { }

    public Item(int itemId, int bagIndex, long cashItemSerialNumber, FileTime dateExpire, InvType invType,
                boolean isCash, Type type) {
        this.itemId = itemId;
        this.bagIndex = bagIndex;
        this.cashItemSerialNumber = cashItemSerialNumber;
        this.dateExpire = dateExpire;
        this.invType = invType;
        this.isCash = isCash;
        this.type = type;
    }

    public Item deepCopy() {
        Item item = new Item();
        item.setItemId(getItemId());
        item.setBagIndex(getBagIndex());
        item.setCashItemSerialNumber(getCashItemSerialNumber());
        item.setDateExpire(getDateExpire().deepCopy());
        item.setInvType(getInvType());
        item.setCash(isCash());
        item.setType(getType());
        item.setOwner(getOwner());
        item.setQuantity(getQuantity());
        return item;
    }

    public int getItemId() {
        return itemId;
    }

    public int getBagIndex() {
        return bagIndex;
    }

    public void setBagIndex(int bagIndex) {
        this.bagIndex = Math.abs(bagIndex);
    }

    public long getCashItemSerialNumber() {
        return getId();
    }

    public FileTime getDateExpire() {
        return dateExpire;
    }

    public InvType getInvType() {
        return invType;
    }

    public Type getType() {
        return type;
    }

    public boolean isCash() {
        return isCash || getInvType() == InvType.CASH;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getType().getVal());
        // GW_ItemSlotBase
        outPacket.encodeInt(getItemId());
        outPacket.encodeByte(isCash());
        if (isCash()) {
            outPacket.encodeLong(getId());
        }
        outPacket.encodeFT(getDateExpire());
        outPacket.encodeInt(getBagIndex());
        if (getType() == Type.ITEM) {
            outPacket.encodeShort(getQuantity()); // nQuantity
            outPacket.encodeString(getOwner()); // sOwner
            outPacket.encodeShort(0); // flag
            if (ItemConstants.isThrowingStar(getItemId()) || ItemConstants.isBullet(getItemId())) {
                outPacket.encodeLong(getId());
            }
        }
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setCashItemSerialNumber(long cashItemSerialNumber) {
        this.cashItemSerialNumber = cashItemSerialNumber;
    }

    public void setDateExpire(FileTime dateExpire) {
        this.dateExpire = dateExpire;
    }

    public void setInvType(InvType invType) {
        this.invType = invType;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + ", ItemId: " + getItemId() + ", Qty: " + getQuantity() + ", InvType: " + getInvType()
                + ", BagIndex: " + getBagIndex();
    }

    /**
     * Sends a packet to the given Char to show that this Item has updated.
     * @param chr The Char to give the update to
     */
    public void updateToChar(Char chr) {
//        short bagIndex = (short) (getInvType() == InvType.EQUIPPED ? - getBagIndex() : getBagIndex());
//        chr.write(WvsContext.inventoryOperation(true, false, InventoryOperation.Add,
//                bagIndex, (short) 0, 0, this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && item.id == item.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId);
    }
}
