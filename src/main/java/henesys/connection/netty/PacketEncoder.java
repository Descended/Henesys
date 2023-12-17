/*
    This file is part of Desu: MapleStory v62 Server Emulator
    Copyright (C) 2014  Zygon <watchmystarz@hotmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package henesys.connection.netty;

import henesys.ServerConfig;
import henesys.ServerConstants;
import henesys.connection.OutPacket;
import henesys.connection.crypto.MapleCrypto;
import henesys.connection.crypto.ShandaCrypto;
import henesys.handlers.header.OutHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;

/**
 * Implementation of a Netty encoder pattern so that encryption of MapleStory
 * packets is possible. Follows steps using the special MapleAES as well as
 * ShandaCrypto (which became non-used after v149.2 in GMS).
 *
 * @author Zygon
 */
public final class PacketEncoder extends MessageToByteEncoder<OutPacket> {
    private static final org.apache.logging.log4j.Logger log = LogManager.getRootLogger();

    @Override
    protected void encode(ChannelHandlerContext chc, OutPacket outPacket, ByteBuf bb) {
        byte[] data = outPacket.getData();
        NettyClient c = chc.channel().attr(NettyClient.CLIENT_KEY).get();
        MapleCrypto mCr = chc.channel().attr(NettyClient.CRYPTO_KEY).get();

        if (c != null) {
            if(!OutHeader.isSpamHeader(OutHeader.getOutHeaderByOp(outPacket.getHeader()))) {
                if(ServerConfig.DEBUG_MODE){
                    log.debug("[Out]\t| {}", outPacket);
                }
            }
            byte[] iv = c.getSendIV();
            byte[] head = MapleCrypto.getHeader(data.length, iv);

            c.acquireEncoderState();
            try {
                if (ServerConstants.VERSION < 149) {
                    ShandaCrypto.encryptData(data);
                }
                mCr.crypt(data, iv);
                c.setSendIV(MapleCrypto.getNewIv(iv));
            } finally {
                c.releaseEncodeState();
            }
            
            bb.writeBytes(head);
            bb.writeBytes(data);
            
        } else {
            log.debug("[PacketEncoder] | Plain sending {}", outPacket);
            bb.writeBytes(data);
        }
        outPacket.release();
    }
}
