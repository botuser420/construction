package scripts.construction.house;

import org.tribot.api2007.types.RSVarBit;
import scripts.data.Varbits;

public enum Butler {
    NONE(),
    DEMON();

    public static Butler get() {
        switch (RSVarBit.get(Varbits.BUTLER.getId()).getValue()) {
            case 0:
                return NONE;
            case 8:
                return DEMON;
            default:
                return NONE;
        }
    }
}
