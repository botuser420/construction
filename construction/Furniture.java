package scripts.construction;

import lombok.Getter;
import obf.Ge;
import org.tribot.api.General;
import org.tribot.api2007.Skills;
import scripts.construction.house.Room;
import scripts.data.ItemCollections;
import scripts.data.ItemID;
import scripts.items.ItemList;
import scripts.magic.Spell;
import scripts.quests.requirements.item.ItemRequirement;

import java.util.*;

public enum Furniture {
    OAK_LARDER("Oak Larder", 33, 8, Plank.OAK, '2', Room.KITCHEN, "Larder"),
    CARVED_OAK_TABLE("Carved Oak Table", 31, 6, Plank.OAK, '3', Room.DINING, "Table"),
    OAK_ARMCHAIR("Oak Armchair", 29, 3, Plank.OAK, '5', Room.PARLOUR, "Chair"),
    OAK_DINING_TABLE("Oak Dining Table", 22, 4, Plank.OAK, '2', Room.DINING, "Table"),
    OAK_CHAIR("Oak Chair", 19, 2, Plank.OAK, '4', Room.PARLOUR, "Chair"),
    //CRAFTING_TABLE1(16, 4, Plank.OAK, '1', Room.KITCHEN, "Chair"),
    //REPAIR_BENCH(15, 2, Plank.OAK, '1', Room.KITCHEN, "Chair"),
    WOODEN_LARDER("Wooden Larder", 9, 8, Plank.REGULAR, '1', Room.KITCHEN, "Larder"),
    WOODEN_CHAIR("Wooden Chair", 8, 3, Plank.REGULAR, '2', Room.PARLOUR, "Chair"),
    WOODEN_BOOKCASE("Wooden Bookcase", 4, 4, Plank.REGULAR, '1', Room.PARLOUR, "Bookcase"),
    CRUDE_WOODEN_CHAIR("Crude Wooden Chair", 1, 2, Plank.REGULAR, '1', Room.PARLOUR, "Chair");

    @Getter
    String name;

    @Getter
    int reqLvl;

    @Getter
    int neededPlanks;

    @Getter
    Plank plankType;

    @Getter
    List<ItemRequirement> itemRequirements;

    @Getter
    Character buildKey;

    @Getter
    Room room;

    @Getter
    String objectName;

    int experienceGiven;

    Furniture(String name, int reqLvl, int neededPlanks, Plank plankType, Character buildKey, Room room, String objectName) {
        this.name = name;
        this.reqLvl = reqLvl;
        this.neededPlanks = neededPlanks;
        this.plankType = plankType;
        this.itemRequirements = ItemList.multiply(Arrays.asList(plankType.getItemRequirements()), neededPlanks, false);
        this.buildKey = buildKey;
        this.room = room;
        this.objectName = objectName;
        experienceGiven = neededPlanks * plankType.getExperienceGiven();
    }

    public static Optional<Furniture> getBestFurniture(EnumSet<Furniture> furnitureSet) {
        return furnitureSet.stream()
                .filter(furniture -> Skills.SKILLS.CONSTRUCTION.getCurrentLevel() >= furniture.getReqLvl())
                .filter(furniture -> ItemList.has(furniture.getItemRequirements()))
                .findFirst();
    }

    public static Plank getCurrentPlank(EnumSet<Furniture> furnitureSet) {
        return furnitureSet.stream()
                .filter(furn -> Skills.SKILLS.CONSTRUCTION.getActualLevel() >= furn.getReqLvl())
                .map(Furniture::getPlankType)
                .findFirst()
                .orElse(Plank.REGULAR);
    }

    public boolean canMake() {
        return Skills.SKILLS.CONSTRUCTION.getActualLevel() >= reqLvl && ItemList.has(itemRequirements);
    }


    public static List<ItemRequirement> getItemRequirements(int stopLevel, EnumSet<Furniture> furnitureSet) {
        ArrayList<ItemRequirement> neededItems = new ArrayList<>();

        neededItems.add(new ItemRequirement("Hammer", ItemID.HAMMER));
        neededItems.add(new ItemRequirement("Saw", ItemID.SAW));
        neededItems.add(new ItemRequirement("Coins", ItemID.COINS_995, 100000, -1));

        ItemRequirement ringOfWealth = new ItemRequirement("Ring of wealth (5)", ItemID.RING_OF_WEALTH_5, 1, true);
        ringOfWealth.addAlternates(ItemCollections.getRingOfWealths());
        neededItems.add(ringOfWealth);

        neededItems.addAll(getRequiredPlanks(stopLevel, furnitureSet));

        if (Spell.TELEPORT_TO_HOUSE.hasLevel()) {
            neededItems.add(new ItemRequirement("Law rune", ItemID.LAW_RUNE));
            neededItems.add(new ItemRequirement("Earth rune", ItemID.EARTH_RUNE));
            neededItems.add(new ItemRequirement("Air rune", ItemID.AIR_RUNE));
        } else
            neededItems.add(new ItemRequirement("Teleport to House", ItemID.TELEPORT_TO_HOUSE));

        return neededItems;
    }

    private static List<ItemRequirement> getRequiredPlanks(int stopLevel, EnumSet<Furniture> furnitureSet) {
        List<ItemRequirement> requiredPlanks = new ArrayList<ItemRequirement>();
        Map<Integer, Furniture> furniture = getAmounts(stopLevel, furnitureSet);
        Map<Plank, Integer> amounts = new HashMap<>();
        for (Map.Entry<Integer, Furniture> entry : furniture.entrySet()) {
            amounts.putIfAbsent(entry.getValue().getPlankType(), 0);
            amounts.put(entry.getValue().getPlankType(),
                    amounts.get(entry.getValue().getPlankType()) + (entry.getKey() * entry.getValue().getNeededPlanks()));
        }

        amounts.entrySet().forEach(entry -> {
            entry.getKey().getItemRequirements()[0].setNoted(true);
            requiredPlanks.addAll(ItemList.multiply(Arrays.asList(entry.getKey().getItemRequirements()), entry.getValue(), false));
            entry.getKey().getItemRequirements()[0].setNoted(false);
        });
        requiredPlanks.forEach(item -> General.println(item.getName() + " " + item.getBuyQuantity()));

        return requiredPlanks;
    }

    private static Map<Integer, Furniture> getAmounts(int stopLevel, EnumSet<Furniture> furnitureSet) {
        Map<Integer, Furniture> amounts = new HashMap<>();
        int constructionLvl = Skills.SKILLS.CONSTRUCTION.getActualLevel();
        for (Furniture furniture : furnitureSet) {
            if (furniture.getReqLvl() < stopLevel) {
                amounts.put(getRequiredAmount(furniture.getReqLvl(), stopLevel, furniture), furniture);
                stopLevel = furniture.getReqLvl();
                if (constructionLvl >= furniture.getReqLvl())
                    break;
            }
        }
        amounts.entrySet().forEach(item -> General.println(item.getKey() + " " + item.getValue().toString()));

        return amounts;
    }

    private static int getRequiredAmount(int startLvl, int stopLvl, Furniture furniture) {
        int xpToStart = Skills.getXPToLevel(Skills.SKILLS.CONSTRUCTION, startLvl);
        int neededXP = Skills.getXPToLevel(Skills.SKILLS.CONSTRUCTION, stopLvl) - (xpToStart > 0 ? xpToStart : 0);
        return (int) Math.ceil((double) neededXP / furniture.experienceGiven);
    }

}
