package scripts.gui;

import com.allatori.annotations.DoNotRename;
import lombok.Getter;
import lombok.Setter;
import scripts.construction.Furniture;

import java.util.List;

public class GuiSettings {

    @Getter
    @Setter
    @DoNotRename
    List<Furniture> tableAvailable;

    @Getter
    @Setter
    @DoNotRename
    List<Furniture> tableUsed;

    @Getter
    @Setter
    @DoNotRename
    String stopLevel;
}
