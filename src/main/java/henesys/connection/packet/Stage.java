package henesys.connection.packet;

import henesys.client.character.Char;
import henesys.connection.OutPacket;
import henesys.enums.DBChar;
import henesys.handlers.header.OutHeader;
import henesys.util.FileTime;
import henesys.world.field.Field;

import java.security.SecureRandom;
import java.util.Random;

public class Stage {

    public static OutPacket setField(Char chr, Field field, int portalId, int channelId, int worldId, boolean characterData) {
        OutPacket outPacket = new OutPacket(OutHeader.SET_FIELD);
        outPacket.encodeShort(0); // Client option
        outPacket.encodeInt(channelId);
        outPacket.encodeInt(worldId);

        outPacket.encodeByte(1); // sNotifierMessage.m_pStr
        outPacket.encodeByte(characterData);
        outPacket.encodeShort(0); // nNotifierCheck, loop size

        if (characterData) {
            Random random = new SecureRandom();
            int s1 = random.nextInt();
            int s2 = random.nextInt();
            int s3 = random.nextInt();
            outPacket.encodeInt(s1);
            outPacket.encodeInt(s2);
            outPacket.encodeInt(s3);

            // TODO: DamageCalc

            chr.encode(outPacket, DBChar.All);

            // Logout Gift
            outPacket.encodeInt(0);

            for (int i = 0; i < 3; i++) {
                outPacket.encodeInt(0);
            }

        } else {
            outPacket.encodeByte(0); // Revive stuff
            outPacket.encodeInt(field.getId());
            outPacket.encodeByte(portalId);
            outPacket.encodeInt(chr.getCharacterStat().getHp());
            outPacket.encodeByte(0); // Enables two EncodeInts
        }
        outPacket.encodeFT(FileTime.currentTime());
        return outPacket;
    }
}
