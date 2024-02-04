package henesys.handlers;

import henesys.Server;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.enums.ChatType;
import henesys.enums.FieldOption;
import henesys.handlers.header.InHeader;
import henesys.util.container.Tuple;
import henesys.world.World;
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
        byte[] machineID = inPacket.decodeArr(16);
        Tuple<Byte, Client> info = Server.getInstance().getChannelFromTransfer(charId, 0);
        byte channel = info.getLeft();
        Client oldClient = info.getRight();
        if (!oldClient.hasCorrectMachineID(machineID)) {
//            c.write(WvsContext.returnToTitle());
            return;
        }
        User user = oldClient.getUser();
        c.setUser(user);
        Account acc = oldClient.getAccount();
        c.setAccount(acc);
        Server.getInstance().getWorldById(0).getChannelById(channel).removeClientFromTransfer(charId);
        c.setChannel(channel);
        c.setWorldId((byte) 0);
        c.setChannelInstance(Server.getInstance().getWorldById(0).getChannelById(channel));
        c.setMachineID(machineID);
        Char chr = oldClient.getChr();
        if (chr == null || chr.getId() != charId) {
            chr = acc.getCharById(charId);
        }
        chr.setClient(c);
        c.getChannelInstance().addChar(chr);
        c.setChr(chr);
        Field field = chr.getOrCreateFieldByCurrentInstanceType(chr.getCharacterStat().getFieldId() <= 0 ? 100000000 : chr.getCharacterStat().getFieldId());
        chr.warp(field, field.getPortalByName("sp"), true, true);
        chr.setChangingChannel(false);
    }

    @Handler(op = InHeader.USER_TRANSFER_CHANNEL_REQUEST)
    public static void handleUserTransferChannelRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        byte channelId = (byte) (inPacket.decodeByte() + 1);
        World world = Server.getInstance().getWorldById(c.getWorldId());
        if (world.getChannelById(channelId) == null) {
            chr.chatMessage(ChatType.SystemNotice, "Could not find that world.");
            return;
        }
        if (chr.getCharacterStat().getHp() <= 0) {
            chr.dispose();
            return;
        }

        Field field = chr.getField();
        if ((field.getFieldLimit() & FieldOption.MigrateLimit.getVal()) > 0
                || channelId < 1 || channelId > world.getChannels().size()) {
            chr.dispose();
            return;
        }

        chr.changeChannel(channelId);
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
