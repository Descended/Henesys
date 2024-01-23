package henesys.items;

import henesys.client.character.Char;
import henesys.connection.OutPacket;
import henesys.constants.ItemConstants;
import henesys.enums.*;
import henesys.loaders.ItemData;
import henesys.util.FileTime;
import henesys.util.Util;

import java.util.*;

import static henesys.enums.BaseStat.AllStat;

/**
 * Created on 11/23/2017.
 */
public class Equip extends Item {
    private String title = "";
    private FileTime equippedDate = FileTime.fromType(FileTime.Type.PLAIN_ZERO);
    private int prevBonusExpRate;
    private short tuc;
    private short cuc;
    private short iStr;
    private short iDex;
    private short iInt;
    private short iLuk;
    private short iMaxHp;
    private short iMaxMp;
    private short iPad;
    private short iMad;
    private short iPDD;
    private short iMDD;
    private short iAcc;
    private short iEva;
    private short iCraft;
    private short iSpeed;
    private short iJump;
    private short attribute;
    private short levelUpType;
    private short level;
    private short exp;
    private short durability = -1;
    private short iuc;
    private short iPvpDamage;
    private byte iReduceReq;
    private short specialAttribute;
    private short durabilityMax;
    private short iIncReq;
    private short growthEnchant;
    private short psEnchant;
    private short imdr;
    private boolean bossReward;
    private short damR;
    private short exGradeOption;
    private short itemState;
    private short chuc;
    private short soulOptionId;
    private short soulSocketId;
    private short soulOption;
    private short rStr;
    private short rDex;
    private short rInt;
    private short rLuk;
    private short rLevel;
    private short rJob;
    private short rPop;
    private List<Integer> options = new ArrayList<>(); // base + add pot + anvil
    private int specialGrade;
    private boolean tradeBlock;

    private boolean only;
    private boolean notSale;
    private int attackSpeed;
    private int price;
    private boolean expireOnLogout;
    private int setItemID;
    private boolean exItem;
    private boolean equipTradeBlock;
    private String iSlot = "";
    private String vSlot = "";
    private int fixedGrade;
    private int dropStreak = 0;
    private short iucMax = ItemConstants.MAX_HAMMER_SLOTS;
    private boolean hasIUCMax = false;

    private List<ItemSkill> itemSkills = new ArrayList<>();

    private List<Integer> sockets = new ArrayList<>();

    public Equip() {
        super();
    }

    public Equip deepCopy() {
        Equip ret = new Equip();
        ret.quantity = quantity;
        ret.bagIndex = bagIndex;
        ret.title = title;
        ret.equippedDate = equippedDate.deepCopy();
        ret.prevBonusExpRate = prevBonusExpRate;
        ret.tuc = tuc;
        ret.iucMax = iucMax;
        ret.hasIUCMax = hasIUCMax;
        ret.cuc = cuc;
        ret.iStr = iStr;
        ret.iDex = iDex;
        ret.iInt = iInt;
        ret.iLuk = iLuk;
        ret.iMaxHp = iMaxHp;
        ret.iMaxMp = iMaxMp;
        ret.iPad = iPad;
        ret.iMad = iMad;
        ret.iPDD = iPDD;
        ret.iMDD = iMDD;
        ret.iAcc = iAcc;
        ret.iEva = iEva;
        ret.iCraft = iCraft;
        ret.iSpeed = iSpeed;
        ret.iJump = iJump;
        ret.attribute = attribute;
        ret.levelUpType = levelUpType;
        ret.level = level;
        ret.exp = exp;
        ret.durability = durability;
        ret.iuc = iuc;
        ret.iPvpDamage = iPvpDamage;
        ret.iReduceReq = iReduceReq;
        ret.specialAttribute = specialAttribute;
        ret.durabilityMax = durabilityMax;
        ret.iIncReq = iIncReq;
        ret.growthEnchant = growthEnchant;
        ret.psEnchant = psEnchant;
        ret.imdr = imdr;
        ret.bossReward = bossReward;
        ret.damR = damR;
        ret.exGradeOption = exGradeOption;
        ret.itemState = itemState;
        ret.chuc = chuc;
        ret.soulOptionId = soulOptionId;
        ret.soulSocketId = soulSocketId;
        ret.soulOption = soulOption;
        ret.rStr = rStr;
        ret.rDex = rDex;
        ret.rInt = rInt;
        ret.rLuk = rLuk;
        ret.rLevel = rLevel;
        ret.rJob = rJob;
        ret.rPop = rPop;
        ret.iSlot = iSlot;
        ret.vSlot = vSlot;
        ret.fixedGrade = fixedGrade;
        ret.options = new ArrayList<>();
        ret.options.addAll(options);
        ret.specialGrade = specialGrade;
        ret.tradeBlock = tradeBlock;
        ret.only = only;
        ret.notSale = notSale;
        ret.attackSpeed = attackSpeed;
        ret.price = price;
        ret.expireOnLogout = expireOnLogout;
        ret.setItemID = setItemID;
        ret.exItem = exItem;
        ret.equipTradeBlock = equipTradeBlock;
        ret.setOwner(getOwner());
        ret.itemId = itemId;
        ret.cashItemSerialNumber = cashItemSerialNumber;
        ret.dateExpire = dateExpire.deepCopy();
        ret.invType = invType;
        ret.type = type;
        ret.isCash = isCash;
        ret.dropStreak = dropStreak;
        return ret;
    }

