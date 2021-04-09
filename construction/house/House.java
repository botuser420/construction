package scripts.construction.house;

import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSVarBit;
import scripts.Sleep;
import scripts.data.Settings;
import scripts.data.Varbits;
import scripts.utilities.Option;

import java.util.Arrays;
import java.util.Optional;

public class House {

    private static final int HOUSE_OPTIONS = 370, OPTIONS_TAB = 116, VIEWER = 422, ROOM_CREATION = 212;

    public static boolean isInHouse() {
        return Objects.findNearest(15, 4525).length > 0 && RSVarBit.get(6719).getValue() == 0;
    }

    public static boolean isViewerOpen() {
        return Interfaces.isInterfaceSubstantiated(VIEWER);
    }

    public static boolean isOptionsOpen() {
        return Interfaces.isInterfaceSubstantiated(HOUSE_OPTIONS);
    }

    public static boolean isRoomCreationOpen() {
        return Interfaces.isInterfaceSubstantiated(ROOM_CREATION);
    }

    public static boolean openOptions() {
        if (Option.openSettingsTab()) {
            RSInterface houseButton = Interfaces.findWhereAction("View House Options", OPTIONS_TAB);
            if (Interfaces.isInterfaceSubstantiated(houseButton) && houseButton.click("View House Options")) {
                return Sleep.till(() -> isOptionsOpen());
            }
        }
        return isOptionsOpen();
    }

    public static boolean closeOptions() {
        if (isOptionsOpen()) {
            RSInterface closeButton = Interfaces.findWhereAction("Close", HOUSE_OPTIONS);
            return Sleep.till(() -> Interfaces.isInterfaceSubstantiated(closeButton) && closeButton.click("Close"));
        }
        return !isOptionsOpen();
    }

    public static boolean closeViewer() {
        if (!isViewerOpen()) return true;
        RSInterface closeButton = Interfaces.findWhereAction("Close", VIEWER);
        return Sleep.till(() -> Interfaces.isInterfaceSubstantiated(closeButton) && closeButton.click("Close"));
    }

    public static boolean leaveHouse() {
        if (openOptions()) {
            RSInterface leaveButton = Interfaces.findWhereAction("Leave House");
            if (Interfaces.isInterfaceSubstantiated(leaveButton) && leaveButton.click("Leave House")) {
                return Sleep.till(() -> !House.isInHouse());
            }
        }
        return false;
    }

    public static Location getLocation() {
        return Arrays.stream(Location.values())
                .filter(house -> house.getSetting() == Game.getSetting(Settings.HOUSE_LOCATION.getId()))
                .findFirst()
                .orElse(Location.NONE);
    }

    public static HouseDoors getRender() {
        return Arrays.stream(HouseDoors.values())
                .filter(houseDoors -> houseDoors.getSetting() == RSVarBit.get(Varbits.DOOR_RENDER.getId()).getValue())
                .findFirst()
                .orElse(HouseDoors.CLOSED);
    }

    public static boolean setRender(HouseDoors renderSetting) {
        if (openOptions()) {
            RSInterface door = Interfaces.get(HOUSE_OPTIONS, i -> i.getTextureID() == renderSetting.getTextureId());
            if (door != null) {
                RSInterface checkBox = Interfaces.get(HOUSE_OPTIONS, door.getIndex() + 1);
                if (Interfaces.isInterfaceSubstantiated(checkBox) && checkBox.click("Select"))
                    return Sleep.till(() -> getRender().equals(renderSetting));
            }
        }
        return false;
    }

    public static boolean openViewer() {
        if (openOptions()) {
            RSInterface viewerButton = Interfaces.findWhereAction("Viewer", HOUSE_OPTIONS);
            if (Interfaces.isInterfaceSubstantiated(viewerButton) && viewerButton.click("Viewer")) {
                return Sleep.till(() -> Interfaces.isInterfaceSubstantiated(VIEWER));
            }
        }
        return false;
    }

    public static boolean buildRoom(Room room) {
        if (isRoomCreationOpen()) return selectRoom(room.getName());
        if (!isViewerOpen()) return openViewer();
        if (roomAdded(room)) return confirmRoom();
        return selectSquare(room);
    }

    private static boolean selectRoom(String roomName) {
        RSInterface roomInterface = Interfaces.findWhereAction(roomName, ROOM_CREATION);
        return Interfaces.isInterfaceSubstantiated(roomInterface) && roomInterface.click(roomName); //TODO Add scrolling
    }

    private static boolean selectSquare(Room room) {
        RSInterface roomSquares = Interfaces.get(VIEWER, i -> i.getWidth() == 256 && i.getHeight() == 256);
        if (roomSquares != null) {
            Optional<RSInterface> roomSquare = Arrays.stream(roomSquares.getChildren())
                    .filter(i -> i.getChildren().length == 243)
                    .findFirst();
            if (roomSquare.isPresent() && roomSquare.get().click() && Sleep.till(() -> ChooseOption.getOptions() != null) &&
                    ChooseOption.select("Add room"))
                return Sleep.till(() -> isRoomCreationOpen());
        }
        return false;
    }

    private static boolean roomAdded(Room room) {
        return Interfaces.get(VIEWER, i -> i.getTextureID() == room.getTextureId()) != null;
    }

    private static boolean confirmRoom() {
        RSInterface done = Interfaces.findWhereAction("Done", VIEWER);
        return Interfaces.isInterfaceSubstantiated(done) && done.click("Done");
    }

    public static boolean isBuildingModeOn() {
        return RSVarBit.get(Varbits.BUILDING_MODE.getId()).getValue() == 1;
    }

    public static boolean enableBuildingMode(boolean enable) {
        if (openOptions()) {
            RSInterface buildingMode = Interfaces.get(HOUSE_OPTIONS, i -> i.getText().equals("Building Mode"));
            if (buildingMode != null) {
                RSInterface toggleSetting = Interfaces.findWhereAction(enable ? "On" : "Off"); //Todo this is kind of cheating. Only works because it's the first option
                if (Interfaces.isInterfaceSubstantiated(toggleSetting) && toggleSetting.click(enable ? "On" : "Off"))
                    return Sleep.till(() -> isBuildingModeOn() == enable);
            }
        }
        return false;
    }

}
