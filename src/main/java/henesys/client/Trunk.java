package henesys.client;

import henesys.items.Item;

import java.util.List;

public class Trunk {

    private int id;

    private int slotCount;

    private int money;

    public Trunk(int slotCount, int money) {
        this.slotCount = slotCount;
        this.money = money;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
