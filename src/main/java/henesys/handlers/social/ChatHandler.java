package henesys.handlers.social;

import henesys.client.Client;
import henesys.connection.InPacket;
import henesys.connection.packet.UserPacket;
import henesys.enums.ChatUserType;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatHandler {

    private static final Logger log = LogManager.getLogger(ChatHandler.class);


    @Handler(op = InHeader.USER_CHAT)
    public static void handleUserChat(Client c, InPacket inPacket) {
        inPacket.decodeInt(); // timestamp
        String message = inPacket.decodeString();
        if (message == "@dispose") {
        }
        boolean balloon = inPacket.decodeByte() != 0;
        c.getChr().getField().broadcastPacket(UserPacket.chat(c.getChr().getId(), ChatUserType.User, message, balloon));


    }

}
