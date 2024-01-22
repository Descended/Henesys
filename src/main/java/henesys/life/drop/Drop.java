package henesys.life.drop;

import henesys.client.character.Char;
import henesys.enums.DropMotionType;
import henesys.enums.DropType;
import henesys.items.Equip;
import henesys.items.Item;
import henesys.items.container.ItemInfo;
import henesys.life.Life;
import henesys.loaders.ItemData;
import henesys.util.FileTime;

public class Drop extends Life {

    private Item item;
    private int money;
    private DropType dropType;
    private int ownerID;
    private boolean explosiveDrop;
    private boolean canBePickedUpByPet;
    private FileTime expireTime;
    private long mobExp;
    private DropMotionType dropMotionType;
    private boolean instanced;
    private int customQuestID = 0;  // Defaults to 0

    public Drop(int templateId) {
        super(templateId);
        dropMotionType = DropMotionType.Normal;
        canBePickedUpByPet = true;
    }

    public DropType getDropType() {
        return dropType;
    }

    public void setDropType(DropType dropType) {
        this.dropType = dropType;
    }

    public Drop(int templateId, Item item) {
        super(templateId);
        this.item = item;
        dropType = DropType.Item;
        dropMotionType = DropMotionType.Normal;
        expireTime = FileTime.fromType(FileTime.Type.ZERO_TIME);
    }

    public Drop(int templateId, int money) {
        super(templateId);
        this.money = money;
        dropType = DropType.Mesos;
        dropMotionType = DropMotionType.Normal;
        expireTime = FileTime.fromType(FileTime.Type.ZERO_TIME);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
        setDropType(DropType.Item);
        this.templateId = item.getItemId();
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        setDropType(DropType.Mesos);
    }

    public boolean isMoney() {
        return getDropType() == DropType.Mesos;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public boolean isExplosiveDrop() {
        return explosiveDrop;
    }

    public void setExplosiveDrop(boolean explosiveDrop) {
        this.explosiveDrop = explosiveDrop;
    }

    public FileTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(FileTime expireTime) {
        this.expireTime = expireTime;
    }

    public byte getItemGrade() {
        byte res = 0;
        if (getItem() != null && getItem() instanceof Equip) {
            res = (byte) ((Equip) getItem()).getGrade();
        }
        return res;
    }

    public void broadcastSpawnPacket(Char onlyChar) {
//        Item item = getItem();
//        ItemInfo ii = null;
//        if (item != null) {
//            ii = ItemData.getItemInfoByID(item.getItemId());
//        }
//        boolean canSpawn = isMoney()
//                || (ii != null && onlyChar.hasAnyQuestsInProgress(ii.getQuestIDs()))
//                || (item != null && (!isCustomQuestItem() || onlyChar.hasQuestInProgress(getCustomQuestID())) && (!isInstanced() || onlyChar.getId() == getOwnerID()));
//        if (canSpawn) {
//            onlyChar.getClient().write(DropPool.dropEnterField(this, getPosition(), getOwnerID(), canBePickedUpBy(onlyChar)));
//        }
    }

    public void setMobExp(long mobExp) {
        this.mobExp = mobExp;
    }

    public long getMobExp() {
        return mobExp;
    }

    public boolean canBePickedUpBy(Char chr) {
        int owner = getOwnerID();
        boolean allowedOwner = owner == chr.getId() ||
//                (chr.getParty() != null && chr.getParty().hasPartyMember(owner)) ||
                owner == 0;
        if (!allowedOwner) {
            return false;
        }
        if (isMoney()) {
            return chr.canAddMoney(getMoney());
        }
        return chr.canHold(item.getItemId(), item.getQuantity());
    }

    public boolean canBePickedUpByPet() {
        return canBePickedUpByPet;
    }

    public void setCanBePickedUpByPet(boolean canBePickedUpByPet) {
        this.canBePickedUpByPet = canBePickedUpByPet;
    }

    public DropMotionType getDropMotionType() {
        return dropMotionType;
    }

    public void setDropMotionType(DropMotionType dropMotionType) {
        this.dropMotionType = dropMotionType;
    }

    public boolean isInstanced() {
        return instanced;
    }

    public void setInstanced(boolean instanced) {
        this.instanced = instanced;
    }

    public boolean isCustomQuestItem() {
        return customQuestID != 0;
    }

    public int getCustomQuestID() {
        return customQuestID;
    }

    public void setCustomQuestID(int customQuestID) {
        this.customQuestID = customQuestID;
    }
}
