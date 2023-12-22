package henesys.items;

import henesys.enums.InvType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final Logger log = LogManager.getLogger(Inventory.class);

    private int id;

    private List<Item> items;

    private InvType type;

    private byte slots;

    public Inventory(InvType type, int slots) {
        this.type = type;
        this.slots = (byte) slots;
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public InvType getType() {
        return type;
    }

    public void setType(InvType type) {
        this.type = type;
    }

    public byte getSlots() {
        return slots;
    }

    public void setSlots(byte slots) {
        this.slots = slots;
    }
}
