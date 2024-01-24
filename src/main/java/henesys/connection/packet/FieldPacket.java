package henesys.connection.packet;

import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.connection.OutPacket;
import henesys.constants.ItemConstants;
import henesys.handlers.header.OutHeader;
import henesys.items.BodyPart;
import henesys.items.Equip;
import henesys.items.Item;

import java.util.List;

public class FieldPacket {

    public static OutPacket characterInfo(Char chr) {
        OutPacket outPacket = new OutPacket(OutHeader.CHARACTER_INFO);
        CharacterStat cs = chr.getCharacterStat();
        outPacket.encodeInt(chr.getId());
        outPacket.encodeByte(cs.getLevel());
        outPacket.encodeShort(cs.getJob());
        outPacket.encodeShort(cs.getFame());
        outPacket.encodeByte(0); // Marriage ring

//        outPacket.encodeString(chr.getGuild() == null ? "-" : chr.getGuild().getName());
//        outPacket.encodeString(chr.getGuild() == null || chr.getGuild().getAlliance() == null ? "-" :
//                chr.getGuild().getAlliance().getName());
        // Guild name and Alliance name
        outPacket.encodeString("");
        outPacket.encodeString("");

        outPacket.encodeByte(0); // pMedalInfo

//        for (Pet pet : chr.getPets()) {
//            outPacket.encodeByte(1); TODO
//        }

        outPacket.encodeByte(0); // CUIUserInfo::SetPetInfo end

        // Mount
        // TODO: if(chr.getMount() != null)
        outPacket.encodeByte(0);

        outPacket.encodeByte(0); // Wish List Size
//        for (items in wishlist) {
//            outPacket.encodeInt(); // Item ID
//        }

        Equip medal = (Equip) chr.getEquippedItemByBodyPart(BodyPart.Medal);
        outPacket.encodeInt(medal == null ? 0 : medal.getItemId());
        outPacket.encodeShort(0); // medal size

        //Chairs
        List<Integer> chairs = chr.getInstallInventory().getItems().stream().filter(i ->
                (ItemConstants.isChair(i.getItemId()))).mapToInt(Item::getItemId).boxed().toList();
        outPacket.encodeInt(chairs.size()); //chair amount(size)
        for (int itemId : chairs) {
            outPacket.encodeInt(itemId);
        }
        return outPacket;
    }
}
