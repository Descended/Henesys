package henesys.handlers;

import henesys.Server;
import henesys.client.Client;
import henesys.client.User;
import henesys.connection.InPacket;
import henesys.connection.packet.Login;
import henesys.enums.LoginType;
import henesys.handlers.header.InHeader;
import henesys.world.World;

public class LoginHandler {

    @Handler(op = InHeader.CHECK_PASSWORD)
    public static void handleCheckPassword(Client c, InPacket inPacket) {
        String username = inPacket.decodeString();
        String password = inPacket.decodeString();
        c.setUser(new User(username, password));
        c.write(Login.checkPasswordResult(LoginType.Success, c.getUser()));

        // TODO: Decode machineID
    }

    @Handler(op = InHeader.WORLD_REQUEST)
    public static void handleWorldRequest(Client c, InPacket inPacket) {
        for (World world : Server.getInstance().getWorlds()) {
            c.write(Login.sendWorldInformation(world, null));
        }
        c.write(Login.sendWorldInformationEnd());
    }
}
