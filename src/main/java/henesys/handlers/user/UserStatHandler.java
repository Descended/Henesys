package henesys.handlers.user;

import henesys.client.Client;
import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.client.character.ExtendSP;
import henesys.connection.InPacket;
import henesys.connection.packet.WvsContext;
import henesys.constants.GameConstants;
import henesys.constants.JobConstants;
import henesys.constants.SkillConstants;
import henesys.enums.Stat;
import henesys.handlers.Handler;
import henesys.handlers.header.InHeader;
import henesys.loaders.SkillData;
import henesys.skills.Skill;

import java.util.HashMap;
import java.util.Map;

public class UserStatHandler {

    @Handler(op = InHeader.USER_ABILITY_UP_REQUEST)
    public static void handleUserAbilityUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        CharacterStat cs = chr.getCharacterStat();
        if (cs.getAp() <= 0) { // If the player has no AP, return.
            chr.dispose();
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
        cs.addStat(charStat, amount);
        cs.setAp((short) (cs.getAp() -1));
        Map<Stat, Object> stats = new HashMap<>();
        if (isHpOrMp) {
            stats.put(charStat, cs.getStat(charStat));
        } else {
            stats.put(charStat, (short) cs.getStat(charStat));
        }
        stats.put(Stat.ap, (short) cs.getStat(Stat.ap));
        c.write(WvsContext.statChanged(stats, true));
    }

    @Handler(op = InHeader.USER_SKILL_UP_REQUEST)
    public static void handleUserSkillUpRequest(Client c, InPacket inPacket) {
        Char chr = c.getChr();
        CharacterStat cs = chr.getCharacterStat();
        inPacket.decodeInt(); // tick
        int skillID = inPacket.decodeInt();
        int amount = 1;
        // separate skill/current skills for adding stuff to the base cache if everything is successful
        Skill skill = SkillData.getSkillDeepCopyById(skillID);
        Skill curSkill = chr.getSkill(skillID);
        byte jobLevel = (byte) JobConstants.getJobLevel((short) skill.getRootId());
        Map<Stat, Object> stats;
        int rootId = skill.getRootId();
        boolean isBeginner = JobConstants.isBeginnerJob((short) rootId);
        boolean isMatching = SkillConstants.isMatching(rootId, cs.getJob());
        if (!isBeginner && !isMatching) {
//            chr.getOffenseManager().addOffense(String.format("Character %d tried adding an invalid skill (job %d, skill id %d)",
//                    chr.getId(), rootId, skillID));
            chr.dispose();
            return;
        }
        if (JobConstants.isBeginnerJob((short) rootId)) {
            stats = new HashMap<>();
            int spentSp = 0;
            for (Skill s : chr.getSkills()) {
                if (SkillConstants.isBeginnerSpAddableSkill(s.getSkillId())) {
                    int currentLevel = s.getCurrentLevel();
                    spentSp += currentLevel;
                }
            }
            int totalSp;
            if (JobConstants.isResistance((short) skill.getRootId())) {
                totalSp = Math.min(cs.getLevel(), GameConstants.RESISTANCE_SP_MAX_LV) - 1; // sp gained from 2~10
            } else {
                totalSp = Math.min(cs.getLevel(), GameConstants.BEGINNER_SP_MAX_LV) - 1; // sp gained from 2~7
            }
            if (totalSp - spentSp >= amount) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                if (max == 0) {
                    // some beginner skills have no max level, default is 3
                    max = 3;
                }
                int newLevel = Math.min(curLevel + amount, max);
                skill.setCurrentLevel(newLevel);
            }
        } else if (JobConstants.isExtendSpJob(cs.getJob())) {
            ExtendSP esp = cs.getExtendSP();
            int currentSp = esp.getSpByJobLevel(jobLevel);
            if (currentSp >= amount) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = Math.min(curLevel + amount, max);
                skill.setCurrentLevel(newLevel);
                esp.setSpToJobLevel(jobLevel, (byte) (currentSp - amount));
                stats = new HashMap<>();
                stats.put(Stat.sp, esp);
            } else {
//                chr.getOffenseManager().addOffense(String.format("Character %d tried adding a skill without having the required amount of sp"
//                                + " (required %d, has %d)",
//                        chr.getId(), amount, currentSp));
                return;
            }
        } else {
            int currentSp = cs.getSp();
            if (currentSp >= amount) {
                int curLevel = curSkill == null ? 0 : curSkill.getCurrentLevel();
                int max = curSkill == null ? skill.getMaxLevel() : curSkill.getMaxLevel();
                int newLevel = Math.min(curLevel + amount, max);
                skill.setCurrentLevel(newLevel);
                chr.getCharacterStat().setSp((short) (currentSp - amount));
                stats = new HashMap<>();
                stats.put(Stat.sp, cs.getSp());
            } else {
//                chr.getOffenseManager().addOffense(String.format("Character %d tried adding a skill without having the required amount of sp"
//                                + " (required %d, has %d)",
//                        chr.getId(), currentSp, amount));
                return;
            }
        }
        c.write(WvsContext.statChanged(stats, true));
        chr.addSkill(skill, false);
        c.write(WvsContext.changeSkillRecordResult(skill));
    }
}
