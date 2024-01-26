package henesys.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 11/17/2017.
 */
public class DataClasses {
    public static List<Class> dataClasses = new ArrayList<>();
    public static List<Class> datCreators = new ArrayList<>();
    static {
        dataClasses.addAll(List.of(
                        ItemData.class,
                        FieldData.class
                )
        );
        datCreators.addAll(List.of(
                FieldData.class,
                ItemData.class,
                MobData.class,
                NpcData.class,
                ReactorData.class
                )
        );
    }
}

