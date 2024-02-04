package henesys.connection.packet;

import henesys.connection.OutPacket;
import henesys.handlers.header.OutHeader;

public class ClientSocket {

    public static OutPacket migrateCommand(boolean success, byte[] ip, int port) {
        OutPacket outPacket = new OutPacket(OutHeader.MIGRATE_COMMAND);

        outPacket.encodeByte(success); // will disconnect if false
        if(success) {
            outPacket.encodeArr(ip);
            outPacket.encodeShort(port);
        }

        return outPacket;
    }
}
