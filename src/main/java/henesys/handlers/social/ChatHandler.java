package henesys.handlers.social;

import henesys.client.Client;
import henesys.connection.InPacket;
import henesys.connection.packet.UserPacket;
import henesys.enums.ChatUserType;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ChatHandler {

    private static final Logger log = LogManager.getLogger(ChatHandler.class);


    @Handler(op = InHeader.USER_CHAT)
    public static void handleUserChat(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // timestamp
        String message = inPacket.decodeString();
        if (Objects.equals(message, "@dispose")) {
            c.getChr().dispose();
            return;
        } else if (Objects.equals(message, "@heal")) {
            c.getChr().heal(c.getChr().getCharacterStat().getMaxHp(), true);
            return;
        }
        boolean balloon = inPacket.decodeByte() != 0;
        c.getChr().getField().broadcastPacket(UserPacket.chat(c.getChr().getId(), ChatUserType.User, message, balloon));


    }

}
