package henesys.constants;

import henesys.jobs.explorer.*;

public class SkillConstants {

    public static boolean isIgnoreMasterLevelForCommon(int skillId) {
        switch (skillId) {
            case Magician.BUFF_MASTERY_BISHOP:
            case Warrior.COMBAT_MASTERY:
            case Warrior.DIVINE_SHIELD:
            case Thief.EXPERT_THROWING_STAR_HANDLING:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSkillNeedMasterLevel(int skillId) {
        if (isIgnoreMasterLevelForCommon(skillId)) {
            return true;
        }
        return false;
    }
}
