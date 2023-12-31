package henesys.handlers;

import henesys.Server;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.packet.Stage;
import henesys.handlers.header.InHeader;
import henesys.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        c.write(Stage.setField(chr, channel, 0, true));
    }
}
