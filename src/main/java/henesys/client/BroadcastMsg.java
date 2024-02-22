package henesys.client;

import henesys.connection.OutPacket;
import henesys.enums.BroadcastMsgType;
import henesys.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asura on 17-6-2018.
 */
public class BroadcastMsg {
    BroadcastMsgType broadcastMsgType;
    Item item;
    String string;
    String string2;
    String string3;
    int arg1;
    int arg2;
    int arg3;

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getBroadcastMsgType().getVal());

        outPacket.encodeString(getString());

        switch (getBroadcastMsgType()) {
            case Notice:
            case PopUpMessage:
            case DarkBlueOnLightBlue:
            case PartyChat:
            case WhiteYellow:
            case SwedishFlag:
                break;
            case Megaphone:
            case MegaphoneNoMessage:
                outPacket.encodeByte(getArg1()); // Channel
                outPacket.encodeByte(getArg2()); // Mega Ear
                break;
            case ItemMegaphone:
                outPacket.encodeByte(getArg1()); // Channel
                outPacket.encodeByte(getArg2()); // Mega Ear
                outPacket.encodeByte(getArg3()); // Boolean  Item: Yes/No
                if(getArg3() != 0) {
                    getItem().encode(outPacket); // Item encode
                }
                break;
            case TripleMegaphone:
                outPacket.encodeByte(getArg1()); // StringList size
                if(getArg1() > 1) {
                    outPacket.encodeString(getString2()); // String 2
                }
                if(getArg1() > 2) {
                    outPacket.encodeString(getString3()); // String 3
                }
                outPacket.encodeByte(getArg2()); // Channel
                outPacket.encodeByte(getArg3()); // Mega Ear
                break;
            case BlueChat_ItemInfo:
            case BlueChat_ItemInfo_2:
                outPacket.encodeInt(getArg1()); // item Id
                if(getArg1() != 0) {
                    getItem().encode(outPacket); // item encode
                }
                break;
            case GM_ErrorMessage:
                outPacket.encodeInt(getArg1()); // npc Id
                break;
            case RedWithChannelInfo:
                outPacket.encodeInt(getArg1()); //  chr Id
                // "#channel" will grab  Chr's  Channel
                break;
            case WhiteYellow_ItemInfo:
                outPacket.encodeByte(getArg1()); // Boolean  Item: Yes/No
                if(getArg1() != 0) {
                    getItem().encode(outPacket); // Item encode
                }
                break;
            case YellowChatFiled_ItemInfo:
                outPacket.encodeInt(getArg1()); // item Id
                outPacket.encodeByte(getArg2()); // boolean: show item
                getItem().encode(outPacket);
                break;
            case PopUpNotice:
                outPacket.encodeInt(getArg1()); // width
                outPacket.encodeInt(getArg2()); // height
                break;
            case Yellow:
            case Yellow_2:
                getItem().encode(outPacket); // Item encode
                break;
            case TryRegisterAutoStartQuest:
                outPacket.encodeInt(getArg1()); // Quest Id
                outPacket.encodeInt(getArg2()); // Time Out
                break;
            case TryRegisterAutoStartQuest_NoAnnouncement:
                outPacket.encodeInt(getArg1()); // Quest Id
                break;
            case SlideNotice:
                outPacket.encodeByte(getArg1());
                break;
        }
    }

    public static String formatMegaphoneStrings(String medalName, String characterName, String message) {
        if (!medalName.isEmpty()) {
            medalName = String.format("<%s> ", medalName);
        }
        return String.format("%s%s : %s", medalName, characterName, message);
    }

    public static BroadcastMsg itemMegaphone(String medalName, String characterName, String message, byte channel, boolean whisperEar, boolean containsItem, Item item) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.ItemMegaphone);

        broadcastMsg.setString(formatMegaphoneStrings(medalName, characterName, message));
        broadcastMsg.setArg1((byte) (channel - 1));
        broadcastMsg.setArg2((byte) (whisperEar ? 1 : 0));
        broadcastMsg.setArg3((byte) (containsItem ? 1 : 0));
        broadcastMsg.setItem(item);

        return broadcastMsg;
    }

    public static BroadcastMsg tripleMegaphone(String medalName, String characterName, List<String> stringList, byte channel, boolean whisperEar) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.TripleMegaphone);

        System.out.println("Pre-formatting: \n" + stringList);
        List<String> formattedMessage = new ArrayList<>();
        for (String line : stringList) {
            formattedMessage.add(formatMegaphoneStrings(medalName, characterName, line));
        }
        System.out.println("Post-formatting: \n" + formattedMessage);

        broadcastMsg.setArg1((byte) formattedMessage.size());
        broadcastMsg.setString(formattedMessage.get(0));
        if(formattedMessage.size() > 1) {
            broadcastMsg.setString2(formattedMessage.get(1));
        }
        if(formattedMessage.size() > 2) {
            broadcastMsg.setString3(formattedMessage.get(2));
        }
        broadcastMsg.setArg2((byte) (channel - 1));
        broadcastMsg.setArg3((byte) (whisperEar ? 1 : 0));

        return broadcastMsg;
    }

    public static BroadcastMsg megaphone(String medalName, String characterName, String message, byte channel, boolean whisperEar) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.Megaphone);

        broadcastMsg.setString(formatMegaphoneStrings(medalName, characterName, message));
        broadcastMsg.setArg1((byte) (channel - 1));
        broadcastMsg.setArg2((byte) (whisperEar ? 1 : 0));

        return broadcastMsg;
    }

    public static BroadcastMsg notice(String string) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.Notice);

        broadcastMsg.setString(string);

        return broadcastMsg;
    }

    public static BroadcastMsg popUpMessage(String string) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.PopUpMessage);

        broadcastMsg.setString(string);

        return broadcastMsg;
    }

    public static BroadcastMsg popUpNotice(String string, int width, int height) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.PopUpNotice);

        broadcastMsg.setString(string);
        broadcastMsg.setArg1(width);
        broadcastMsg.setArg2(height);

        return broadcastMsg;
    }

    public static BroadcastMsg blueChatWithItemInfo(String string, Item item) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.BlueChat_ItemInfo);

        broadcastMsg.setString(string);
        broadcastMsg.setArg1(item.getItemId());
        broadcastMsg.setItem(item);

        return broadcastMsg;
    }

    public static BroadcastMsg blueChatWithItemInfo2(String string, Item item) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.BlueChat_ItemInfo_2);

        broadcastMsg.setString(string);
        broadcastMsg.setArg1(item.getItemId());
        broadcastMsg.setItem(item);

        return broadcastMsg;
    }

    public static BroadcastMsg errorMessage(String string, int npcId) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.GM_ErrorMessage);

        broadcastMsg.setString(string);
        broadcastMsg.setArg1(npcId);

        return broadcastMsg;
    }

    public static BroadcastMsg whiteYellowItemInfo(String string, Item item, boolean containsItem) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.WhiteYellow_ItemInfo);

        broadcastMsg.setString(string);
        broadcastMsg.setItem(item);
        broadcastMsg.setArg1(containsItem ? 1 : 0);
        return broadcastMsg;
    }

    public static BroadcastMsg yellowFilled(String string, Item item, boolean show) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.YellowChatFiled_ItemInfo);

        broadcastMsg.setString(string);
        broadcastMsg.setItem(item);
        broadcastMsg.setArg1(item.getItemId());
        broadcastMsg.setArg2(show ? 1 : 0);

        return broadcastMsg;
    }

    public static BroadcastMsg yellow(String string, Item item) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.Yellow);

        broadcastMsg.setString(string);
        broadcastMsg.setItem(item);
        return broadcastMsg;
    }

    public static BroadcastMsg yellow2(String string, Item item) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.Yellow_2);

        broadcastMsg.setString(string);
        broadcastMsg.setItem(item);
        return broadcastMsg;
    }
  /*
    public static BroadcastMsg slideNotice(String string, boolean show) {
        BroadcastMsg broadcastMsg = new BroadcastMsg();
        broadcastMsg.setBroadcastMsgType(BroadcastMsgType.SlideNotice);

        broadcastMsg.setString(string);
        broadcastMsg.setArg1(show ? 1 : 0);

        return broadcastMsg;
    }
*/

    public BroadcastMsgType getBroadcastMsgType() {
        return broadcastMsgType;
    }

    public void setBroadcastMsgType(BroadcastMsgType broadcastMsgType) {
        this.broadcastMsgType = broadcastMsgType;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public int getArg3() {
        return arg3;
    }

    public void setArg3(int arg3) {
        this.arg3 = arg3;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
