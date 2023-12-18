package henesys.client.character;

import henesys.client.character.avatar.AvatarLook;

public class Char {

    private String name;

    private int id;

    private boolean changingChannel;

    private boolean inCashShop;

    private AvatarLook avatarLook;

    private CharacterStat characterStat;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
