package henesys.client;

import henesys.client.character.Char;
import henesys.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Account class that represents an account in the system. An User can have multiple accounts. For each world.
 * @author Desc
 */
public class Account {

    private static final Logger log = LogManager.getLogger(Account.class);

    private int id;
    private int worldId;

    private Set<Char> characters = new HashSet<>();
    // nxCredit is from mobs, so is account (world) specific.
    private int nxCredit;
    private User user;
    private Char currentChr;

    public Account(User user, int worldId) {
        this.user = user;
        this.worldId = worldId;
        this.characters = new HashSet<>();
    }

    public Account(){
    }

    public int getId() {
        return id;
    }

    public Set<Char> getCharacters() {
        return characters;
    }

    public void addCharacter(Char character) {
       getCharacters().add(character);
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getNxCredit() {
        return nxCredit;
    }

    public void setNxCredit(int nxCredit) {
        this.nxCredit = nxCredit;
    }

    public void addNXCredit(int credit) {
        int newCredit = getNxCredit() + credit;
        if (newCredit >= 0) {
            setNxCredit(newCredit);
        } else {
            log.error("Account {} tried to set NX Credits under 0.", id);
        }
    }
    public void deductNXCredit(int credit) {
        addNXCredit(-credit);
    }

    public Char getCharById(int id) {
        return Util.findWithPred(getCharacters(), chr -> chr.getId() == id);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public Char getCurrentChr() {
        return currentChr;
    }

    public void setCurrentChr(Char currentChr) {
        this.currentChr = currentChr;
    }

}
