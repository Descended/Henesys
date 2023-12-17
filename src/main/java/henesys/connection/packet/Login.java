package henesys.connection.packet;

import henesys.ServerConstants;
import henesys.client.User;
import henesys.connection.OutPacket;
import henesys.enums.LoginType;
import henesys.handlers.header.OutHeader;
import henesys.util.Position;
import henesys.util.container.Tuple;
import henesys.world.Channel;
import henesys.world.World;

import java.util.Set;

/**
 * @author Desc
 */
public class Login {

    public static OutPacket sendConnect(byte[] siv, byte[] riv) {
        OutPacket outPacket = new OutPacket();

        outPacket.encodeShort(14);
        outPacket.encodeShort(ServerConstants.VERSION);
        outPacket.encodeString(ServerConstants.MINOR_VERSION);
        outPacket.encodeArr(siv);
        outPacket.encodeArr(riv);
        outPacket.encodeByte(8);
        return outPacket;
    }

    public static OutPacket checkPasswordResult(LoginType loginType, User user) {
        OutPacket outPacket = new OutPacket(OutHeader.CHECK_PASSWORD_RESULT.getValue());
        outPacket.encodeByte(loginType.getValue());
        outPacket.encodeByte(0);
        outPacket.encodeInt(0);
        if (loginType == LoginType.Success) {
            outPacket.encodeInt(user.getId());
            outPacket.encodeByte(user.getGender());
            outPacket.encodeByte(user.getAccountMode());
            outPacket.encodeShort(user.getUserType().getVal());
            outPacket.encodeByte(0); // nCountryId
            outPacket.encodeString(user.getUsername());
            outPacket.encodeByte(0); // nPurchaseExp
            outPacket.encodeByte(0); // Chat Unblock Reason
            outPacket.encodeLong(0); // Chat Unblock Date
            outPacket.encodeLong(0); // dtRegisterDate
            outPacket.encodeInt(user.getCharacterSlots());
            outPacket.encodeByte(1); // v44
            outPacket.encodeByte(1); // sMsg
            outPacket.encodeLong(0);
        } else if (loginType == LoginType.Blocked) {
            // TODO: Implement Blocked LoginType
        } else {
            //
        }

        return outPacket;

    }

    public static OutPacket sendWorldInformation(World world, Set<Tuple<Position, String>> stringInfos) {
        // CLogin::OnWorldInformation
        OutPacket outPacket = new OutPacket(OutHeader.WORLD_INFORMATION.getValue());
        outPacket.encodeByte(world.getWorldId());
        outPacket.encodeString(world.getName());
        outPacket.encodeByte(world.getWorldState());
        outPacket.encodeString(world.getWorldEventDescription());
        outPacket.encodeInt(world.getWorldEventEXP_WSE());
        outPacket.encodeInt(world.getWorldEventDrop_WSE());
        outPacket.encodeByte(world.isCharCreateBlock());
        outPacket.encodeByte(world.getChannels().size());
        for (Channel channel : world.getChannels()) {
            outPacket.encodeString(channel.getName());
            outPacket.encodeInt(channel.getGaugePx());
            outPacket.encodeInt(channel.getWorldId());
            outPacket.encodeInt(channel.getChannelId());
            outPacket.encodeByte(channel.isAdultChannel());
        }
        if (stringInfos == null) {
            outPacket.encodeShort(0);
        } else { // Balloon
            outPacket.encodeShort(stringInfos.size());
            for (Tuple<Position, String> stringInfo : stringInfos) {
                outPacket.encodePosition(stringInfo.getLeft());
                outPacket.encodeString(stringInfo.getRight());
            }
        }
        return outPacket;
    }

    public static OutPacket sendWorldInformationEnd() {
        OutPacket outPacket = new OutPacket(OutHeader.WORLD_INFORMATION);

        outPacket.encodeInt(255);

        return outPacket;
    }

    public static OutPacket sendServerStatus() {
        OutPacket outPacket = new OutPacket(OutHeader.SERVER_STATUS.getValue());
        outPacket.encodeByte(0); // TODO: Implement Server Status (0 = Normal, 1 = Highly populated, 2 = Full)
        outPacket.encodeByte(0);
        return outPacket;
    }

    public static OutPacket sendAliveReq() {
        return new OutPacket(OutHeader.ALIVE_REQ.getValue());
    }

}
