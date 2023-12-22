package henesys.constants;

import henesys.jobs.explorer.Bishop;
import henesys.jobs.explorer.Hero;
import henesys.jobs.explorer.NightLord;
import henesys.jobs.explorer.Paladin;

public class SkillConstants {

    public static boolean isIgnoreMasterLevelForCommon(int skillId) {
        switch (skillId) {
            case Bishop.BUFF_MASTERY:
            case Hero.COMBAT_MASTERY:
            case Paladin.DIVINE_SHIELD:
            case NightLord.EXPERT_THROWING_STAR_HANDLING:
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
