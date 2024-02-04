package henesys.client.character;

import henesys.Server;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.character.avatar.AvatarLook;
import henesys.client.character.skills.temp.TemporaryStatManager;
import henesys.connection.OutPacket;
import henesys.connection.packet.*;
import henesys.constants.GameConstants;
import henesys.constants.ItemConstants;
import henesys.constants.SkillConstants;
import henesys.enums.*;
import henesys.items.BodyPart;
import henesys.items.Equip;
import henesys.items.Inventory;
import henesys.items.Item;
import henesys.items.container.ItemInfo;
import henesys.life.room.MiniRoom;
import henesys.loaders.ItemData;
import henesys.skills.Skill;
import henesys.util.FileTime;
import henesys.util.Position;
import henesys.world.Channel;
import henesys.world.World;
import henesys.world.field.Field;
import henesys.world.field.Portal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Inet4Address;
import java.util.*;
import java.util.function.Predicate;

import static henesys.enums.InventoryOperation.Add;
import static henesys.enums.InventoryOperation.UpdateQuantity;

public class Char {
    private static final Logger log = LogManager.getLogger(Char.class);

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

    private int maxFriends;

    private Inventory equippedInventory;
    private Inventory equipInventory;
    private Inventory consumeInventory;
    private Inventory etcInventory;
    private Inventory installInventory;
    private Inventory cashInventory;

    private Set<Skill> skills;
    private Map<Integer, Long> skillCoolTimes;
    private Field field;
    private TemporaryStatManager temporaryStatManager;
    private int bulletIDForAttack;
    private int completedSetItemID;
    private int portableChairID;
    private int activeEffectItemID;
    private Position position;
    private int tamingMobLevel;
    private int tamingMobExp;
    private int tamingMobFatigue;
    private MiniRoom miniRoom;
    private String ADBoardRemoteMsg;
    public Char() {
        temporaryStatManager = new TemporaryStatManager(this);
    }

    public Char(int id, CharacterStat cs, int accountId, AvatarLook avatarLook) {
        this.id = id;
        this.accountId = accountId;
        this.characterStat = cs;
        this.avatarLook = avatarLook;
        this.equippedInventory = new Inventory(InvType.EQUIPPED, 52, id);
        this.equipInventory = new Inventory(InvType.EQUIP, 52, id);
        this.consumeInventory = new Inventory(InvType.CONSUME, 52, id);
        this.etcInventory = new Inventory(InvType.ETC, 52, id);
        this.installInventory = new Inventory(InvType.INSTALL, 52, id);
        this.cashInventory = new Inventory(InvType.CASH, 52, id);
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
        log.info("Logging out {}", getCharacterStat().getName());
        getField().removeChar(this);
        getClient().getUser().setCurrentChr(null);
        getClient().getChannelInstance().removeChar(this);
        getClient().setChr(null);
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
            outPacket.encodeInt(getCharacterStat().getMoney());
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
            List<Item> equippedItems = getEquippedInventory().getItems();
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
        getClient().write(WvsContext.statChanged(Collections.emptyMap(), true));
    }

    /**
     * Heals this Char's HP for a certain amount. Caps off at maximum HP.
     *
     * @param amount The amount to heal.
     */
    public void heal(int amount, boolean whilstDeath) {
        CharacterStat cs = getCharacterStat();
        int curHP = cs.getHp();
        int maxHP = cs.getMaxHp();
        int newHP = Math.min(curHP + amount, maxHP);
        Map<Stat, Object> stats = new HashMap<>();

        if (whilstDeath || cs.getHp() > 0) {
            setStat(Stat.hp, newHP);
            stats.put(Stat.hp, newHP);
            getClient().write(WvsContext.statChanged(stats, false));
        }
    }

