package henesys.jobs.resistance;

import henesys.jobs.Job;

public class Citizen implements Job {
    public static final int CRYSTAL_THROW = 30001000;
    public static final int INFLITRATE = 30001001;
    public static final int POTION_MASTERY = 30000002;

    @Override
    public void handleSkill(int skillId) {

    }

    @Override
    public void handleAttack(int skillId) {

    }

    @Override
    public void handleBuff(int skillId) {

    }

    @Override
    public boolean isHandlerOfSkill(int skillId) {
        return false;
    }
}
