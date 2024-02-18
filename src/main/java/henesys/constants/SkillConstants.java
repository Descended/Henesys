package henesys.constants;

import henesys.client.character.skills.info.SkillInfo;
import henesys.jobs.explorer.Magician.*;
import henesys.jobs.explorer.Thief.*;
import henesys.jobs.explorer.Warrior.*;
import henesys.loaders.SkillData;

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

    public static boolean isPassiveSkill(int skillId) {
        SkillInfo si = SkillData.getSkillInfoById(skillId);
        return si != null && si.isPsd() && si.getPsdSkills().isEmpty();
    }
    public static boolean isMatching(int rootId, int job) {
        boolean matchingStart = job / 100 == rootId / 100;
        boolean matching = matchingStart;
        if (matchingStart && rootId % 100 != 0) {
            // job path must match
            matching = (rootId % 100) / 10 == (job % 100) / 10;
        }
        return matching;
    }
    public static boolean isSkillNeedMasterLevel(int skillId) {
        if (isIgnoreMasterLevelForCommon(skillId)) {
            return true;
        }
        return false;
    }

    public static boolean isBeginnerSpAddableSkill(int skillID) {
        return skillID == 1000 || skillID == 1001 || skillID == 1002;
    }
}