    public long getSerialNumber() {
        return getId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileTime getEquippedDate() {
        return equippedDate;
    }

    public int getPrevBonusExpRate() {
        return prevBonusExpRate;
    }

    // scroll slots
    public short getTuc() {
        return tuc;
    }

    public short getIUCMax() {
        return hasIUCMax ? iucMax : ItemConstants.MAX_HAMMER_SLOTS;
    }

    public boolean isHasIUCMax() {
        return hasIUCMax;
    }

    public short getCuc() {
        return cuc;
    }

    public void setCuc(short cuc) {
        this.cuc = cuc;
    }

    public short getiStr() {
        return iStr;
    }

    public void setiStr(short iStr) {
        this.iStr = iStr;
    }

    public short getiDex() {
        return iDex;
    }

    public void setiDex(short iDex) {
        this.iDex = iDex;
    }

    public short getiInt() {
        return iInt;
    }

    public void setiInt(short iInt) {
        this.iInt = iInt;
    }

    public short getiLuk() {
        return iLuk;
    }

    public void setiLuk(short iLuk) {
        this.iLuk = iLuk;
    }

    public short getiMaxHp() {
        return iMaxHp;
    }

    public void setiMaxHp(short iMaxHp) {
        this.iMaxHp = iMaxHp;
    }

    public short getiMaxMp() {
        return iMaxMp;
    }

    public void setiMaxMp(short iMaxMp) {
        this.iMaxMp = iMaxMp;
    }

    public short getiPad() {
        return iPad;
    }

    public void setiPad(short iPad) {
        this.iPad = iPad;
    }

    public short getiMad() {
        return iMad;
    }

    public void setiMad(short iMad) {
        this.iMad = iMad;
    }

    public short getiPDD() {
        return iPDD;
    }

    public void setiPDD(short iPDD) {
        this.iPDD = iPDD;
    }

    public short getiMDD() {
        return iMDD;
    }

    public void setiMDD(short iMDD) {
        this.iMDD = iMDD;
    }

    public short getiAcc() {
        return iAcc;
    }

    public void setiAcc(short iAcc) {
        this.iAcc = iAcc;
    }

    public short getiEva() {
        return iEva;
    }

    public void setiEva(short iEva) {
        this.iEva = iEva;
    }

    public short getiCraft() {
        return iCraft;
    }

    public void setiCraft(short iCraft) {
        this.iCraft = iCraft;
    }

    public short getiSpeed() {
        return iSpeed;
    }

    public void setiSpeed(short iSpeed) {
        this.iSpeed = iSpeed;
    }

    public short getiJump() {
        return iJump;
    }

    public void setiJump(short iJump) {
        this.iJump = iJump;
    }

    public short getAttribute() {
        return attribute;
    }

    public void setAttribute(short attribute) {
        this.attribute = attribute;
    }

    public void addAttribute(EquipAttribute ea) {
        short attr = getAttribute();
        attr |= (short) ea.getVal();
        setAttribute(attr);
    }

    public short getLevelUpType() {
        return levelUpType;
    }

    public void setLevelUpType(short levelUpType) {
        this.levelUpType = levelUpType;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getExp() {
        return exp;
    }

    public void setExp(short exp) {
        this.exp = exp;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public short getIuc() {
        return iuc;
    }

    public void setIuc(short iuc) {
        this.iuc = iuc;
    }

    public short getiPvpDamage() {
        return iPvpDamage;
    }

    public void setiPvpDamage(short iPvpDamage) {
        this.iPvpDamage = iPvpDamage;
    }

    public byte getiReduceReq() {
        return iReduceReq;
    }

    public void setiReduceReq(short iReduceReq) {
        this.iReduceReq = (byte) iReduceReq;
    }

    public short getSpecialAttribute() {
        return specialAttribute;
    }

    public void setSpecialAttribute(short specialAttribute) {
        this.specialAttribute = specialAttribute;
    }


    public short getExGradeOption() {
        return exGradeOption;
    }

    public void setExGradeOption(short exGradeOption) {
        this.exGradeOption = exGradeOption;
    }
    public short getDamR() {
        return damR;
    }

    public void setDamR(short damR) {
        this.damR = damR;
    }

    public short getImdr() {
        return imdr;
    }

    public void setImdr(short imdr) {
        this.imdr = imdr;
    }

    public boolean isBossReward() {
        return bossReward;
    }

    public void setBossReward(boolean bossReward) {
        this.bossReward = bossReward;
    }
    public short getPsEnchant() {
        return psEnchant;
    }

    public void setPsEnchant(short psEnchant) {
        this.psEnchant = psEnchant;
    }

    public short getGrowthEnchant() {
        return growthEnchant;
    }

    public void setGrowthEnchant(short growthEnchant) {
        this.growthEnchant = growthEnchant;
    }

    public short getiIncReq() {
        return iIncReq;
    }

    public void setiIncReq(short iIncReq) {
        this.iIncReq = iIncReq;
    }

    public short getDurabilityMax() {
        return durabilityMax;
    }

    public void setDurabilityMax(short durabilityMax) {
        this.durabilityMax = durabilityMax;
    }

    public short getItemState() {
        return itemState;
    }

    public void setItemState(short itemState) {
        this.itemState = itemState;
    }
    public short getGrade() {
        ItemGrade bonusGrade = ItemGrade.getGradeByVal(getBonusGrade());
        if (bonusGrade.isHidden()) {
            return ItemGrade.getHiddenBonusGradeByBaseGrade(ItemGrade.getGradeByVal(getBaseGrade())).getVal();
        }
        return getBaseGrade();
    }

    public short getBaseGrade() {
        return ItemGrade.getGradeByOption(getOptionBase(0)).getVal();
    }

    public short getBonusGrade() {
        return ItemGrade.getGradeByOption(getOptionBonus(0)).getVal();
    }


    public short getChuc() {
        return chuc;
    }

    public void setChuc(short chuc) {
        this.chuc = chuc;
    }

    public short getSoulOptionId() {
        return soulOptionId;
    }

    public void setSoulOptionId(short soulOptionId) {
        this.soulOptionId = soulOptionId;
    }

    public short getSoulSocketId() {
        return soulSocketId;
    }

    public void setSoulSocketId(short soulSocketId) {
        this.soulSocketId = soulSocketId;
    }

    public short getSoulOption() {
        return soulOption;
    }

    public void setSoulOption(short soulOption) {
        this.soulOption = soulOption;
    }

    public short getrPop() {
        return rPop;
    }

    public void setrPop(short rPop) {
        this.rPop = rPop;
    }

    public short getrJob() {
        return rJob;
    }

    public void setrJob(short rJob) {
        this.rJob = rJob;
    }

    public short getrLevel() {
        return rLevel;
    }

    public void setrLevel(short rLevel) {
        this.rLevel = rLevel;
    }

    public short getrLuk() {
        return rLuk;
    }

    public void setrLuk(short rLuk) {
        this.rLuk = rLuk;
    }

    public short getrInt() {
        return rInt;
    }

    public void setrInt(short rInt) {
        this.rInt = rInt;
    }

    public short getrDex() {
        return rDex;
    }

    public void setrDex(short rDex) {
        this.rDex = rDex;
    }

    public short getrStr() {
        return rStr;
    }

    public void setrStr(short rStr) {
        this.rStr = rStr;
    }

    public List<Integer> getOptions() {
        return options;
    }

    public void setOptions(List<Integer> options) {
        this.options = options;
    }

    public void setOptions(String options) {
        // 0,0,0,0,0 TODO
        this.options = new ArrayList<>();
    }

    public String getiSlot() {
        return iSlot;
    }

    public void setiSlot(String iSlot) {
        this.iSlot = iSlot;
    }

    public String getvSlot() {
        return vSlot;
    }

    public void setvSlot(String vSlot) {
        this.vSlot = vSlot;
    }

    public int getSpecialGrade() {
        return specialGrade;
    }

    public boolean isTradeBlock() {
        return tradeBlock;
    }

    public boolean isOnly() {
        return only;
    }

    public boolean isNotSale() {
        return notSale;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public int getPrice() {
        return price;
    }


    public boolean isExpireOnLogout() {
        return expireOnLogout;
    }

    public int getSetItemID() {
        return setItemID;
    }

    public int getFixedGrade() {
        return fixedGrade;
    }

    public boolean isExItem() {
        return exItem;
    }

    public boolean isEquipTradeBlock() {
        return equipTradeBlock;
    }

    public void setEquippedDate(FileTime equippedDate) {
        this.equippedDate = equippedDate;
    }

    public void setPrevBonusExpRate(int prevBonusExpRate) {
        this.prevBonusExpRate = prevBonusExpRate;
    }

    public void setTuc(short tuc) {
        this.tuc = tuc;
    }

    public void setHasIUCMax(boolean hasIUCMax) {
        this.hasIUCMax = hasIUCMax;
    }

    public void setIUCMax(short iucMax) {
        this.iucMax = iucMax;
    }

    public void setSpecialGrade(int specialGrade) {
        this.specialGrade = specialGrade;
    }

    public void setTradeBlock(boolean tradeBlock) {
        this.tradeBlock = tradeBlock;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public void setNotSale(boolean notSale) {
        this.notSale = notSale;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setExpireOnLogout(boolean expireOnLogout) {
        this.expireOnLogout = expireOnLogout;
    }

    public void setSetItemID(int setItemID) {
        this.setItemID = setItemID;
    }

    public void setExItem(boolean exItem) {
        this.exItem = exItem;
    }

    public void setEquipTradeBlock(boolean equipTradeBlock) {
        this.equipTradeBlock = equipTradeBlock;
    }

    public void setFixedGrade(int fixedGrade) {
        this.fixedGrade = fixedGrade;
    }

    public int getDropStreak() {
        return dropStreak;
    }

    public void setDropStreak(int dropStreak) {
        this.dropStreak = dropStreak;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(1);
        // GW_ItemSlotBase
        super.encode(outPacket);
        // GW_ItemSlotEquip
//        outPacket.encodeLong(getSerialNumber());
//        outPacket.encodeString(getTitle(), 13);
//        getEquippedDate().encode(outPacket);
//        outPacket.encodeInt(getPrevBonusExpRate());
        // GW_ItemSlotEquipBase
        outPacket.encodeByte(getTuc());
        outPacket.encodeByte(getCuc());
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iStr));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iDex));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iInt));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iLuk));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iMaxHP));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iMaxMP));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iPAD));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iMAD));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iPDD));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iMDD));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iACC));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iEVA));
        outPacket.encodeShort(getiCraft());
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iSpeed));
        outPacket.encodeShort(getTotalStat(EquipBaseStat.iJump));
        outPacket.encodeString(getTitle());
        outPacket.encodeShort(getAttribute());
        outPacket.encodeByte(getLevelUpType());
        outPacket.encodeByte(getLevel());
        outPacket.encodeInt(getExp());
        outPacket.encodeInt(getDurability());
        outPacket.encodeInt(getIuc()); // hammer
        // GW_ItemSlotEquipOpt
        outPacket.encodeByte(0); // getGrade()
        outPacket.encodeByte(getChuc());

        for (int i = 0; i < 3; i++) {
//            outPacket.encodeShort(getOptionBase(i));
            outPacket.encodeShort(0);
        }

        for (int i = 0; i < 2; i++) {
            // sockets 0 through 2 (-1 = none, 0 = empty, >0 = filled
            outPacket.encodeShort(getSocket(i));
        }
        if (!isCash()) {
            outPacket.encodeLong(getId());
        }

        outPacket.encodeLong(0);
        outPacket.encodeInt(0);

    }

    public int getSocket(int num) {
        return num < getSockets().size() ? getSockets().get(num) : 0;
    }

    public void setSocket(int num, int value) {
        while (num >= getSockets().size()) {
            getSockets().add(0);
        }
        getSockets().set(num, value);
    }

    public int getTotalStat(EquipBaseStat stat) {
        switch (stat) {
            case tuc:
                return getTuc();
            case cuc:
                return getCuc();
            case iStr:
                return getiStr() + getSocketStat(ScrollStat.incSTR);
            case iDex:
                return getiDex() + getSocketStat(ScrollStat.incDEX);
            case iInt:
                return getiInt() + getSocketStat(ScrollStat.incINT);
            case iLuk:
                return getiLuk() + getSocketStat(ScrollStat.incLUK);
            case iMaxHP:
                return getiMaxHp() + getSocketStat(ScrollStat.incMHP);
            case iMaxMP:
                return getiMaxMp() + getSocketStat(ScrollStat.incMMP);
            case iPAD:
                return getiPad() + getSocketStat(ScrollStat.incPAD);
            case iMAD:
                return getiMad() + getSocketStat(ScrollStat.incMAD);
            case iPDD:
                return getiPDD() + getSocketStat(ScrollStat.incPDD);
            case iMDD:
                return getiMDD() + getSocketStat(ScrollStat.incMDD);
            case iACC:
                return getiAcc() + getSocketStat(ScrollStat.incACC);
            case iEVA:
                return getiEva() + getSocketStat(ScrollStat.incEVA);
            case iCraft:
                return getiCraft();
            case iSpeed:
                return getiSpeed() + getSocketStat(ScrollStat.incSpeed);
            case iJump:
                return getiJump() + getSocketStat(ScrollStat.incJump);
            case attribute:
                return getAttribute();
            case levelUpType:
                return getLevelUpType();
            case level:
                return getLevel();
            case exp:
                return getExp();
            case durability:
                return getDurability();
            case iuc:
                return getIuc(); // hammer
            case iPvpDamage:
                return getiPvpDamage();
            case iReduceReq:
                return getiReduceReq();
            case specialAttribute:
                return getSpecialAttribute();
            case durabilityMax:
                return getDurabilityMax();
            case iIncReq:
                return getiIncReq();
            case growthEnchant:
                return getGrowthEnchant(); // ygg
            case psEnchant:
                return getPsEnchant(); // final strike
            case imdr:
                return getImdr() + getSocketStat(ScrollStat.ignoreTargetDEF); // ied
            case damR:
                return getDamR() + getSocketStat(ScrollStat.incDAMr); // td
            case exGradeOption:
                return getExGradeOption();
        }
        return 0;
    }

    private boolean hasStat(EquipBaseStat ebs) {
        return getBaseStat(ebs) != 0;
    }

    private int getStatMask(int pos) {
        int mask = 0;
        for (EquipBaseStat ebs : EquipBaseStat.values()) {
            if (hasStat(ebs) && ebs.getPos() == pos) {
                mask |= ebs.getVal();
            }
        }
        return mask;
    }

    public void setBaseStat(EquipBaseStat equipBaseStat, long amount) {
        switch (equipBaseStat) {
            case tuc:
                setTuc((short) amount);
                break;
            case cuc:
                setCuc((short) amount);
                break;
            case iStr:
                setiStr((short) amount);
                break;
            case iDex:
                setiDex((short) amount);
                break;
            case iInt:
                setiInt((short) amount);
                break;
            case iLuk:
                setiLuk((short) amount);
                break;
            case iMaxHP:
                setiMaxHp((short) amount);
                break;
            case iMaxMP:
                setiMaxMp((short) amount);
                break;
            case iPAD:
                setiPad((short) amount);
                break;
            case iMAD:
                setiMad((short) amount);
                break;
            case iPDD:
                setiPDD((short) amount);
                break;
            case iMDD:
                setiMDD((short) amount);
                break;
            case iACC:
                setiAcc((short) amount);
                break;
            case iEVA:
                setiEva((short) amount);
                break;
            case iCraft:
                setiCraft((short) amount);
                break;
            case iSpeed:
                setiSpeed((short) amount);
                break;
            case iJump:
                setiJump((short) amount);
                break;
            case attribute:
                setAttribute((short) amount);
                break;
            case levelUpType:
                setLevelUpType((short) amount);
                break;
            case level:
                setLevel((short) amount);
                break;
            case exp:
                setExp((short) amount);
                break;
            case durability:
                setDurability((short) amount);
                break;
            case iuc:
                setIuc((short) amount);
                break;
            case iPvpDamage:
                setiPvpDamage((short) amount);
                break;
            case iReduceReq:
                setiReduceReq((byte) amount);
                break;
            case specialAttribute:
                setSpecialAttribute((short) amount);
                break;
            case durabilityMax:
                setDurabilityMax((short) amount);
                break;
            case iIncReq:
                setiIncReq((short) amount);
                break;
            case growthEnchant:
                setGrowthEnchant((short) amount);
                break;
            case psEnchant:
                setPsEnchant((short) amount);
                break;
            case imdr:
                setImdr((short) amount);
                break;
            case damR:
                setDamR((short) amount);
                break;
            case exGradeOption:
                setExGradeOption((short) amount);
                break;
        }
    }

    public long getBaseStat(EquipBaseStat equipBaseStat) {
        switch (equipBaseStat) {
            case tuc:
                return getTuc();
            case cuc:
                return getCuc();
            case iStr:
                return getiStr();
            case iDex:
                return getiDex();
            case iInt:
                return getiInt();
            case iLuk:
                return getiLuk();
            case iMaxHP:
                return getiMaxHp();
            case iMaxMP:
                return getiMaxMp();
            case iPAD:
                return getiPad();
            case iMAD:
                return getiMad();
            case iPDD:
                return getiPDD();
            case iMDD:
                return getiMDD();
            case iACC:
                return getiAcc();
            case iEVA:
                return getiEva();
            case iCraft:
                return getiCraft();
            case iSpeed:
                return getiSpeed();
            case iJump:
                return getiJump();
            case attribute:
                return getAttribute();
            case levelUpType:
                return getLevelUpType();
            case level:
                return getLevel();
            case exp:
                return getExp();
            case durability:
                return getDurability();
            case iuc:
                return getIuc();
            case iPvpDamage:
                return getiPvpDamage();
            case iReduceReq:
                return getiReduceReq();
            case specialAttribute:
                return getSpecialAttribute();
            case durabilityMax:
                return getDurabilityMax();
            case iIncReq:
                return getiIncReq();
            case growthEnchant:
                return getGrowthEnchant();
            case psEnchant:
                return getPsEnchant();
            case imdr:
                return getImdr();
            case damR:
                return getDamR();
            case exGradeOption:
                return getExGradeOption();
            default:
                return 0;
        }
    }


    public void addStat(EquipBaseStat stat, int amount) {
        int cur = (int) getBaseStat(stat);
        int newStat = cur + amount >= 0 ? cur + amount : 0; // stat cannot be negative
        setBaseStat(stat, newStat);
    }

    public boolean hasAttribute(EquipAttribute equipAttribute) {
        return (getAttribute() & equipAttribute.getVal()) != 0;
    }

    public void removeAttribute(EquipAttribute equipAttribute) {
        if (!hasAttribute(equipAttribute)) {
            return;
        }
        short attr = getAttribute();
        attr ^= equipAttribute.getVal();
        setAttribute(attr);
    }


    public int[] getOptionBase() {
        return new int[]{getOptions().get(0), getOptions().get(1), getOptions().get(2)};
    }

    public int getOptionBase(int num) {
        return getOptions().get(num);
    }

    public int setOptionBase(int num, int val) {
        return getOptions().set(num, val);
    }

    public int[] getOptionBonus() {
        return new int[]{getOptions().get(3), getOptions().get(4), getOptions().get(5)};
    }

    public int getOption(int num, boolean bonus) {
        return bonus ? getOptionBonus(num) : getOptionBase(num);
    }

    public int getOptionBonus(int num) {
        return getOptions().get(num + 3);
    }

    public void setOptionBonus(int num, int val) {
        getOptions().set(num + 3, val);
    }

    public void setOption(int num, int val, boolean bonus) {
        if (bonus) {
            setOptionBonus(num, val);
        } else {
            setOptionBase(num, val);
        }
    }

    public int getRandomOption(boolean bonus, int line, int cubeId, int additionalPrimes) {
//        List<Integer> data = ItemConstants.getWeightedOptionsByEquip(this, bonus, line, cubeId, additionalPrimes);
//        return data.get(Util.getRandom(data.size() - 1));
        return 0;
    }

    // required level for players to equip this
    public int getRequiredLevel() {
        // the highest of them as negative values won't work as intended
        return Math.max(0, getrLevel() + getiIncReq() - (getiReduceReq()));
    }

    /**
     * Resets the potential of this equip's base options. Takes the value of an ItemGrade (1-4), and sets the appropriate values.
     * Also calculates if a third line should be added.
     *
     * @param val             The value of the item's grade (HiddenRare~HiddenLegendary).getVal().
     * @param thirdLineChance The chance of a third line being added.
     */
    public void setHiddenOptionBase(short val, int thirdLineChance) {
        if (!ItemConstants.canEquipHavePotential(this)) {
            return;
        }

        int max = 3;
        if (getOptionBase(2) == 0) {
            // If this equip did not have a 3rd line already, thirdLineChance to get it
            if (Util.succeedProp(100 - thirdLineChance)) {
                max = 2;
            }
        }
        for (int i = 0; i < max; i++) {
            setOptionBase(i, -val);
        }
    }

    public void setHiddenOptionBonus(short val, int thirdLineChance) {
        if (!ItemConstants.canEquipHavePotential(this)) {
            return;
        }

        int max = 3;
        if (getOptionBonus(2) == 0) {
            // If this equip did not have a 3rd line already, thirdLineChance to get it
            if (Util.succeedProp(100 - thirdLineChance)) {
                max = 2;
            }
        }
        for (int i = 0; i < max; i++) {
            setOptionBonus(i, -val);
        }
    }

    public int getAnvilId() {
        return getOptions().get(6); // Anvil
    }


    public int getSocketStat(ScrollStat ss) {
        int amount = 0;
        for (int i = 0; i < getSockets().size(); i++) {
            int id = getSocket(i);
            if (id != 0 && id != ItemConstants.EMPTY_SOCKET_ID) {
                int nebuliteId = ItemConstants.NEBILITE_BASE_ID + id;
                Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(nebuliteId).getScrollStats();
                amount += vals.getOrDefault(ss, 0);
            }
        }
        return amount;
    }

    public List<Integer> getSockets() {
        return sockets;
    }

    public void setSockets(List<Integer> sockets) {
        this.sockets = sockets;
    }
    public void setSockets(String socket) {
        this.sockets = new ArrayList<>(); // TODO
    }

    public double getPotentialBaseStat(BaseStat baseStat, boolean isBonus) {
        double res = 0;
        // if isBonus is true then we get the total baseStat from bonus pot only
        int loopSize = isBonus ? getOptions().size() - 1 : 3;
        int startIndex = isBonus ? 3 : 0;
        for (int i = startIndex; i < loopSize; i++) { // last one is anvil => skipped
            int id = getOptions().get(i);
            int level = (getrLevel() + getiIncReq()) / 10 + 1;
            ItemOption io = ItemData.getItemOptionById(id);
            if (io != null) {
                Map<BaseStat, Double> valMap = io.getStatValuesByLevel(level);
                switch (baseStat) {
                    case AllStatr:
                        // Allstats are stored as 4 basestats in the valmap (STRr, DEXr, INTr, LUKr) no AllStatr
                        if (valMap.getOrDefault(BaseStat.STRr, 0D) != 0D && valMap.getOrDefault(BaseStat.DEXr, 0D) != 0D) {
                            res += valMap.getOrDefault(BaseStat.STRr, 0D);
                        }
                        break;
                    case AllStat:
                        if (valMap.getOrDefault(BaseStat.STR, 0D) != 0D && valMap.getOrDefault(BaseStat.DEX, 0D) != 0D){
                            res += valMap.getOrDefault(BaseStat.STR, 0D);
                        }
                        break;
                    default:
                        res += valMap.getOrDefault(baseStat, 0D);
                        break;
                }
            }
        }
        return res;
    }

    public double getBaseStat(BaseStat baseStat) {
        // TODO: Sockets
        double res = 0;
        for (int i = 0; i < getOptions().size() - 1; i++) { // last one is anvil => skipped
            int id = getOptions().get(i);
            int level = (getrLevel() + getiIncReq()) / 10 + 1;
            ItemOption io = ItemData.getItemOptionById(id);
            if (io != null) {
                Map<BaseStat, Double> valMap = io.getStatValuesByLevel(level);
                res += valMap.getOrDefault(baseStat, 0D);
            }
        }
        switch (baseStat) {
            case STR:
                res += getTotalStat(EquipBaseStat.iStr);
                break;
            case DEX:
                res += getTotalStat(EquipBaseStat.iDex);
                break;
            case INT:
                res += getTotalStat(EquipBaseStat.iInt);
                break;
            case LUK:
                res += getTotalStat(EquipBaseStat.iLuk);
                break;
            case PAD:
                res += getTotalStat(EquipBaseStat.iPAD);
                break;
            case MAD:
                res += getTotalStat(EquipBaseStat.iMAD);
                break;
            case PDD:
                res += getTotalStat(EquipBaseStat.iPDD);
                break;
            case MDD:
                res += getTotalStat(EquipBaseStat.iMDD);
                break;
            case MHP:
                res += getTotalStat(EquipBaseStat.iMaxHP);
                break;
            case MMP:
                res += getTotalStat(EquipBaseStat.iMaxMP);
                break;
            case DAMr:
                res += getTotalStat(EquipBaseStat.damR);
                break;
            case ignoreTargetDEF:
                res += getTotalStat(EquipBaseStat.imdr);
                break;
            case EVA:
                res += getTotalStat(EquipBaseStat.iEVA);
                break;
            case ACC:
                res += getTotalStat(EquipBaseStat.iACC);
                break;
            case Speed:
                res += getTotalStat(EquipBaseStat.iSpeed);
                break;
            case Jump:
                res += getTotalStat(EquipBaseStat.iJump);
                break;
            case booster:
                res += getAttackSpeed();
                break;
            case reduceCooltime:
                res += getSocketStat(ScrollStat.reduceCooltime);
                break;
            case CriticaldamageMin:
                res += getSocketStat(ScrollStat.incCriticaldamageMin);
                break;
            case CriticaldamageMax:
                res += getSocketStat(ScrollStat.incCriticaldamageMax);
                break;
            case EVAr:
                res += getSocketStat(ScrollStat.incEVAr);
                break;
            case ACCr:
                res += getSocketStat(ScrollStat.incACCr);
                break;
            case RewardProp:
                res += getSocketStat(ScrollStat.incRewardProp);
                break;
            case MesoProp:
                res += getSocketStat(ScrollStat.incMesoProp);
                break;
        }
        return res;
    }

    @Override
    public boolean isTradable() {
        return !hasAttribute(EquipAttribute.Untradable);
    }

    public boolean hasUsedSlots() {
        Equip defaultEquip = ItemData.getEquipDeepCopyFromID(getItemId(), false);
        return defaultEquip.getTuc() != getTuc();
    }

    public void resetStats() {
        Equip normalEquip = ItemData.getEquipDeepCopyFromID(getItemId(), false);
        for (EquipBaseStat ebs : EquipBaseStat.values()) {
            setBaseStat(ebs, normalEquip.getBaseStat(ebs));
        }
    }

    public void applyScroll(Item scroll, Char chr, boolean success) {
        if (scroll == null) {
            chr.chatMessage(ChatType.SystemNotice, "Could not find scroll or equip.");
            chr.dispose();
            return;
        }
        int scrollID = scroll.getItemId();
        boolean boom = false;
        Map<ScrollStat, Integer> vals = ItemData.getItemInfoByID(scrollID).getScrollStats();
        if (!vals.isEmpty()) {
            boolean recover = vals.getOrDefault(ScrollStat.recover, 0) != 0;
            if (getBaseStat(EquipBaseStat.tuc) <= 0 && !recover) {
                chr.dispose();
                return;
            }
            boolean reset = vals.getOrDefault(ScrollStat.reset, 0) + vals.getOrDefault(ScrollStat.perfectReset, 0) != 0;
            boolean useTuc = !recover && !reset;
            int curse = vals.getOrDefault(ScrollStat.cursed, 0);
            if (success) {
                boolean chaos = vals.containsKey(ScrollStat.randStat);
                if (chaos) {
                    boolean noNegative = vals.containsKey(ScrollStat.noNegative);
                    int max = vals.containsKey(ScrollStat.incRandVol) ? ItemConstants.INC_RAND_CHAOS_MAX : ItemConstants.RAND_CHAOS_MAX;
                    for (EquipBaseStat ebs : ScrollStat.getRandStats()) {
                        int cur = (int) getBaseStat(ebs);
                        if (cur == 0) {
                            continue;
                        }
                        int randStat = Util.getRandom(max);
                        randStat = !noNegative && Util.succeedProp(50) ? -randStat : randStat;
                        addStat(ebs, randStat);
                    }
                }
                if (recover) {
                    Equip fullTucEquip = ItemData.getEquipDeepCopyFromID(getItemId(), false);
                    int maxTuc = fullTucEquip.getTuc();
                    if (getTuc() + getCuc() < maxTuc) {
                        addStat(EquipBaseStat.tuc, 1);
                    } else {
                        return; //clean slate scroll won't be consumed on items that it cannot be used on
                    }
                } else if (reset) {
                    resetStats();
                } else {
                    for (Map.Entry<ScrollStat, Integer> entry : vals.entrySet()) {
                        ScrollStat ss = entry.getKey();
                        int val = entry.getValue();
                        if (ss.getEquipStat() != null) {
                            addStat(ss.getEquipStat(), val);
                        }
                    }
                }
                if (useTuc) {
                    addStat(EquipBaseStat.tuc, -1);
                    addStat(EquipBaseStat.cuc, 1);
                }
            } else {
                if (curse > 0) {
                    boom = Util.succeedProp(curse);
                    if (boom && !hasAttribute(EquipAttribute.ProtectionScroll)) {
                        chr.consumeItem(this, 1);
                    } else {
                        boom = false;
                    }
                }
                if (useTuc && !hasAttribute(EquipAttribute.UpgradeCountProtection)) {
                    addStat(EquipBaseStat.tuc, -1);
                }
            }
            removeAttribute(EquipAttribute.ProtectionScroll);
            removeAttribute(EquipAttribute.LuckyDay);
            if (useTuc) {
                removeAttribute(EquipAttribute.UpgradeCountProtection);
            }
//            chr.getClient().write(FieldPacket.showItemUpgradeEffect(chr.getId(), success, false, scrollID, getItemId(), boom));
            if (!boom) {
                updateToChar(chr);
            }
            chr.consumeItem(scroll, 1);
        } else {
            chr.chatMessage(ChatType.Normal, "Could not find scroll data.");
            chr.dispose();
        }
    }

    public void addItemSkill(ItemSkill itemSkill) {
        this.itemSkills.add(itemSkill);
    }
    public List<ItemSkill> getItemSkills() {
        return itemSkills;
    }

}
