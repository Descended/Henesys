package henesys.client.character;

public class Char {

    private String name;

    private int id;

    private boolean changingChannel;

    private boolean inCashShop;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChangingChannel() {
        return changingChannel;
    }

    public void setChangingChannel(boolean changingChannel) {
        this.changingChannel = changingChannel;
    }

    public boolean isInCashShop() {
        return inCashShop;
    }

    public void setInCashShop(boolean inCashShop) {
        this.inCashShop = inCashShop;
    }

    public void logout() {
        // TODO: implement
    }
}
