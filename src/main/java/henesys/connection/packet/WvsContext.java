package henesys.connection.packet;

import henesys.client.BroadcastMsg;
import henesys.client.character.ExtendSP;
import henesys.connection.OutPacket;
import henesys.enums.InvType;
import henesys.enums.InventoryOperation;
import henesys.enums.Stat;
import henesys.handlers.header.OutHeader;
import henesys.items.Equip;
import henesys.items.Item;
import henesys.skills.Skill;

import java.util.*;

public class WvsContext {

    public static OutPacket statChanged(Map<Stat, Object> stats, boolean exclRequestSent) {
        OutPacket outPacket = new OutPacket(OutHeader.STAT_CHANGED);
        outPacket.encodeByte(exclRequestSent);

        int mask = 0;
        for (Stat stat : stats.keySet()) {
            mask |= stat.getVal();
        }
        outPacket.encodeInt(mask);
        // sorts stats by their order in the Stat enum
        Comparator<Object> statComper = Comparator.comparingInt(o -> ((Stat) o).getVal());
        TreeMap<Stat, Object> sortedStats = new TreeMap<>(statComper);
        sortedStats.putAll(stats);
        for (Map.Entry<Stat, Object> entry : sortedStats.entrySet()) {
            Stat stat = entry.getKey();
            Object value = entry.getValue();

            switch (stat) {
                case skin:
                case level:
                    outPacket.encodeByte((Byte) value);
                    break;
                case face:
                case hair:
                case hp:
                case mhp:
                case mp:
                case mmp:
                case pop:
                    outPacket.encodeInt((Integer) value);
                    break;
                case str:
                case dex:
                case inte:
                case luk:
                case ap:
                case subJob:
                    outPacket.encodeShort((Short) value);
                    break;
                case sp:
                    if (value instanceof ExtendSP) {
                        ((ExtendSP) value).encode(outPacket);
                    } else {
                        outPacket.encodeShort((Short) value);
                    }
                    break;
                case exp:
                case money:
                    outPacket.encodeLong((Long) value);
                    break;
            }
        }
        outPacket.encodeByte(0); // nMixBaseHairColor
        outPacket.encodeByte(0); // nMixAddHairColor
        return outPacket;
    }

    public static OutPacket inventoryOperation(boolean exclRequestSent, boolean notRemoveAddInfo, InventoryOperation type, short oldPos, short newPos,
                                               int bagPos, Item item) {
        // logic like this in packets :(
        InvType invType = item.getInvType();
        if ((oldPos > 0 && newPos < 0 && invType == InvType.EQUIPPED) || (invType == InvType.EQUIPPED && oldPos < 0)) {
            invType = InvType.EQUIP;
        }

        OutPacket outPacket = new OutPacket(OutHeader.INVENTORY_OPERATION);

        outPacket.encodeByte(exclRequestSent);
        outPacket.encodeByte(true); // context or notRemoveAddInfo

        outPacket.encodeByte(type.getVal());
        outPacket.encodeByte(invType.getVal());
        outPacket.encodeShort(oldPos);
        switch (type) {
            case Add:
            case BagNewItem:
                item.encode(outPacket);
                break;
            case UpdateQuantity:
                outPacket.encodeShort(item.getQuantity());
                break;
            case Move:
                outPacket.encodeShort(newPos);
                if (invType == InvType.EQUIP && (oldPos < 0 || newPos < 0)) {
                    outPacket.encodeByte(item.getCashItemSerialNumber() > 0);
                }
                break;
            case Remove:
                outPacket.encodeByte(0);
                break;
            case ItemExp:
                outPacket.encodeLong(((Equip) item).getExp());
                break;
            case UpdateBagPos:
                outPacket.encodeInt(bagPos);
                break;
            case UpdateBagQuantity:
                outPacket.encodeShort(newPos);
                break;
            case BagRemoveSlot:
                break;
        }
        return outPacket;
    }

    public static OutPacket changeSkillRecordResult(Skill skill) {
        List<Skill> skills = new ArrayList<>();
        skills.add(skill);
        return changeSkillRecordResult(skills, true, true);
    }
    public static OutPacket changeSkillRecordResult(List<Skill> skills, boolean exclRequestSent, boolean sn) {
        OutPacket outPacket = new OutPacket(OutHeader.CHANGE_SKILL_RECORD_RESULT);

        outPacket.encodeByte(exclRequestSent);
        outPacket.encodeShort(skills.size());
        skills.forEach((skill) -> skill.encode(outPacket));
        outPacket.encodeByte(sn);

        return outPacket;
    }

    public static OutPacket broadcastMsg(BroadcastMsg broadcastMsg) {
        OutPacket outPacket = new OutPacket(OutHeader.BROADCAST_MSG);

        broadcastMsg.encode(outPacket);

        return outPacket;
    }
}
