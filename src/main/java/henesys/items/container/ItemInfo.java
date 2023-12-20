package henesys.items.container;


import henesys.enums.BaseStat;
import henesys.enums.InvType;
import henesys.enums.ScrollStat;
import henesys.enums.SpecStat;
import henesys.items.Item;
import henesys.loaders.ItemData;

import java.util.*;

/**
 * Created on 1/9/2018.
 */
public class ItemInfo {
    private int itemId;
    private InvType invType;
    private boolean cash;
    private int price;
    private int slotMax = 200;
    private boolean tradeBlock;
    private boolean notSale;
    private String path = "";
    private boolean noCursed;
    private Map<ScrollStat, Integer> scrollStats = new HashMap<>();

    private Map<SpecStat, Integer> specStats = new HashMap<>();

    private int bagType;
    private boolean quest;
    private int reqQuestOnProgress;
    private Set<Integer> questIDs = new HashSet<>();
    private int mobID;
    private int mobHP;
    private int createID;
    private int npcID;
    private int linkedID;
    private boolean monsterBook;
    private boolean notConsume;
    private String script = "";
    private int scriptNPC;
    private int life;
    private int masterLv;
    private int reqSkillLv;
    private Set<Integer> skills = new HashSet<>();
    private int moveTo;
    private Set<ItemRewardInfo> itemRewardInfos = new HashSet<>();
    private int skillId;
    private Set<Integer> reqItemIds = new HashSet<>();
    private boolean sample;

    public Set<Integer> getReqItemIds() {
        return reqItemIds;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setInvType(InvType invType) {
        this.invType = invType;
    }

    public InvType getInvType() {
        return invType;
    }

    public void setCash(boolean cash) {
        this.cash = cash;
    }

    public boolean isCash() {
        return cash;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setSlotMax(int slotMax) {
        this.slotMax = slotMax;
    }

    public int getSlotMax() {
        return slotMax;
    }

    public void setTradeBlock(boolean tradeBlock) {
        this.tradeBlock = tradeBlock;
    }

    public boolean isTradeBlock() {
        return tradeBlock;
    }

    public void setNotSale(boolean notSale) {
        this.notSale = notSale;
    }

    public boolean isNotSale() {
        return notSale;
    }

    public boolean isChair() {
        return getItemId() >= 3010000 && getItemId() <= 3020000;
    }

    public boolean isDamageSkin() {
        return isNotConsume() && isNotSale() && hasSample();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setNoCursed(boolean noCursed) {
        this.noCursed = noCursed;
    }

    public boolean isNoCursed() {
        return noCursed;
    }

    public Map<ScrollStat, Integer> getScrollStats() {
        return scrollStats;
    }

    public void setScrollStats(Map<ScrollStat, Integer> scrollStats) {
        this.scrollStats = scrollStats;
    }

    public void putScrollStat(ScrollStat scrollStat, int val) {
        getScrollStats().put(scrollStat, val);
    }

    public void setBagType(int bagType) {
        this.bagType = bagType;
    }

    public int getBagType() {
        return bagType;
    }

    public void setQuest(boolean quest) {
        this.quest = quest;
    }

    public boolean isQuest() {
        return quest;
    }

    public void setReqQuestOnProgress(int reqQuestOnProgress) {
        this.reqQuestOnProgress = reqQuestOnProgress;
    }

    public int getReqQuestOnProgress() {
        return reqQuestOnProgress;
    }
    public void addQuest(int questID) {
        getQuestIDs().add(questID);
    }

    public Set<Integer> getQuestIDs() {
        return questIDs;
    }

    public void setMobID(int mobID) {
        this.mobID = mobID;
    }

    public int getMobID() {
        return mobID;
    }

    public void setCreateID(int createID) {
        this.createID = createID;
    }

    public int getCreateID() {
        return this.createID;
    }

    public void setMobHP(int mobHP) {
        this.mobHP = mobHP;
    }

    public int getMobHP() {
        return mobHP;
    }

    public void setNpcID(int npcID) {
        this.npcID = npcID;
    }

    public int getNpcID() {
        return npcID;
    }

    public void setLinkedID(int linkedID) {
        this.linkedID = linkedID;
    }

    public int getLinkedID() {
        return linkedID;
    }

    public void setMonsterBook(boolean monsterBook) {
        this.monsterBook = monsterBook;
    }

    public boolean isMonsterBook() {
        return monsterBook;
    }

    public void setNotConsume(boolean notConsume) {
        this.notConsume = notConsume;
    }

    public boolean isNotConsume() {
        return notConsume;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }

    public void setScriptNPC(int scriptNPC) {
        this.scriptNPC = scriptNPC;
    }

    public int getScriptNPC() {
        return scriptNPC;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public int getMasterLv() {
        return masterLv;
    }

    public void setMasterLv(int masterLv) {
        this.masterLv = masterLv;
    }

    public int getReqSkillLv() {
        return reqSkillLv;
    }

    public void setReqSkillLv(int reqSkillLv) {
        this.reqSkillLv = reqSkillLv;
    }

    public Set<Integer> getSkills() {
        return skills;
    }

    public void setSkills(Set<Integer> skills) {
        this.skills = skills;
    }

    public void addSkill(int skill) {
        skills.add(skill);
    }

    public int getMoveTo() {
        return moveTo;
    }

    public void setMoveTo(int moveTo) {
        this.moveTo = moveTo;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public void addItemReward(ItemRewardInfo iri) {
        getItemRewardInfos().add(iri);
    }

    public Set<ItemRewardInfo> getItemRewardInfos() {
        return itemRewardInfos;
    }

    public Item getRandomReward() {
        List<ItemRewardInfo> iris = new ArrayList<>(getItemRewardInfos());
        iris.sort(Comparator.comparingDouble(ItemRewardInfo::getProb));
        Collections.reverse(iris);
        double rand = new Random().nextDouble() * 100 + 1;
        for (ItemRewardInfo iri : iris) {
            if (rand <= iri.getProb()) {
                Item item = ItemData.getItemDeepCopy(iri.getItemID());
                item.setQuantity(iri.getCount());
                return item;
            }
            rand -= iri.getProb();
        }
        return null;
    }

    public void setSample(boolean sample) {
        this.sample = sample;
    }
    public boolean hasSample() {
        return sample;
    }

    public int getBaseStat(BaseStat baseStat) {
        // for bullets, etc.
        for (ScrollStat scrollStat : getScrollStats().keySet()) {
            if (scrollStat.getBaseStat() == baseStat) {
                return getScrollStats().get(scrollStat);
            }
        }
        return 0;
    }

    public Map<SpecStat, Integer> getSpecStats() {
        return specStats;
    }

    public void putSpecStat(SpecStat ss, int i) {
        getSpecStats().put(ss, i);
    }

    public void setSpecStats(Map<SpecStat, Integer> specStats) {
        this.specStats = specStats;
    }
}
