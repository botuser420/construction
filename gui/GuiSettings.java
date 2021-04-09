package scripts.gui;

import lombok.Getter;
import lombok.Setter;
import scripts.construction.Furniture;

import java.util.List;

public class GuiSettings {

    @Getter
    @Setter
    List<Furniture> tableAvailable;

    @Getter
    @Setter
    List<Furniture> tableUsed;

    @Getter
    @Setter
    String stopLevel;
}
