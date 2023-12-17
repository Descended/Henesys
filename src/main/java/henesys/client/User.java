package henesys.client;

import henesys.ServerConfig;
import henesys.client.character.Char;
import henesys.enums.UserType;
import henesys.enums.PicStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a User of this system. It is uniquely identified by its username.
 *
 * @author Sjonnie
 * Created on 3/19/2019.
 */
public class User {

    private static final Logger log = LogManager.getLogger(Account.class);

    private int id;
    private String username;

    private String email;
    private String password;
    private String pic;
    private UserType userType;
    private int votePoints;
    private int donationPoints;
    private byte gender;
    private byte accountMode;
    private int characterSlots;
    private int maplePoints;
    private int nxPrepaid;

    private Set<Account> accounts;
    private Date banExpireDate;
    private String banReason;
    private Char currentChr;
    private Account currentAcc;
    private Date birthDate;
    private int offensePoints;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.userType = UserType.Player;
        this.accounts = new HashSet<>();
        this.characterSlots = ServerConfig.MAX_CHARACTERS;
    }

    public static Logger getLog() {
        return log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Char getCurrentChr() {
        return currentChr;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setCurrentChr(Char currentChr) {
        this.currentChr = currentChr;
    }

    public int getMaplePoints() {
        return maplePoints;
    }

    public void setMaplePoints(int maplePoints) {
        this.maplePoints = maplePoints;
    }

    public int getNxPrepaid() {
        return nxPrepaid;
    }

    public void setNxPrepaid(int nxPrepaid) {
        this.nxPrepaid = nxPrepaid;
    }

    public void addMaplePoints(int points) {
        int newPoints = getMaplePoints() + points;
        if (newPoints >= 0) {
            setMaplePoints(newPoints);
        }
    }

    public void deductMaplePoints(int points) {
        addMaplePoints(-points);
    }

    public void addNXPrepaid(int prepaid) {
        int newPrepaid = getNxPrepaid() + prepaid;
        if (newPrepaid >= 0) {
            setNxPrepaid(newPrepaid);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void deductNXPrepaid(int prepaid) {
        addNXPrepaid(-prepaid);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        getAccounts().add(account);
    }


    public String getBanReason() {
        return banReason;
    }

    public void setBanReason(String banReason) {
        this.banReason = banReason;
    }

    public Account getCurrentAcc() {
        return currentAcc;
    }

    public void setCurrentAcc(Account currentAcc) {
        this.currentAcc = currentAcc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public PicStatus getPicStatus() {
        PicStatus picStatus;
        String pic = getPic();
        if(pic == null || pic.isEmpty()) {
            picStatus = PicStatus.CREATE_PIC;
        } else {
            picStatus = PicStatus.ENTER_PIC;
        }
        return picStatus;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public byte getAccountMode() {
        return accountMode;
    }

    public void setAccountMode(byte accountMode) {
        this.accountMode = accountMode;
    }


    public Date getBanExpireDate() {
        return banExpireDate;
    }

    public void setBanExpireDate(Date banExpireDate) {
        this.banExpireDate = banExpireDate;
    }

    public int getCharacterSlots() {
        return characterSlots;
    }

    public void setCharacterSlots(int characterSlots) {
        this.characterSlots = characterSlots;
    }

    public Account getAccountByWorldId(int worldId) {
        for (Account account : getAccounts()) {
            if (account.getWorldId() == worldId) {
                return account;
            }
        }
        return null;
    }

    public int getVotePoints() {
        return votePoints;
    }

    public void setVotePoints(int votePoints) {
        this.votePoints = votePoints;
    }

    public int getDonationPoints() {
        return donationPoints;
    }

    public void setDonationPoints(int donationPoints) {
        this.donationPoints = donationPoints;
    }

    public void addVotePoints(int amount) {
        setVotePoints(getVotePoints() + amount);
    }

    public void addDonationPoints(int amount) {
        setDonationPoints(getDonationPoints() + amount);
    }

    public int getOffensePoints() {
        return offensePoints;
    }

    public void setOffensePoints(int offensePoints) {
        this.offensePoints = offensePoints;
    }

    public void unstuck() {
        // TODO: implement
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", currentChr=" + currentChr +
                ", currentAcc=" + currentAcc +
                '}';
    }
}
