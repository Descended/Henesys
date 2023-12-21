package henesys.connection.packet;

import henesys.ServerConstants;
import henesys.client.Account;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.client.dao.AvatarLookDao;
import henesys.client.dao.CharDao;
import henesys.client.dao.CharacterStatDao;
import henesys.connection.OutPacket;
import henesys.enums.LoginType;
import henesys.handlers.header.OutHeader;
import henesys.util.Position;
import henesys.util.container.Tuple;
import henesys.world.Channel;
import henesys.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Desc
 */
public class Login {

    /**
     * Sends the connect packet to the client. This is called when the client connects to the server.
     * @param siv The send IV.
     * @param riv The receive IV.
     * @return The OutPacket containing the connect packet.
     */
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

    /**
     * Sends the login result to the client. This is called when the client sends a login request packet.
     * @param loginType The login type.
     * @param user The user.
     * @return The OutPacket containing the login result.
     */
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

    /**
     * Sends the world information to the client. This is called when the client sends a World Request packet. This
     * happens when the User has successfully logged in. Each world gets its own World Information packet.
     * @param world The world to send the information of.
     * @param stringInfos The balloon information to send.
     * @return The OutPacket containing the world information.
     */
    public static OutPacket sendWorldInformation(World world, Set<Tuple<Position, String>> stringInfos) {
        // CLogin::OnWorldInformation
        OutPacket outPacket = new OutPacket(OutHeader.WORLD_INFORMATION.getValue());
        outPacket.encodeByte(world.getWorldId());
        outPacket.encodeString(world.getName());
        outPacket.encodeByte(world.getWorldState());
        outPacket.encodeString(world.getWorldEventDescription());
        outPacket.encodeShort(world.getWorldEventEXP_WSE());
        outPacket.encodeShort(world.getWorldEventDrop_WSE());
        outPacket.encodeByte(world.isCharCreateBlock());
        outPacket.encodeByte(world.getChannels().size());
        for (Channel channel : world.getChannels()) {
            outPacket.encodeString(channel.getName());
            outPacket.encodeInt(channel.getGaugePx());
            outPacket.encodeByte(channel.getWorldId());
            outPacket.encodeByte(channel.getChannelId());
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

    /**
     * Lets the client know that the world information has ended.
     * @return The OutPacket containing the world information.
     */
    public static OutPacket sendWorldInformationEnd() {
        OutPacket outPacket = new OutPacket(OutHeader.WORLD_INFORMATION);

        outPacket.encodeInt(255);

        return outPacket;
    }

    /**
     * Sends the server status to the client. This is called when the client clicks on a world in the world selection.
     * @return The OutPacket containing the server status.
     */
    public static OutPacket sendServerStatus() {
        OutPacket outPacket = new OutPacket(OutHeader.SERVER_STATUS.getValue());
        outPacket.encodeByte(0); // TODO: Implement Server Status (0 = Normal, 1 = Highly populated, 2 = Full)
        outPacket.encodeByte(0);
        return outPacket;
    }

    public static OutPacket selectWorldResult(User user, Account account) {
        OutPacket outPacket = new OutPacket(OutHeader.SELECT_WORLD_RESULT.getValue());
        outPacket.encodeByte(0); // status/nSuccess
        CharDao charDao = new CharDao();
        CharacterStatDao characterStatDao = new CharacterStatDao();
        AvatarLookDao avatarLookDao = new AvatarLookDao();
        List<Char> chars = charDao.getCharsByAccountId(account.getId());
        outPacket.encodeByte(chars.size());
        for (Char chr : chars) {
            chr.setCharacterStat(characterStatDao.findById(chr.getId(), chr.getCharacterStatId()));
            chr.getCharacterStat().encode(outPacket);
            chr.setAvatarLook(avatarLookDao.findById(chr.getId(), chr.getAvatarLookId()));
            chr.getAvatarLook().encode(outPacket);
            outPacket.encodeByte(false); // family stuff, deprecated
            boolean hasRanking = false;
            outPacket.encodeByte(hasRanking);
            if (hasRanking) {
                outPacket.encodeInt(0); // world rank
                outPacket.encodeInt(0); // getTotRankGap
                outPacket.encodeInt(0); // Job Rank
                outPacket.encodeInt(0); // gap
            }
        }
        outPacket.encodeByte(user.getPicStatus().getVal()); // bLoginOpt
        outPacket.encodeInt(user.getCharacterSlots());
        outPacket.encodeInt(0); // Buying char slots (m_nBuyCharCount)

        return outPacket;
    }

    public static OutPacket sendAliveReq() {
        return new OutPacket(OutHeader.ALIVE_REQ.getValue());
    }

    public static OutPacket checkDuplicatedIDResult(String name, byte code) {
        OutPacket outPacket = new OutPacket(OutHeader.CHECK_DUPLICATED_ID_RESULT);

        outPacket.encodeString(name);
        outPacket.encodeByte(code);

        return outPacket;
    }

    public static OutPacket createNewCharacterResult(LoginType type, Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.CREATE_NEW_CHARACTER_RESULT);

        outPacket.encodeByte(type.getValue());
        if (type == LoginType.Success) {
            chr.getCharacterStat().encode(outPacket);
            chr.getAvatarLook().encode(outPacket);

            outPacket.encodeByte(false);
            outPacket.encodeByte(false);
        }

        return outPacket;
    }
}
