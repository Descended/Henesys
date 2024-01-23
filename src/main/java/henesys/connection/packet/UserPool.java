package henesys.connection.packet;

import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.client.character.avatar.AvatarLook;
import henesys.client.character.skills.temp.TemporaryStatManager;
import henesys.connection.OutPacket;
import henesys.handlers.header.OutHeader;

public class UserPool {

    public static OutPacket userEnterField(Char chr) {
        CharacterStat cs = chr.getCharacterStat();
        AvatarLook al = chr.getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();
        OutPacket outPacket = new OutPacket(OutHeader.USER_ENTER_FIELD);

        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(cs.getLevel());
        outPacket.encodeString(cs.getName());
        if(false) { // chr.getGuild() != null
//            chr.getGuild().encodeForRemote(outPacket);
        } else {
//            Guild.defaultEncodeForRemote(outPacket);
            outPacket.encodeString("");
            outPacket.encodeShort(0);
            outPacket.encodeByte(0);
            outPacket.encodeShort(0);
            outPacket.encodeByte(0);
        }
        tsm.encodeForRemote(outPacket, tsm.getCurrentStats());
        outPacket.encodeShort(cs.getJob());
        al.encode(outPacket);
        outPacket.encodeInt(0); // DriverID
        outPacket.encodeInt(0); // dwPassenserID
        outPacket.encodeInt(0); // nChocoCount
        outPacket.encodeInt(chr.getActiveEffectItemID());
        outPacket.encodeInt(chr.getCompletedSetItemID());
        outPacket.encodeInt(chr.getPortableChairID());
        outPacket.encodePosition(chr.getPosition());
        outPacket.encodeByte(0); //bShowAdminEffect
//        for(Pet pet : chr.getPets()) { TODO ENCODE PETS
//            if(pet.getId() == 0) {
//                continue;
//            }
//            outPacket.encodeByte(1);
//            pet.encode(outPacket);
//        }

        outPacket.encodeByte(0); // pet end byte
        outPacket.encodeInt(chr.getTamingMobLevel());
        outPacket.encodeInt(chr.getTamingMobExp());
        outPacket.encodeInt(chr.getTamingMobFatigue());
//        byte miniRoomType = chr.getMiniRoom() != null ? chr.getMiniRoom().getMiniRoomType().getVal() : 0;
        byte miniRoomType = 0;
        outPacket.encodeByte(miniRoomType);
        if(miniRoomType > 0) {
//            chr.getMiniRoom().encode(outPacket);
        }
        outPacket.encodeByte(chr.getADBoardRemoteMsg() != null);
        if (chr.getADBoardRemoteMsg() != null) {
            outPacket.encodeString(chr.getADBoardRemoteMsg());
        }
        outPacket.encodeByte(chr.hasFriendshipItem());
        if(chr.hasFriendshipItem()) {
//            chr.getFriendshipRingRecord().encode(outPacket);
        }
        return outPacket;
    }

    public static OutPacket userLeaveField(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.USER_LEAVE_FIELD);

        outPacket.encodeInt(chr.getId());

        return outPacket;
    }
}
