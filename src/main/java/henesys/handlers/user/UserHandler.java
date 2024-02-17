package henesys.handlers.user;

import henesys.client.Client;
import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.packet.FieldPacket;
import henesys.connection.packet.UserRemote;
import henesys.enums.ChatType;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;
import henesys.life.movement.MovementInfo;
import henesys.util.Position;
import henesys.world.field.Field;
import henesys.world.field.Portal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserHandler {

    private static final Logger log = LogManager.getLogger(UserHandler.class);

    @Handler(op = InHeader.USER_MOVE)
    public static void handleUserMove(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeLong();
        inPacket.decodeByte(); // FieldKey
        inPacket.decodeLong();
        inPacket.decodeInt(); // Map Crc
        inPacket.decodeInt(); // dwKey
        inPacket.decodeInt(); // dwKeyCrc

        // CMovePath::Flush -> CMovePath::Encode (line 85)
        MovementInfo movementInfo = new MovementInfo(inPacket);
        movementInfo.applyTo(chr);

        chr.getField().broadcastPacket(UserRemote.move(chr, movementInfo), chr);
    }

    @Handler(op = InHeader.USER_PORTAL_TELEPORT_REQUEST)
    public static void handleUserTeleportRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        Field field = chr.getField();
        byte fieldKey = inPacket.decodeByte();
        String portalName = inPacket.decodeString();
        Position pos = inPacket.decodePosition();
        Portal portal = field.getPortalByName(portalName);
        if (portal == null) {
            // Character is trying to teleport to a portal that doesn't exist in the current field (WZ Edit)
            chr.chatMessage(ChatType.SystemNotice, "The portal you tried to teleport to could not be found.");
        }
    }
    @Handler(op = InHeader.USER_CHARACTER_INFO_REQUEST)
    public static void handleUserCharacterInfoRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        inPacket.decodeInt(); // tick
        int requestID = inPacket.decodeInt();
        Char requestChar = c.getChr().getField().getCharByID(requestID); // TODO: Fix this
        if (requestChar == null) {
            chr.chatMessage(ChatType.SystemNotice, "The character you tried to find could not be found.");
            chr.dispose();
        } else {
            c.write(FieldPacket.characterInfo(requestChar));
        }
    }

}
