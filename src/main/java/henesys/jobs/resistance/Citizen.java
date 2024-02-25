package henesys.jobs.resistance;

import henesys.client.Client;
import henesys.jobs.Job;

public class Citizen implements Job {
    public static final int CRYSTAL_THROW = 30001000;
    public static final int INFLITRATE = 30001001;
    public static final int POTION_MASTERY = 30000002;


    @Override
    public void handleSkill(Client c, int skillId, byte skillLevel) {

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
