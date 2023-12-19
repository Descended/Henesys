package henesys.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created on 12/22/2017.
 */
public enum Stat {
    skin(0x1),
    face(0x2),
    hair(0x4),
    level(0x10),
    subJob(0x20),
    str(0x40),
    dex(0x80),
    inte(0x100),
    luk(0x200),
    hp(0x400),
    mhp(0x800),
    mp(0x1000),
    mmp(0x2000),
    ap(0x4000),
    sp(0x8000),
    exp(0x10000),
    pop(0x20000),
    money(0x40000),
    tempExp(0x200000),
    ;

    private final int val;

    Stat(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static Stat getByVal(int stat) {
        return Arrays.stream(values()).filter(s -> s.getVal() == stat).findFirst().orElse(null);
    }

    public static List<Stat> getStatsByFlag(int mask) {
        List<Stat> stats = new ArrayList<>();
        List<Stat> allStats = Arrays.asList(values());
        Collections.sort(allStats);
        for (Stat stat : allStats) {
            if ((stat.getVal() & mask) != 0) {
                stats.add(stat);
            }
        }
        return stats;
    }
}
