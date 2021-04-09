package scripts.construction.house;

import lombok.Getter;

public enum Room {
    PARLOUR(1, 101, 1368, 1000, "Parlour", Floor.GROUND),
    GARDEN(1, 92, 1369, 1000, "Garden", Floor.GROUND),
    KITCHEN(5, 93, 1370, 5000, "Kitchen", Floor.GROUND),
    DINING(10, 91, 1371, 5000, "Dining room", Floor.GROUND),
    WORKSHOP(15, 100, 1377, 10000, "Workshop", Floor.GROUND),
    BEDROOM(20, 102, 1372, 10000, "Bedroom", Floor.GROUND);
    //SKILL_HALL(25, 15000, "Skill hall");

    @Getter
    int reqLvl, roomIndex, textureId;

    int coins;

    @Getter
    String name;

    @Getter
    Floor floor;


    Room(int reqLvl, int roomIndex, int textureId, int coins, String name, Floor floor) {
        this.reqLvl = reqLvl;
        this.roomIndex = roomIndex;
        this.textureId = textureId;
        this.coins = coins;
        this.name = name;
        this.floor = floor;
    }
}
