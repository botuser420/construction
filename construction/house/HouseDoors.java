package scripts.construction.house;

import lombok.Getter;

public enum HouseDoors {
    CLOSED(0, 1576),
    OPEN(1, 1577),
    NONE(2, 1578);

    @Getter
    int setting;

    @Getter
    int textureId;

    HouseDoors(int setting, int textureId) {
        this.setting = setting;
        this.textureId = textureId;
    }
}