    /**
     * "Heals" this Char's MP for a certain amount. Caps off at maximum MP.
     *
     * @param amount The amount to heal.
     */
    public void healMP(int amount) {
        CharacterStat cs = getCharacterStat();
        int curMP = cs.getMp();
        int maxMP = cs.getMaxMp();
        int newMP = Math.min(curMP + amount, maxMP);
        Map<Stat, Object> stats = new HashMap<>();
        setStat(Stat.mp, newMP);
        stats.put(Stat.mp, newMP);
        getClient().write(WvsContext.statChanged(stats, false));
    }

    /**
     * Heals this Char's HP and MP for a certain amount.
     * @param hpAmount The amount to heal HP for.
     * @param mpAmount The amount to heal MP for.
     * @param whilstDeath Whether to heal even if the Char is dead.
     */
    public void healHpMp(int hpAmount, int mpAmount, boolean whilstDeath) {
        CharacterStat cs = getCharacterStat();
        int curHP = cs.getHp();
        int maxHP = cs.getMaxHp();
        int newHP = Math.min(curHP + hpAmount, maxHP);
        int curMP = cs.getMp();
        int maxMP = cs.getMaxMp();
        int newMP = Math.min(curMP + mpAmount, maxMP);
        Map<Stat, Object> stats = new HashMap<>();
        if (whilstDeath || cs.getHp() > 0) {
            setStat(Stat.hp, newHP);
            stats.put(Stat.hp, newHP);
            setStat(Stat.mp, newMP);
            stats.put(Stat.mp, newMP);
        }
        getClient().write(WvsContext.statChanged(stats, false));
    }

    /**
     * Unequips an {@link Item}. Ensures that the hairEquips and both inventories get updated.
     *
     * @param item The Item to equip.
     */
    public void unequip(Item item) {
        Inventory inv = getEquippedInventory();
        AvatarLook al = getAvatarLook();
        CharacterStat cs = getCharacterStat();
        int itemID = item.getItemId();
        getInventoryByType(InvType.EQUIPPED).removeItem(item);
        item.setBagIndex(getInventoryByType(InvType.EQUIP).getFirstOpenSlot());
        getInventoryByType(InvType.EQUIP).addItem(item);
        boolean isCash = item.isCash();
        int pos = item.getBagIndex();
        Equip overrideItem;
        // get the corresponding cash item
        if (isCash) {
            overrideItem = (Equip) inv.getItemBySlot(Math.abs(pos) - 100);
        } else {
            overrideItem = (Equip) inv.getItemBySlot(Math.abs(pos) + 100);
        }
        int overrideItemId = overrideItem == null ? -1 : overrideItem.getItemId();
        al.removeItem(itemID, overrideItemId, isCash);
        byte maskValue = AvatarModifiedMask.AvatarLook.getVal();
        getField().broadcastPacket(UserRemote.avatarModified(this, maskValue, (byte) 0), this);
        int maxHp = cs.getMaxHp();
        int maxMp = cs.getMaxMp();
        if (cs.getHp() > maxHp) {
            heal(maxHp, false);
        }
        if (cs.getMp() > maxMp) {
            healMP(maxMp);
        }
    }

    /**
     * Equips an {@link Item}. Ensures that the hairEquips and both inventories get updated.
     *
     * @param item The Item to equip.
     */
    public boolean equip(Item item, int newPos) {
        return false;
    }

    /**
     * Adds a Stat to this Char.
     *
     * @param charStat which Stat to add
     * @param amount   the amount of Stat to add
     */
    public void addStat(Stat charStat, int amount) {
        setStat(charStat, getStat(charStat) + amount);
    }

