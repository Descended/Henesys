package henesys.handlers;

import henesys.Server;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.handlers.header.InHeader;
import henesys.util.container.Tuple;
import henesys.world.field.Field;
import henesys.world.field.Portal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MigrationHandler {

    private static final Logger log = LogManager.getLogger(MigrationHandler.class);

    @Handler(op = InHeader.MIGRATE_IN)
    public static void handleMigrateIn(Client c, InPacket inPacket) {
        short idk = inPacket.decodeShort();
        int charId = inPacket.decodeInt();
        boolean adminClient = inPacket.decodeByte() != 0;
        Tuple<Byte, Client> info = Server.getInstance().getChannelFromTransfer(charId, 0);
        byte channel = info.getLeft();
        Client oldClient = info.getRight();
        User user = oldClient.getUser();
        c.setUser(user);
        Account acc = oldClient.getAccount();
        c.setAccount(acc);
        Server.getInstance().getWorldById(0).getChannelById(channel).removeClientFromTransfer(charId);
        c.setChannel(channel);
        c.setWorldId((byte) 0);
        c.setChannelInstance(Server.getInstance().getWorldById(0).getChannelById(channel));
        Char chr = oldClient.getChr();
        if (chr == null || chr.getId() != charId) {
            chr = acc.getCharById(charId);
        }
        chr.setClient(c);
        c.getChannelInstance().addChar(chr);
        c.setChr(chr);
        Field field = chr.getOrCreateFieldByCurrentInstanceType(chr.getCharacterStat().getFieldId() <= 0 ? 100000000 : chr.getCharacterStat().getFieldId());
        chr.warp(field, field.getPortalByName("sp"), true, true);
    }

    @Handler(op = InHeader.USER_TRANSFER_FIELD_REQUEST)
    public static void handleUserTransferFieldRequest(Client c, InPacket inPacket) {
        byte fieldKey = inPacket.decodeByte();
        int fieldId = inPacket.decodeInt();
        String portalName = inPacket.decodeString();
        byte idk = inPacket.decodeByte();
        boolean wheel = inPacket.decodeByte() != 0;
        boolean chase = inPacket.decodeByte() != 0;
        Char chr = c.getChr();
        Field field = chr.getField();
        Portal portal = field.getPortalByName(portalName);
        if (portal.getScript() != null && !portal.getScript().isEmpty()) {
//            chr.getScriptManager().startScript(portal.getId(), portal.getScript(), ScriptType.Portal);
        } else {
            Field toField = chr.getOrCreateFieldByCurrentInstanceType(portal.getTargetMapId());
            if (toField == null) {
                return;
            }
            Portal toPortal = toField.getPortalByName(portal.getTargetPortalName());
            if (toPortal == null) {
                toPortal = toField.getPortalByName("sp");
            }
            chr.warp(toField, toPortal, false, false);
        }
    }
}
