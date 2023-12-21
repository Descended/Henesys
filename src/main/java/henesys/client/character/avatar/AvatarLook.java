package henesys.client.character.avatar;

import henesys.connection.OutPacket;
import henesys.constants.ItemConstants;

import java.util.ArrayList;
import java.util.List;

public class AvatarLook {

    private int id;

    private int characterId;
    private int gender;
    private int skin;
    private int face;
    private int hair;

    private int weaponId;

    private List<Integer> hairEquips;
    private List<Integer> unseenEquips;

    private List<Integer> petIDs;

    public AvatarLook() {
        this.hairEquips = new ArrayList<>();
        this.unseenEquips = new ArrayList<>();
        this.petIDs = new ArrayList<>();
    }

    public AvatarLook(int gender, int skin, int face, int hair, int weaponId) {
        this.gender = gender;
        this.skin = skin;
        this.face = face;
        this.hair = hair;
        this.weaponId = weaponId;
        this.hairEquips = new ArrayList<>();
        this.unseenEquips = new ArrayList<>();
        this.petIDs = new ArrayList<>();
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getGender());
        outPacket.encodeByte(getSkin());
        outPacket.encodeInt(getFace());
        outPacket.encodeByte(0); // ignored
        outPacket.encodeInt(getHair());

        for (int itemId : getHairEquips()) {
            int bodyPart = ItemConstants.getBodyPartFromItem(itemId, getGender());
            if (bodyPart != 0) {
                outPacket.encodeByte(bodyPart); // body part
                outPacket.encodeInt(itemId); // item id
            }
        }
        outPacket.encodeByte(-1);
        for (int itemId : getUnseenEquips()) {
            outPacket.encodeByte(ItemConstants.getBodyPartFromItem(itemId, getGender())); // body part
            outPacket.encodeInt(itemId);
        }
        outPacket.encodeByte(-1);

        outPacket.encodeInt(getWeaponId());

        for (int i = 0; i < 3; i++) {
            if (getPetIDs().size() > i) {
                outPacket.encodeInt(getPetIDs().get(i)); // always 3
            } else {
                outPacket.encodeInt(0);
            }
        }
    }

    public List<Integer> getPetIDs() {
        return petIDs;
    }

    public void setPetIDs(List<Integer> petIDs) {
        this.petIDs = petIDs;
    }

    public byte getGender() {
        return (byte) gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public byte getSkin() {
        return (byte) skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public List<Integer> getHairEquips() {
        return hairEquips;
    }

    public void setHairEquips(List<Integer> hairEquips) {
        this.hairEquips = hairEquips;
    }

    public List<Integer> getUnseenEquips() {
        return unseenEquips;
    }

    public void setUnseenEquips(List<Integer> unseenEquips) {
        this.unseenEquips = unseenEquips;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
}
