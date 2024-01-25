package henesys.handlers.user;

import henesys.client.Client;
import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.connection.InPacket;
import henesys.connection.packet.WvsContext;
import henesys.enums.Stat;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;

import java.util.HashMap;
import java.util.Map;

public class UserStatHandler {

    @Handler(op = InHeader.USER_ABILITY_UP_REQUEST)
    public static void handleUserAbilityUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        CharacterStat cs = chr.getCharacterStat();
        if (cs.getAp() <= 0) { // If the player has no AP, return.
            return;
        }
        inPacket.decodeInt(); // tick
        short stat = inPacket.decodeShort();
        Stat charStat = Stat.getByVal(stat);
        short amount = 1;
        boolean isHpOrMp = false;
        if (charStat == Stat.mmp || charStat == Stat.mhp) {
            isHpOrMp = true;
            amount = 20;
        }
        chr.addStat(charStat, amount);
        cs.setAp((short) (cs.getAp() -1));
        Map<Stat, Object> stats = new HashMap<>();
        if (isHpOrMp) {
            stats.put(charStat, chr.getStat(charStat));
        } else {
            stats.put(charStat, (short) chr.getStat(charStat));
        }
        stats.put(Stat.ap, (short) chr.getStat(Stat.ap));
        c.write(WvsContext.statChanged(stats, true));
    }
}
