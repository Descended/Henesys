package henesys.jobs.explorer;

import henesys.client.Client;
import henesys.client.character.skills.info.SkillInfo;
import henesys.client.character.skills.temp.TemporaryStatManager;
import henesys.jobs.Job;
import henesys.loaders.SkillData;
import henesys.skills.Option;
import henesys.skills.Skill;

public class Beginner implements Job {

    public static final int FOLLOW_THE_LEAD = 8;
    public static final int BLESSING_OF_THE_FAIRY = 12;
    public static final int THREE_SNAILS = 1000;
    public static final int RECOVERY = 1001;
    public static final int NIMBLE_FEET = 1002;
    public static final int LEGENDARY_SPIRIT = 1003;
    public static final int MONSTER_RIDER = 1004;
    public static final int ECHO_OF_HERO = 1005;
    public static final int TEST_SKILL = 1006;
    public static final int MAKER = 1007;
    public static final int BAMBOO_RAIN = 1009;
    public static final int INVINCIBILITY = 1010;
    public static final int POWER_EXPLOSION = 1011;
    public static final int SPACESHIP = 1013;
    public static final int SPACE_DASH = 1014;
    public static final int SPACE_BEAM = 1015;
    public static final int YETI_RIDER = 1017;
    public static final int YETI_MOUNT = 1018;
    public static final int WITCH_BROOMSTICK = 1019;
    public static final int RAGE_OF_PHARAOH = 1020;
    public static final int WOODEN_PONY = 1025;
    public static final int SOARING = 1026;
    public static final int CROCO = 1027;
    public static final int BLACK_SCOOTER = 1028;
    public static final int PINK_SCOOTER = 1029;
    public static final int NIMBUS_CLOUD = 1030;
    public static final int BALROG = 1031;
    public static final int BALROG2 = 1032;
    public static final int RACECAR = 1033;
    public static final int ZD_TIGER = 1034;
    public static final int MIST_BALROG = 1035;
    public static final int LION = 1036;
    public static final int UNICORN = 1037;
    public static final int LOW_RIDER = 1038;
    public static final int RED_TRUCK = 1039;
    public static final int GARGOYLE = 1040;
    public static final int SHINJO = 1042;
    public static final int ORANGE_MUSHROOM = 1044;
    public static final int SPACESHIP2 = 1046;
    public static final int SPACE_DASH2 = 1047;
    public static final int SPACE_BEAM2 = 1048;
    public static final int NIGHTMARE = 1049;
    public static final int YETI = 1050;
    public static final int OSTRICH = 1051;
    public static final int PINK_BEAR_BALLOON = 1052;
    public static final int TRANSFORMATION_ROBOT = 1053;
    public static final int CHICKEN = 1054;
    public static final int MOTORCYCLE = 1063;
    public static final int POWER_SUIT = 1064;
    public static final int OS4_SHUTTLE = 1065;
    public static final int VISITOR_MELEE = 1066;
    public static final int VISITOR_RANGE = 1067;
    public static final int OWL = 1069;
    public static final int MOTHERSHIP = 1070;
    public static final int OS3A_MACHINE = 1071;
    public static final int DECENT_HASTE = 8000;
    public static final int DECENT_MYSTIC_DOOR = 8001;
    public static final int DECENT_SHARP_EYES = 8002;
    public static final int DECENT_HYPER_BODY = 8003;

    @Override
    public void handleSkill(Client c, int skillId, byte skillLevel) {
        Option o1 = new Option();
        Option o2 = new Option();
        Skill skill = SkillData.getSkillDeepCopyById(skillId);
        SkillInfo si = null;
        TemporaryStatManager tsm = c.getChr().getTemporaryStatManager();
        if(skill != null) {
            si = SkillData.getSkillInfoById(skillId);
        }
        switch (skillId) {
            case NIMBLE_FEET:
                o1.nOption = 5 + 5 * skillLevel;
                o1.rOption = skillId;
                o1.tOption = 4 * skillLevel;
                break;
            case RECOVERY:
                break;
            default:
                break;
        }

    }

    @Override
    public void handleAttack(Client c, int skillId, byte skillLevel) {

    }

    @Override
    public void handleBuff(Client c, int skillId, byte skillLevel) {

    }


    @Override
    public boolean isHandlerOfSkill(int skillId) {
        return false;
    }

}
