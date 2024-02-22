package henesys.jobs.cygnus;

import henesys.jobs.Job;

public class Noblesse implements Job {
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
