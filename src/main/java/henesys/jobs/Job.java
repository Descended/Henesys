package henesys.jobs;

import henesys.client.Client;

public interface Job {

    void handleSkill(Client c, int skillId, byte skillLevel);

    void handleAttack(Client c, int skillId, byte skillLevel);

    void handleBuff(Client c, int skillId, byte skillLevel);

    boolean isHandlerOfSkill(int skillId);
}
