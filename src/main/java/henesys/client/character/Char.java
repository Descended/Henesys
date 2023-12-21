package henesys.client.character;

import henesys.client.Account;
import henesys.client.Client;
import henesys.client.character.avatar.AvatarLook;
import henesys.connection.packet.UserLocal;
import henesys.enums.ChatType;
import henesys.items.Item;

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
    private Client client;

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
}
