package henesys.items;

import henesys.Server;
import henesys.constants.ItemConstants;
import henesys.enums.InvType;
import henesys.enums.WeaponType;
import henesys.items.container.ItemInfo;
import henesys.loaders.ItemData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {

    private static final Logger log = LogManager.getLogger(Inventory.class);

    private int id;

    private List<Item> items;

    private InvType type;

    private byte slots;

    private int characterId;

    public Inventory() {
    }

    public Inventory(InvType type, int slots, int characterId) {
        this.type = type;
        this.slots = (byte) slots;
        this.characterId = characterId;
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

    public Item getItemByItemID(int itemId) {
        return getItems().stream().filter(item -> (item.getItemId() == itemId) && item.getQuantity() != 0).findFirst().orElse(null);
    }

    public Item getItemByItemIDAndStackable(int itemId) {
        ItemInfo ii = ItemData.getItemInfoByID(itemId);
        if (ii == null) {
            return getItemByItemID(itemId);
        }
        return getItems().stream()
                .filter(item -> item.getItemId() == itemId && item.getQuantity() < ii.getSlotMax())
                .min(Comparator.comparingInt(Item::getBagIndex))
                .orElse(null);
    }

    public Item getFirstItemByBodyPart(BodyPart bodyPart) {
        List<Item> items = getItemsByBodyPart(bodyPart);
        return items != null && !items.isEmpty() ? items.getFirst() : null;
    }

    public List<Item> getItemsByBodyPart(BodyPart bodyPart) {
        return getItems().stream().filter(item -> item.getBagIndex() == bodyPart.getVal()).collect(Collectors.toList());
    }

    public void addItem(Item item) {
        if (getItems().size() < getSlots()) {
            if (item.getId() == 0) {
                Server.getInstance().assignItemIdAndInc(item);
            }
            getItems().add(item);
            item.setInvType(getType());
            item.setInventoryId(getId());
            sortItemsByIndex();
        }
    }

    public void sortItemsByIndex() {
        getItems().sort(Comparator.comparingInt(Item::getBagIndex)); // sort by bag index
    }

        public int getFirstOpenSlot() {
        int oldIndex = 0;
        sortItemsByIndex();
        for (Item item : getItems()) {
            // items are always sorted by bag index
            if (item.getBagIndex() - oldIndex > 1) {
                // there's a gap between 2 consecutive items
                break;
            }
            oldIndex = item.getBagIndex();
        }
        return oldIndex + 1;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public Item getItemBySlot(int bagIndex) {
        return getItemBySlotS(bagIndex < 0 ? -bagIndex : bagIndex);
    }

    private Item getItemBySlotS(int bagIndex) {
        return getItems().stream().filter(item -> item.getBagIndex() == bagIndex).findAny().orElse(null);
    }

    public void removeItem(Item item) {
        getItems().remove(item);
        sortItemsByIndex();
    }

    public boolean isFull() {
        return getItems().size() >= getSlots();
    }

    public WeaponType getEquippedWeaponType() {
        Item weapon = getEquippedItemByBodyPart(BodyPart.Weapon);
        if (weapon == null) {
            return WeaponType.Barehand;
        }
        return WeaponType.getByVal(ItemConstants.getWeaponType(weapon.getItemId()));
    }

    /**
     * Returns the Equip equipped at a certain {@link BodyPart}.
     *
     * @param bodyPart The requested bodyPart.
     * @return The Equip corresponding to <code>bodyPart</code>. Null if there is none.
     */
    public Item getEquippedItemByBodyPart(BodyPart bodyPart) {
        List<Item> items = getItemsByBodyPart(bodyPart);
        return !items.isEmpty() ? items.getFirst() : null;
    }
}
