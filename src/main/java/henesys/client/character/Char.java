package henesys.client.character;

import henesys.client.Account;
import henesys.client.Client;
import henesys.client.character.avatar.AvatarLook;
import henesys.connection.OutPacket;
import henesys.connection.packet.UserLocal;
import henesys.constants.SkillConstants;
import henesys.enums.ChatType;
import henesys.enums.DBChar;
import henesys.enums.InvType;
import henesys.items.BodyPart;
import henesys.items.Equip;
import henesys.items.Inventory;
import henesys.items.Item;
import henesys.skills.Skill;
import henesys.util.FileTime;

import java.util.*;

public class Char {

    private int id;
    private int accountId;
    private boolean changingChannel;

    private int characterStatId;
    private int avatarLookId;

    private boolean inCashShop;
    private AvatarLook avatarLook;
    private CharacterStat characterStat;

    private boolean talkingToNpc;
    private int combatOrders;
    private Client client;

    private int meso;
    private int maxFriends;

    private Inventory equippedInventory = new Inventory(InvType.EQUIPPED, 52);
    private Inventory equipInventory = new Inventory(InvType.EQUIP, 52);
    private Inventory consumeInventory = new Inventory(InvType.CONSUME, 52);
    private Inventory etcInventory = new Inventory(InvType.ETC, 52);
    private Inventory installInventory = new Inventory(InvType.INSTALL, 52);
    private Inventory cashInventory = new Inventory(InvType.CASH, 52);

    private Set<Skill> skills;
    private Map<Integer, Long> skillCoolTimes;

    public Char() {
    }

    public Char(int id, CharacterStat cs, int accountId, AvatarLook avatarLook) {
        this.id = id;
        this.accountId = accountId;
        this.characterStat = cs;
        this.avatarLook = avatarLook;
    }

    public int getId() {
        return id;
    }

    public boolean isChangingChannel() {
        return changingChannel;
    }

    public void setChangingChannel(boolean changingChannel) {
        this.changingChannel = changingChannel;
    }

    public boolean isInCashShop() {
        return inCashShop;
    }

    public void setInCashShop(boolean inCashShop) {
        this.inCashShop = inCashShop;
    }

    public AvatarLook getAvatarLook() {
        return avatarLook;
    }

    public void setAvatarLook(AvatarLook avatarLook) {
        this.avatarLook = avatarLook;
    }

    public CharacterStat getCharacterStat() {
        return characterStat;
    }

    public void setCharacterStat(CharacterStat characterStat) {
        this.characterStat = characterStat;
    }

    public void logout() {
        // TODO: implement
    }

    /**
     * Encodes this Char's info inside a given {@link OutPacket}, with given info.
     *
     * @param outPacket The OutPacket this method should encode to.
     * @param mask      Which info needs to be encoded.
     */
    public void encode(OutPacket outPacket, DBChar mask) {
        outPacket.encodeLong(mask.get());
        outPacket.encodeByte(getCombatOrders());
        outPacket.encodeByte(0); // Some loop

        if (mask.isInMask(DBChar.Character)) {
            getCharacterStat().encode(outPacket);
            outPacket.encodeByte(getMaxFriends()); // nFriendMax

            boolean linkedChar = false;
            outPacket.encodeByte(linkedChar); // linked character
            if (linkedChar) {
                outPacket.encodeString("Desc"); // linked character name?
            }
        }

        if (mask.isInMask(DBChar.Money)) {
            outPacket.encodeInt(getMeso());
        }

        if (mask.isInMask(DBChar.InventorySize)) {
            outPacket.encodeByte(getEquipInventory().getSlots());
            outPacket.encodeByte(getConsumeInventory().getSlots());
            outPacket.encodeByte(getInstallInventory().getSlots()); // setup inventory
            outPacket.encodeByte(getEtcInventory().getSlots()); // etc inventory
            outPacket.encodeByte(getCashInventory().getSlots());
        }

        if (mask.isInMask(DBChar.AdminShopCount)) {
            outPacket.encodeInt(0); // ???
            outPacket.encodeInt(0);
        }

        if (mask.isInMask(DBChar.ItemSlotEquip)) {
            List<Item> equippedItems = new ArrayList<>(getEquippedInventory().getItems());
            equippedItems.sort(Comparator.comparingInt(Item::getBagIndex));
            // Normal equipped items
            for (Item item : equippedItems) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() > BodyPart.BPBase.getVal() && item.getBagIndex() < BodyPart.BPEnd.getVal()) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);

