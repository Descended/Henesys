package henesys.handlers;

import henesys.client.Client;
import henesys.connection.InPacket;
import henesys.handlers.header.InHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorHandler {
    private static final Logger log = LogManager.getLogger(ErrorHandler.class);

    @Handler(op = InHeader.CLIENT_DUMP_LOG)
    public static void handleClientDumpLog(Client c, InPacket inPacket) {
        short callType = inPacket.decodeShort();
        int errorCode = inPacket.decodeInt();
        short backupBufferSize = inPacket.decodeShort();
        int rawSeq = inPacket.decodeInt();
        short type = inPacket.decodeShort();
        byte[] backupBuffer = inPacket.decodeArr(backupBufferSize-6);

        log.error("Client Dump Log: callType: {}, errorCode: {}, backupBufferSize: {}, rawSeq: {}, type: {}, backupBuffer: {}", callType, errorCode, backupBufferSize, rawSeq, type, backupBuffer);
    }
}
