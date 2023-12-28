package henesys.constants;

import henesys.jobs.explorer.Magician.*;
import henesys.jobs.explorer.Thief.*;
import henesys.jobs.explorer.Warrior.*;
public class SkillConstants {

    public static boolean isIgnoreMasterLevelForCommon(int skillId) {
        switch (skillId) {
            case Bishop.BUFF_MASTERY_BISHOP:
            case Hero.COMBAT_MASTERY:
            case Paladin.DIVINE_SHIELD:
            case NightLord.EXPERT_THROWING_STAR_HANDLING:
            // TODO: Add more skills here that ignore master level
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
