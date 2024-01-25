package henesys.connection.packet;

import henesys.client.character.Char;
import henesys.client.character.avatar.AvatarLook;
import henesys.client.character.skills.temp.TemporaryStatManager;
import henesys.connection.OutPacket;
import henesys.enums.AvatarModifiedMask;
import henesys.handlers.header.OutHeader;
import henesys.skills.CharacterTemporaryStat;

public class UserRemote {

    public static OutPacket avatarModified(Char chr, byte mask, byte carryItemEffect) {
        AvatarLook al = chr.getAvatarLook();
        TemporaryStatManager tsm = chr.getTemporaryStatManager();

        OutPacket outPacket = new OutPacket(OutHeader.USER_AVATAR_MODIFIED);

        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(mask);
        if((mask & AvatarModifiedMask.AvatarLook.getVal()) != 0) {
            al.encode(outPacket);
        }
        if((mask & AvatarModifiedMask.SubAvatarLook.getVal()) != 0) {
            al.encode(outPacket); // subAvatarLook
        }
        if((mask & AvatarModifiedMask.Speed.getVal()) != 0) {
            outPacket.encodeByte(tsm.getOption(CharacterTemporaryStat.Speed).nOption);
        }
        if((mask & AvatarModifiedMask.CarryItemEffect.getVal()) != 0) {
            outPacket.encodeByte(carryItemEffect);
        }
        boolean hasFriendShip = false;
        outPacket.encodeByte(hasFriendShip);
        if(hasFriendShip) {
//            chr.getFriendshipRingRecord().encode(outPacket);
        }
        outPacket.encodeInt(chr.getCompletedSetItemID());

        return outPacket;
    }

}
