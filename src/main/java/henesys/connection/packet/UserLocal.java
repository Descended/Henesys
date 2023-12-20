package henesys.connection.packet;

import henesys.connection.OutPacket;
import henesys.enums.ChatType;
import henesys.handlers.header.OutHeader;

public class UserLocal {

    public static OutPacket chatMsg(ChatType colour, String msg) {
        OutPacket outPacket = new OutPacket(OutHeader.USER_CHAT_MSG);

        outPacket.encodeShort(colour.getVal());
        outPacket.encodeString(msg);

        return outPacket;
    }
}
