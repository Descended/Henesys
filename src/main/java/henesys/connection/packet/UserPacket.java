package henesys.connection.packet;

import henesys.connection.OutPacket;
import henesys.enums.ChatUserType;
import henesys.handlers.header.OutHeader;

public class UserPacket {

    public static OutPacket chat(int charID, ChatUserType type, String msg, boolean onlyBalloon) {
        OutPacket outPacket = new OutPacket(OutHeader.USER_CHAT);

        outPacket.encodeInt(charID);
        outPacket.encodeByte(type.getVal());
        outPacket.encodeString(msg);
        outPacket.encodeByte(onlyBalloon);
        return outPacket;
    }
}
