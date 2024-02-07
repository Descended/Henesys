package henesys.connection.packet;


import henesys.connection.OutPacket;
import henesys.enums.MobControllerType;
import henesys.handlers.header.OutHeader;
import henesys.life.DeathType;
import henesys.life.mob.ForcedMobStat;
import henesys.life.mob.Mob;
import henesys.util.Position;

/**
 * Created on 2/28/2018.
 */
public class MobPool {
    public static OutPacket enterField(Mob mob) {
        OutPacket outPacket = new OutPacket(OutHeader.MOB_ENTER_FIELD);

        outPacket.encodeInt(mob.getObjectId());
        outPacket.encodeByte(1);
        outPacket.encodeInt(mob.getTemplateId());
        mob.encode(outPacket);
        return outPacket;
    }

    public static OutPacket mobChangeController(Mob mob, MobControllerType mobCtrlType) {
        OutPacket outPacket = new OutPacket(OutHeader.MOB_CHANGE_CONTROLLER);

        outPacket.encodeByte(mobCtrlType.getVal()); // 0 = None | 1 = Control | 2 = Aggro
        outPacket.encodeInt(mob.getObjectId());

        if(mobCtrlType.getVal() > MobControllerType.Reset.getVal()){
            outPacket.encodeByte(5); // nCalcDamageIndex | Controller
            outPacket.encodeInt(mob.getTemplateId());
            // Encode mob data -
            mob.encode(outPacket);
        }
        return outPacket;
    }
    public static OutPacket leaveField(int id, DeathType deadType) {
        OutPacket outPacket = new OutPacket(OutHeader.MOB_LEAVE_FIELD);

        outPacket.encodeInt(id);
        outPacket.encodeByte(deadType.getVal());

        return outPacket;
    }
}
