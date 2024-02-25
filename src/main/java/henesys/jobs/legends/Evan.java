package henesys.jobs.legends;

import henesys.client.Client;
import henesys.jobs.Job;

public class Evan implements Job {


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