    /**
     * Gets a raw Stat from this Char, unaffected by things such as equips and skills.
     *
     * @param charStat The requested Stat
     * @return the requested stat's value
     */
    public int getStat(Stat charStat) {
        CharacterStat cs = getCharacterStat();
        switch (charStat) {
            case str:
                return cs.getStr();
            case dex:
                return cs.getDex();
            case inte:
                return cs.getIntt();
            case luk:
                return cs.getLuk();
            case hp:
                return cs.getHp();
            case mhp:
                return cs.getMaxHp();
            case mp:
                return cs.getMp();
            case mmp:
                return cs.getMaxMp();
            case ap:
                return cs.getAp();
            case level:
                return cs.getLevel();
            case skin:
                return cs.getSkin();
            case face:
                return cs.getFace();
            case hair:
                return cs.getHair();
            case pop:
                return cs.getFame();
            case subJob:
                return cs.getSubJob();
        }
        return -1;
    }
    public void setStat(Stat charStat, int amount) {
        CharacterStat cs = getCharacterStat();
        switch (charStat) {
            case str:
                cs.setStr((short) amount);
                break;
            case dex:
                cs.setDex((short) amount);
                break;
            case inte:
                cs.setInt((short) amount);
                break;
            case luk:
                cs.setLuk((short) amount);
                break;
            case hp:
                cs.setHp(amount);
                break;
            case mhp:
                cs.setMaxHp(amount);
                break;
            case mp:
                cs.setMp(amount);
                break;
            case mmp:
                cs.setMaxMp(amount);
                break;
            case ap:
                cs.setAp((short) amount);
                break;
            case level:
                cs.setLevel((byte) amount);
                break;
            case skin:
                cs.setSkin((byte) amount);
                break;
            case face:
                cs.setFace(amount);
                break;
            case hair:
                cs.setHair(amount);
                break;
            case pop:
                cs.setFame((short) amount);
                break;
        }
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

    public void addItemToInventory(InvType type, Item item, boolean hasCorrectBagIndex, boolean excelReq) {
        Inventory inventory = getInventoryByType(type);
        ItemInfo ii = ItemData.getItemInfoByID(item.getItemId());
        int quantity = item.getQuantity();
        if (inventory != null) {
            Item existingItem = inventory.getItemByItemIDAndStackable(item.getItemId());
            boolean stackable = true;
            if (existingItem != null) {
                boolean expire = existingItem.getDateExpire().equals(item.getDateExpire());
                if (existingItem.getDateExpire().isPermanent() && item.getDateExpire().isPermanent()) {
                    expire = true;
                }
                stackable = existingItem.getInvType().isStackable() && expire;
            }
            boolean rec = false;
            if (existingItem != null && stackable && !ItemConstants.isRechargable(item.getItemId())) {
                if (existingItem.getQuantity() < ii.getSlotMax()) {
                    if (quantity + existingItem.getQuantity() > ii.getSlotMax()) {
                        quantity = ii.getSlotMax() - existingItem.getQuantity();
                        item.setQuantity(item.getQuantity() - quantity);
                        rec = true;
                    }
                    existingItem.addQuantity(quantity);
                    getClient().write(WvsContext.inventoryOperation(excelReq, false,
                            UpdateQuantity, (short) existingItem.getBagIndex(), (byte) -1, 0, existingItem));
                    Item copy = item.deepCopy();
                    copy.setQuantity(quantity);
                    if (rec) {
                        addItemToInventory(item.getInvType(), item, false, true);
                    }
                }
            } else {
                if (!hasCorrectBagIndex) {
                    item.setBagIndex(inventory.getFirstOpenSlot());
                }
                Item itemCopy = null;
                if (item.getInvType().isStackable() && ii != null && item.getQuantity() > ii.getSlotMax()) {
                    itemCopy = item.deepCopy();
                    quantity = quantity - ii.getSlotMax();
                    itemCopy.setQuantity(quantity);
                    item.setQuantity(ii.getSlotMax());
                    rec = true;
                }
                inventory.addItem(item);
                getClient().write(WvsContext.inventoryOperation(excelReq, false,
                        Add, (short) item.getBagIndex(), (byte) -1, 0, item));
                if (rec) {
                    addItemToInventory(item.getInvType(), itemCopy, false, excelReq);
                }
            }
            setBulletIDForAttack(calculateBulletIDForAttack(1));
        }
    }

    public int calculateBulletIDForAttack(int requiredAmount) {
        Item weapon = getEquippedInventory().getFirstItemByBodyPart(BodyPart.Weapon);
        if (weapon == null) {
            return 0;
        }
        Predicate<Item> kindOfBulletPred;
        int id = weapon.getItemId();

        if (ItemConstants.isClaw(id)) {
            kindOfBulletPred = i -> ItemConstants.isThrowingStar(i.getItemId());
        } else if (ItemConstants.isBow(id)) {
            kindOfBulletPred = i -> ItemConstants.isBowArrow(i.getItemId());
        } else if (ItemConstants.isXBow(id)) {
            kindOfBulletPred = i -> ItemConstants.isXBowArrow(i.getItemId());
        } else if (ItemConstants.isGun(id)) {
            kindOfBulletPred = i -> ItemConstants.isBullet(i.getItemId());
        } else {
            return 0;
        }
        Item i = getConsumeInventory().getItems().stream().sorted(Comparator.comparing(Item::getBagIndex)).filter(kindOfBulletPred).filter(item -> item.getQuantity() >= requiredAmount).findFirst().orElse(null);
        return i != null ? i.getItemId() : 0;
    }

    public Inventory getInventoryByType(InvType invType) {
        switch (invType) {
            case EQUIPPED:
                return getEquippedInventory();
            case EQUIP:
                return getEquipInventory();
            case CONSUME:
                return getConsumeInventory();
            case ETC:
                return getEtcInventory();
            case INSTALL:
                return getInstallInventory();
            case CASH:
                return getCashInventory();
            default:
                return null;
        }
    }

    public void changeChannel(byte channelId) {
        logout();
        setChangingChannel(true);
        getField().removeChar(this);;
        int worldID = getClient().getWorldId();
        World world = Server.getInstance().getWorldById(worldID);

        Channel channel = world.getChannelById(channelId);
        channel.addClientInTransfer(channelId, getId(), getClient());
        getClient().write(ClientSocket.migrateCommand(true, Inet4Address.getLoopbackAddress().getAddress(), channel.getPort()));
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
    public boolean canAddMoney(long reqMoney) {
        return getMoney() + reqMoney > 0 && getMoney() + reqMoney < GameConstants.MAX_MONEY;
    }

    public long getMoney() {
        return getCharacterStat().getMoney();
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

    public int getBulletIDForAttack() {
        return bulletIDForAttack;
    }

    public void setBulletIDForAttack(int bulletIDForAttack) {
        this.bulletIDForAttack = bulletIDForAttack;
    }

    private boolean canHold(List<Item> items) {
        // explicitly use a Char param to avoid accidentally adding items
        if (items.isEmpty()) {
            return true;
        }
        Item item = items.getFirst();
        if (canHold(item.getItemId())) {
            Inventory inv = getInventoryByType(item.getInvType());
            inv.addItem(item);
            items.remove(item);
            return canHold(items);
        } else {
            return false;
        }

    }
    /**
     * Checks if this Char can hold an Item in their inventory, assuming that its quantity is 1.
     *
     * @param id the item's itemID
     * @return whether or not this Char can hold an item in their inventory
     */
    public boolean canHold(int id) {
        boolean canHold;
        if (ItemConstants.isEquip(id)) {  //Equip
            canHold = getEquipInventory().getSlots() > getEquipInventory().getItems().size();
        } else {    //Item
            ItemInfo ii = ItemData.getItemInfoByID(id);
            Inventory inv = getInventoryByType(ii.getInvType());
            Item curItem = inv.getItemByItemID(id);
            canHold = (curItem != null && curItem.getQuantity() + 1 < ii.getSlotMax()) || inv.getSlots() > inv.getItems().size();
        }
        return canHold;
    }

    public boolean canHold(int id, int quantity) {
        int slotMax = 1;
        ItemInfo ii = ItemData.getItemInfoByID(id);
        if (ii != null) {
            slotMax = ii.getSlotMax();
        }
        List<Item> items = new ArrayList<>();
        for (int i = quantity; i > 0; i -= slotMax) {
            Item item = ItemData.getItemDeepCopy(id);
            item.setQuantity(Math.min(i, slotMax));
            items.add(item);
        }
        return canHold(items);
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        getCharacterStat().setFieldId(field.getId());
        this.field = field;
    }

    /**
     * Warps this character to a given field, at a given portal.
     * Ensures that the previous map does not contain this Char anymore, and that the new field
     * does.
     * Ensures that all Lifes are immediately spawned for the new player.
     *
     * @param toField The {@link Field} to warp to.
     */
    public void warp(Field toField, Portal portal, boolean characterData, boolean saveReturnMap) {
        if (toField == null) {
            return;
        }
        Field currentField = getField();

        if (currentField != null) {
            if (saveReturnMap) {
//                setPreviousFieldID(currentField.getId()); // this may be a bad idea in some cases? idk
//                setNearestReturnPortal();
            }
            currentField.removeChar(this);
        }

        setField(toField);
        toField.addChar(this);
//        getCharacterStat().setPortal(portal.getId());
        setPosition(new Position(portal.getX(), portal.getY()));
        getClient().write(Stage.setField(this, toField, portal.getId(), getClient().getChannel(), getClient().getWorldId(), characterData));
        toField.spawnLifesForChar(this);
    }

    public Field getOrCreateFieldByCurrentInstanceType(int fieldID) {
        Field res;
        res = getClient().getChannelInstance().getField(fieldID);
        return res;
    }

    public TemporaryStatManager getTemporaryStatManager() {
        return temporaryStatManager;
    }

    public void setTemporaryStatManager(TemporaryStatManager temporaryStatManager) {
        this.temporaryStatManager = temporaryStatManager;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getCompletedSetItemID() {
        return completedSetItemID;
    }

    public void setCompletedSetItemID(int completedSetItemID) {
        this.completedSetItemID = completedSetItemID;
    }

    public int getPortableChairID() {
        return portableChairID;
    }

    public void setPortableChairID(int portableChairID) {
        this.portableChairID = portableChairID;
    }

    public int getActiveEffectItemID() {
        return activeEffectItemID;
    }

    public void setActiveEffectItemID(int activeEffectItemID) {
        this.activeEffectItemID = activeEffectItemID;
    }

    public int getTamingMobLevel() {
        return tamingMobLevel;
    }

    public void setTamingMobLevel(int tamingMobLevel) {
        this.tamingMobLevel = tamingMobLevel;
    }

    public int getTamingMobExp() {
        return tamingMobExp;
    }

    public void setTamingMobExp(int tamingMobExp) {
        this.tamingMobExp = tamingMobExp;
    }

    public int getTamingMobFatigue() {
        return tamingMobFatigue;
    }

    public void setTamingMobFatigue(int tamingMobFatigue) {
        this.tamingMobFatigue = tamingMobFatigue;
    }

    public MiniRoom getMiniRoom() {
        return miniRoom;
    }

    public void setMiniRoom(MiniRoom miniRoom) {
        this.miniRoom = miniRoom;
    }

    public String getADBoardRemoteMsg() {
        return ADBoardRemoteMsg;
    }

    public void setADBoardRemoteMsg(String ADBoardRemoteMsg) {
        this.ADBoardRemoteMsg = ADBoardRemoteMsg;
    }

    public boolean hasFriendshipItem() {
        return false;
    }

    /**
     * Returns the Equip equipped at a certain {@link BodyPart}.
     *
     * @param bodyPart The requested bodyPart.
     * @return The Equip corresponding to <code>bodyPart</code>. Null if there is none.
     */
    public Item getEquippedItemByBodyPart(BodyPart bodyPart) {
        List<Item> items = getEquippedInventory().getItemsByBodyPart(bodyPart);
        return !items.isEmpty() ? items.getFirst() : null;
    }

}
