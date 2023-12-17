package henesys;

import henesys.world.World;

import java.util.List;

public class ServerConfig {

    public static final String SERVER_NAME = "Henesys";

    public static final int MAX_CHARACTERS = 30;
    public static final boolean DEBUG_MODE = true;
    public static final List<World> WORLDS = List.of(new World[]{
            new World(0, "Henesys", 0, "Henesys", 1, 1, 1, 3, 1, 1, 1),
    });
    public static final boolean AUTO_REGISTER = true;
}
