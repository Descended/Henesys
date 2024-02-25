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

    public static boolean isKeyDownSkill(int skillId) {
        return skillId == 2321001 || skillId == 80001836 || skillId == 37121052 || skillId == 36121000 ||
                skillId == 37121003 || skillId == 36101001 || skillId == 33121114 || skillId == 33121214 ||
                skillId == 35121015 || skillId == 33121009 || skillId == 32121003 || skillId == 31211001 ||
                skillId == 31111005 || skillId == 30021238 || skillId == 31001000 || skillId == 31101000 ||
                skillId == 80001887 || skillId == 80001880 || skillId == 80001629 || skillId == 20041226 ||
                skillId == 60011216 || skillId == 65121003 || skillId == 80001587 || skillId == 131001008 ||
                skillId == 142111010 || skillId == 131001004 || skillId == 95001001 || skillId == 101110100 ||
                skillId == 101110101 || skillId == 101110102 || skillId == 27111100 || skillId == 12121054 ||
                skillId == 11121052 || skillId == 11121055 || skillId == 5311002 || skillId == 4341002 ||
                skillId == 5221004 || skillId == 5221022 || skillId == 3121020 || skillId == 3101008 ||
                skillId == 3111013 || skillId == 1311011 || skillId == 2221011 || skillId == 2221052 ||
                skillId == 25121030 || skillId == 27101202 || skillId == 25111005 || skillId == 23121000 ||
                skillId == 22171083 || skillId == 14121004 || skillId == 13111020 || skillId == 13121001 ||
                skillId == 14111006 || (skillId >= 80001389 && skillId <= 80001392) || skillId == 42121000 ||
                skillId == 42120003 || skillId == 5700010 || skillId == 5711021 || skillId == 5721001 ||
                skillId == 5721061 || skillId == 21120018 || skillId == 21120019 || skillId == 24121000 ||
                skillId == 24121005;
    }
}