            // Cash equipped items
            for (Item item : getEquippedInventory().getItems()) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() >= BodyPart.CBPBase.getVal() && item.getBagIndex() <= BodyPart.CBPEnd.getVal()) {
                    outPacket.encodeShort(equip.getBagIndex() - 100);
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);

            // Equip inventory
            for (Item item : getEquipInventory().getItems()) {
                Equip equip = (Equip) item;
                outPacket.encodeShort(equip.getBagIndex());
                equip.encode(outPacket);
            }

            outPacket.encodeShort(0);

            // NonBPEquip::Decode (Evan)
            for (Item item : getEquippedInventory().getItems()) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() >= BodyPart.EvanBase.getVal() && item.getBagIndex() < BodyPart.EvanEnd.getVal()) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);
            // Mech
            // >= 20k < 200024?
            for (Item item : getEquippedInventory().getItems()) {
                Equip equip = (Equip) item;
                if (item.getBagIndex() >= BodyPart.MechBase.getVal() && item.getBagIndex() < BodyPart.MechEnd.getVal()) {
                    outPacket.encodeShort(equip.getBagIndex());
                    equip.encode(outPacket);
                }
            }
            outPacket.encodeShort(0);
        }

        if (mask.isInMask(DBChar.ItemSlotConsume)) {
            for (Item item : getConsumeInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ItemSlotInstall)) {
            for (Item item : getInstallInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ItemSlotEtc)) {
            for (Item item : getEtcInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }
        if (mask.isInMask(DBChar.ItemSlotCash)) {
            for (Item item : getCashInventory().getItems()) {
                outPacket.encodeByte(item.getBagIndex());
                item.encode(outPacket);
            }
            outPacket.encodeByte(0);
        }

        if (mask.isInMask(DBChar.SkillRecord)) {
//                Set<Skill> skillRecord = getSkills();
            Set<Skill> skillRecord = new HashSet<>();
            outPacket.encodeShort(skillRecord.size());
            for (Skill skill : skillRecord) {
                outPacket.encodeInt(skill.getSkillId());
                outPacket.encodeInt(skill.getCurrentLevel());
                outPacket.encodeFT(FileTime.fromType(FileTime.Type.MAX_TIME));
                if (SkillConstants.isSkillNeedMasterLevel(skill.getSkillId())) {
                    outPacket.encodeInt(skill.getMasterLevel());
                }
            }
        }
        if (mask.isInMask(DBChar.SkillCooltime)) {
            long curTime = System.currentTimeMillis();
            Map<Integer, Long> cooltimes = new HashMap<>();
//            getSkillCoolTimes().forEach((key, value) -> {
//                if (value - curTime > 0) {
//                    cooltimes.put(key, value);
//                }
//            });
            outPacket.encodeShort(cooltimes.size());
            for (Map.Entry<Integer, Long> cooltime : cooltimes.entrySet()) {
                outPacket.encodeInt(cooltime.getKey()); // nSkillId
                outPacket.encodeShort((int) ((cooltime.getValue() - curTime) / 1000)); // nSkillCooltime
            }
        }

        if (mask.isInMask(DBChar.QuestRecord)) {
//                short size = (short) getQuestManager().getQuestsInProgress().size();
            short size = 0;
            outPacket.encodeShort(size);
//                for (Quest quest : getQuestManager().getQuestsInProgress()) {
//                    outPacket.encodeShort(quest.getQRKey());
//                    outPacket.encodeString(quest.getQRValue());
//                }
        }
        if (mask.isInMask(DBChar.QuestComplete)) {
//                Set<Quest> completedQuests = getQuestManager().getCompletedQuests();
            outPacket.encodeShort(0);
//                outPacket.encodeShort(completedQuests.size());
//                for (Quest quest : completedQuests) {
//                    outPacket.encodeInt(quest.getQRKey());
//                    outPacket.encodeFT(0); // Timestamp of completion
//                }
        }

        if (mask.isInMask(DBChar.MinigameRecord)) {
            int size = 0;
            outPacket.encodeShort(size);
//                for (int i = 0; i < size; i++) {
//                    new MiniGameRecord().encode(outPacket);
        }

        if (mask.isInMask(DBChar.CoupleRecord)) {
            int coupleSize = 0;
            outPacket.encodeShort(coupleSize);
//            for (int i = 0; i < coupleSize; i++) {
//                new CoupleRecord().encode(outPacket);
//            }
            int friendSize = 0;
            outPacket.encodeShort(friendSize);
//            for (int i = 0; i < friendSize; i++) {
//                new FriendRecord().encode(outPacket);
//            }
            int marriageSize = 0;
//            if (getMarriageRecord() != null) {
//                marriageSize = 1;
//            }
            outPacket.encodeShort(marriageSize);
//            for (int i = 0; i < marriageSize; i++) {
//                getMarriageRecord().encode(outPacket);
//            }
        }

        if (mask.isInMask(DBChar.MapTransfer)) {
            for (int i = 0; i < 5; i++) {
                outPacket.encodeInt(0);
            }
            for (int i = 0; i < 10; i++) {
                outPacket.encodeInt(0);
            }
        }

        if (mask.isInMask(DBChar.NewYearCard)) {
            outPacket.encodeShort(0);
        }

        if (mask.isInMask(DBChar.QuestRecordEx)) {
//            outPacket.encodeShort(getQuestManager().getEx().size());
            outPacket.encodeShort(0);
//            for (Quest quest : getQuestManager().getEx()) {
//                outPacket.encodeShort(quest.getQRKey());
//                outPacket.encodeString(quest.getQRValue());
//            }
        }

        if (mask.isInMask(DBChar.WildHunterInfo)) {
//            if (JobConstants.isWildHunter(getAvatarData().getCharacterStat().getJob())) {
//                getWildHunterInfo().encode(outPacket); // GW_WildHunterInfo::Decode
//            }
        }

        if (mask.isInMask(DBChar.QuestCompleteOld)) {
            outPacket.encodeShort(0);
        }

        if (mask.isInMask(DBChar.VisitorLog)) {
            outPacket.encodeShort(0);
        }

    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    /**
     * Disposes this Char, allowing it to send packets to the server again.
     */
    public void dispose() {
        setTalkingToNpc(false);
//        getClient().write(WvsContext.exclRequest());
    }

    public boolean isTalkingToNpc() {
        return talkingToNpc;
    }

    public void setTalkingToNpc(boolean talkingToNpc) {
        this.talkingToNpc = talkingToNpc;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Consumes {@code quantity} of an item with the specified {@code id} from this Char's {@link Inventory}.
     * Will remove the Item if it has a quantity of 1.
     *
     * @param id       The Item's id.
     * @param quantity The amount to consume.
     */

    public void consumeItem(int id, int quantity) {
//        Item checkItem = ItemData.getItemDeepCopy(id);
//        if (checkItem != null) {
//            for (Item item : getInventoryByType(checkItem.getInvType()).getItemsByItemID(id)) {
//                if (quantity <= 0) break;
//                int stackSize = item.getQuantity();
//                consumeItem(item, quantity);
//                quantity -= stackSize;
//            }
//        }
    }

    /**
     * Consumes {@code quantity} of the given {@link Item} from this Char's {@link Inventory}. Will remove the Item
     * if it has a quantity of 1.
     *
     * @param item     The Item to consume
     * @param quantity The amount of items to consume
     */
    public void consumeItem(Item item, int quantity) {
//        item.setQuantity(Math.max(0, item.getQuantity() - quantity));
//        // data race possible
//        if (item.getQuantity() <= 0 && !ItemConstants.isThrowingItem(item.getItemId())) {
//            Inventory inventory = getInventoryByType(item.getInvType());
//            inventory.removeItem(item);
//            short bagIndex = (short) item.getBagIndex();
//            if (inventory.getType() == InvType.EQUIPPED) {
//                boolean isCash = item.isCash();
//                int pos = item.getBagIndex();
//                // get corresponding cash item based on bag index
//                Equip overrideItem = (Equip) inventory.getItemBySlot((isCash) ? pos - 100 : pos + 100);
//                if (overrideItem == null) {
//                    getAvatarData().getAvatarLook().removeItem(item.getItemId(), -1, isCash);
//                } else {
//                    getAvatarData().getAvatarLook().removeItem(item.getItemId(), overrideItem.getItemId(), isCash);
//                }
//                bagIndex = (short) -bagIndex;
//            }
//            write(WvsContext.inventoryOperation(true, false,
//                    Remove, bagIndex, (byte) 0, 0, item));
//        } else {
//            write(WvsContext.inventoryOperation(true, false,
//                    UpdateQuantity, (short) item.getBagIndex(), (byte) -1, 0, item));
//        }
//        setBulletIDForAttack(calculateBulletIDForAttack(1));
    }

    /**
     * Sends a message to this Char with a given {@link ChatType colour}.
     *
     * @param clr The Colour this message should be in.
     * @param msg The message to display.
     */
    public void chatMessage(ChatType clr, String msg) {
        getClient().write(UserLocal.chatMsg(clr, msg));
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCharacterStatId() {
        return characterStatId;
    }

    public void setCharacterStatId(int characterStatId) {
        this.characterStatId = characterStatId;
    }

    public int getAvatarLookId() {
        return avatarLookId;
    }

    public void setAvatarLookId(int avatarLookId) {
        this.avatarLookId = avatarLookId;
    }

    public int getCombatOrders() {
        return combatOrders;
    }

    public void setCombatOrders(int combatOrders) {
        this.combatOrders = combatOrders;
    }

    public int getMaxFriends() {
        return maxFriends;
    }

    public void setMaxFriends(int maxFriends) {
        this.maxFriends = maxFriends;
    }

    public int getMeso() {
        return meso;
    }

    public void setMeso(int meso) {
        this.meso = meso;
    }

    public Inventory getEquipInventory() {
        return equipInventory;
    }

    public void setEquipInventory(Inventory equipInventory) {
        this.equipInventory = equipInventory;
    }

    public Inventory getConsumeInventory() {
        return consumeInventory;
    }

    public void setConsumeInventory(Inventory consumeInventory) {
        this.consumeInventory = consumeInventory;
    }

    public Inventory getEtcInventory() {
        return etcInventory;
    }

    public void setEtcInventory(Inventory etcInventory) {
        this.etcInventory = etcInventory;
    }

    public Inventory getInstallInventory() {
        return installInventory;
    }

    public void setInstallInventory(Inventory installInventory) {
        this.installInventory = installInventory;
    }

    public Inventory getCashInventory() {
        return cashInventory;
    }

    public void setCashInventory(Inventory cashInventory) {
        this.cashInventory = cashInventory;
    }

    public Inventory getEquippedInventory() {
        return equippedInventory;
    }

    public void setEquippedInventory(Inventory equippedInventory) {
        this.equippedInventory = equippedInventory;
    }

    public Map<Integer, Long> getSkillCoolTimes() {
        return skillCoolTimes;
    }

    public void setSkillCoolTimes(Map<Integer, Long> skillCoolTimes) {
        this.skillCoolTimes = skillCoolTimes;
    }
}
