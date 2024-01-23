package henesys.client.character.skills.temp;

import henesys.client.character.Char;
import henesys.connection.OutPacket;
import henesys.skills.CharacterTemporaryStat;
import henesys.skills.Option;

import java.util.*;

public class TemporaryStatManager {

    private Char chr;
    private Map<CharacterTemporaryStat, List<Option>> currentStats = new HashMap<>();

    public TemporaryStatManager(Char chr) {
        this.chr = chr;
    }

    public int[] getMaskByCollection(Map<CharacterTemporaryStat, List<Option>> map) {
        int[] res = new int[CharacterTemporaryStat.length];
        for (int i = 0; i < res.length; i++) {
            for (CharacterTemporaryStat cts : map.keySet()) {
                if (cts.getPos() == i) {
                    res[i] |= cts.getVal();
                }
            }
        }
        return res;
    }

    public boolean hasStat(CharacterTemporaryStat cts) {
        return getCurrentStats().containsKey(cts);
    }
    public Option getOption(CharacterTemporaryStat cts) {
        if (hasStat(cts)) {
            return getCurrentStats().get(cts).get(0);
        }
        return new Option();
    }

    public void encodeForRemote(OutPacket outPacket, Map<CharacterTemporaryStat, List<Option>> collection) {
        int[] mask = getMaskByCollection(collection);
        for (int maskElem : mask) {
            outPacket.encodeInt(maskElem);
        }

        List<CharacterTemporaryStat> orderedAndFilteredCtsList = new ArrayList<>(collection.keySet()).stream()
                .filter(cts -> cts.getRemoteOrder() != -1)
                .sorted(Comparator.comparingInt(CharacterTemporaryStat::getRemoteOrder))
                .toList();

        for (CharacterTemporaryStat cts : orderedAndFilteredCtsList) {
            Option o = getOption(cts);
            if (cts.isRemoteEncode1()) {
                outPacket.encodeByte(o.nOption);
            } else if (cts.isRemoteEncode4()) {
                outPacket.encodeInt(o.nOption);
            } else {
                outPacket.encodeShort(o.nOption);
            }
            if (!cts.isNotEncodeReason()) {
                outPacket.encodeInt(o.rOption);
            }
        }
    }

    public Map<CharacterTemporaryStat, List<Option>> getCurrentStats() {
        return currentStats;
    }
}
