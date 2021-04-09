package scripts.construction.house;

import lombok.Getter;

public enum Location {
    NONE(0),
    RIMMINGTON(33);

    @Getter
    int setting;

    Location(int setting) {
        this.setting = setting;
    }
}
