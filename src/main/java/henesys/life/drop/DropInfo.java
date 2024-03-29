package henesys.life.drop;

import henesys.constants.GameConstants;
import henesys.util.Util;

/**
 * Created on 2/21/2018.
 */
public class DropInfo {
    private long id;
    private int itemID;
    private int chance; // out of a 10000 - It's actually GameConstants.MAX_DROP_CHANCE - KOOKIIE
    private int money;
    private int minMoney;
    private int maxmoney;
    private int minQuant = 1;
    private int maxQuant = 1;
    private int quantity = 1;
    private boolean reactorDrop;
    private boolean instanced;

    public DropInfo() {
    }

    public DropInfo(int itemID, int chance) {
        this.itemID = itemID;
        this.chance = chance;
    }
    public DropInfo(int itemID, int chance, boolean instanced) {
        this.itemID = itemID;
        this.chance = chance;
        this.instanced = true;
    }

    public DropInfo(int chance, int minMoney, int maxmoney) {
        this.chance = chance;
        this.minMoney = minMoney;
        this.maxmoney = maxmoney;
        generateNextDrop();
    }

    public DropInfo(int itemID, int chance, int minQuant, int maxQuant) {
        this.itemID = itemID;
        this.chance = chance;
        this.minQuant = minQuant;
        this.maxQuant = maxQuant;
        generateNextDrop();
    }

    public DropInfo(int itemID, int chance, int minQuant, int maxQuant, boolean instanced) {
        this.itemID = itemID;
        this.chance = chance;
        this.minQuant = minQuant;
        this.maxQuant = maxQuant;
        this.instanced = instanced;
        generateNextDrop();
    }

    public DropInfo(int itemID, int chance, int minQuant, int maxQuant, boolean instanced, int customQuestID) {
        this.itemID = itemID;
        this.chance = chance;
        this.minQuant = minQuant;
        this.maxQuant = maxQuant;
        this.instanced = instanced;
        generateNextDrop();
    }

    public void generateNextDrop() {
        if (getMaxMoney() > 0) {
            setMoney(getMinMoney() + Util.getRandom(getMaxMoney() - getMinMoney()));
        } else {
            setQuantity(getMinQuant() + Util.getRandom(getMaxQuant() - getMinQuant()));
        }
    }

    public int getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(int minMoney) {
        this.minMoney = minMoney;
    }

    public int getMaxMoney() {
        return maxmoney;
    }

    public void setMaxMoney(int maxmoney) {
        this.maxmoney = maxmoney;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    /**
     * Does an RNG roll to check if this should be dropped.
     *
     * @param dropRate the drop rate of the Char, baseline should be 0.
     * @param worldDropRate the world drop rate, baseline should be 1.
     * @return Whether or not the drop is successful.
     */
    public boolean willDrop(int dropRate, int worldDropRate) {
        // Added 50x multiplier for the dropping chance if the item is a Quest item.
        int chance = getChance();
        chance *= (100 + dropRate) * worldDropRate / 100D;
        return Util.succeedProp(chance, GameConstants.MAX_DROP_CHANCE);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isMoney() {
        return getMoney() > 0 || getMinMoney() > 0;
    }

    public void multiplyMoniesBy(double multiplier) {
        if (this.minMoney > 0) {
            this.minMoney = (int) (this.minMoney * multiplier);
        }
        if (this.maxmoney > 0) {
            this.maxmoney = (int) (this.maxmoney * multiplier);
        }
    }

    public int getMinQuant() {
        return minQuant;
    }

    public void setMinQuant(int minQuant) {
        this.minQuant = minQuant;
    }

    public int getMaxQuant() {
        return maxQuant;
    }

    public void setMaxQuant(int maxQuant) {
        this.maxQuant = maxQuant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        if (getItemID() != 0) {
            return String.format("Item %d, chance %d", getItemID(), getChance());
        } else {
            return String.format("%d mesos.", getMoney());
        }
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DropInfo deepCopy() {
        DropInfo di = new DropInfo();

        di.setItemID(getItemID());
        di.setChance(getChance());
        di.setMoney(getMoney());
        di.setMinMoney(getMinMoney());
        di.setMaxMoney(getMaxMoney());
        di.setMinQuant(getMinQuant());
        di.setMaxQuant(getMaxQuant());
        di.setQuantity(getQuantity());
        return di;
    }

    public void setReactorDrop(boolean reactorDrop) {
        this.reactorDrop = reactorDrop;
    }

    public boolean getReactorDrop() {
        return reactorDrop;
    }

    public boolean isInstanced() {
        return instanced;
    }

    public void setInstanced(boolean instanced) {
        this.instanced = instanced;
    }

}
