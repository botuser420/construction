package scripts.construction;

import org.tribot.api.General;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.*;
import org.tribot.api2007.types.*;
import scripts.NPC;
import scripts.Sleep;
import scripts.Walk;
import scripts.construction.house.House;
import scripts.data.NpcID;
import scripts.data.ObjectID;
import scripts.dax.api_lib.DaxWalker;
import scripts.dax.walker.utils.AccurateMouse;
import scripts.framework.event.EventFramework;
import scripts.items.Bank;
import scripts.items.Item;
import scripts.items.ItemList;
import scripts.magic.Spell;
import scripts.quests.requirements.item.ItemRequirement;
import scripts.utilities.ObjectUtil;

import java.util.*;

public class Construction extends EventFramework {

    private int stopLevel;
    private List<ItemRequirement> itemRequirements;
    private Furniture furniture;

    private final RSArea RIMMINGTON = new RSArea(new RSTile(2944, 3201, 0), new RSTile(2961, 3225, 0));
    private final int CONSTRUCTION_MASTER = 458;

    public static EnumSet<Furniture> furnitureSet = EnumSet.noneOf(Furniture.class);

    public Construction(int stopLevel) {
        this.stopLevel = stopLevel;
        itemRequirements = Furniture.getItemRequirements(stopLevel, furnitureSet);
        furnitureSet.addAll(Arrays.asList(Furniture.OAK_LARDER, Furniture.OAK_ARMCHAIR, Furniture.OAK_CHAIR, Furniture.WOODEN_CHAIR, Furniture.CRUDE_WOODEN_CHAIR));
    }

    public Construction(List<Furniture> furnitureList, int stopLevel) {
        this.stopLevel = stopLevel;
        this.furnitureSet.addAll(furnitureList);
        itemRequirements = Furniture.getItemRequirements(stopLevel, furnitureSet);
    }

    @Override
    public void execute() {
        if (Banking.isInBank()) {
            General.println("In bank");
            if (Banking.isBankScreenOpen())
                Bank.close();
            else if (Spell.TELEPORT_TO_HOUSE.canCast() && Spell.TELEPORT_TO_HOUSE.cast()) {
                Sleep.till(() -> House.isInHouse());
            }
        } else if (RIMMINGTON.contains(Player.getPosition())) {
            if (furniture != null && !furniture.plankType.equals(Plank.REGULAR)) {
                Inventory.drop(ItemList.getIds(Arrays.asList(Plank.REGULAR.getItemRequirements())));
            }
            if (Inventory.isFull() || getNotedPlanksId() == -1) {
                if (ObjectUtil.click(ObjectID.PORTAL_15478, "Build mode", new RSTile(2953, 3224, 0)))
                    Sleep.till(() -> House.isInHouse());
            } else
                unNote();
        } else if (House.isInHouse()) {
            Optional<Furniture> currentFurniture = Furniture.getBestFurniture(furnitureSet);
            if (currentFurniture.isPresent()) {
                if (!House.isBuildingModeOn()) {
                    House.enableBuildingMode(true);
                } else
                    trainConstruction(currentFurniture.get());
            } else {
                if (getNotedPlanksId() == -1) {
                    General.println("Didn't find noted planks in inventory. Going to visit a bank");
                    if (House.isOptionsOpen())
                        House.closeOptions();
                    else
                        DaxWalker.walkToBank();
                } else if (House.leaveHouse())
                    Sleep.till(() -> RIMMINGTON.contains(Player.getPosition()));
            }
        }
    }

    private void unNote() {
        if (NPCChat.getOptions() != null) {
            typeKey(Arrays.asList(NPCChat.getOptions()));
            Sleep.till(() -> NPCChat.getOptions() == null);
        } else if (NPC.useItem(new RSTile(2949, 3214, 0), getNotedPlanksId(), NpcID.PHIALS))
            Sleep.till(() -> NPCChat.getOptions() != null);
    }

    private void trainConstruction(Furniture furniture) {
        this.furniture = furniture;
        RSObject[] furnitureObject = org.tribot.api2007.Objects.findNearest(25, furniture.getObjectIds());
        if (furnitureObject.length > 0) {
            if (Interfaces.isInterfaceSubstantiated(CONSTRUCTION_MASTER)) {
                Keyboard.typeKeys(furniture.getBuildKey());
                Sleep.loop();
                if (Inventory.getCount(furniture.getPlankType().getItemRequirements()[0].getId()) < furniture.getNeededPlanks())
                    House.openOptions();
                Sleep.till(() -> objectDisappeared(furnitureObject[0]), General.random(10000, 12000));
            } else if (NPCChat.getOptions() != null) {
                Keyboard.typeKeys('1');
                Sleep.till(() -> objectDisappeared(furnitureObject[0]));
            } else if (House.isViewerOpen())
                House.closeViewer();
            else if (!furnitureObject[0].isClickable()) {
                if (Walking.blindWalkTo(Walk.getCloserTile(furnitureObject[0].getPosition(), General.random(1, 2))))
                    Sleep.till(() -> furnitureObject[0].isOnScreen());
            } else if (AccurateMouse.click(furnitureObject[0], "Build", "Remove")) {
                Sleep.till(() -> NPCChat.getOptions() != null || Interfaces.isInterfaceSubstantiated(CONSTRUCTION_MASTER));
            }
        } else
            House.buildRoom(furniture.getRoom());
    }

    @Override
    public String getStatus() {
        return "Construction lvl " + Skills.SKILLS.CONSTRUCTION.getActualLevel();
    }

    @Override
    public boolean isCompleted() {
        return Skills.SKILLS.CONSTRUCTION.getActualLevel() >= stopLevel;
    }

    @Override
    public String toString() {
        return furniture != null ? "Making " + furniture.toString() : "-";
    }

    @Override
    public boolean isActive() {
        return (House.isInHouse() || RIMMINGTON.contains(Player.getPosition())) && getNotedPlanksId() != -1;
    }

    @Override
    public List<ItemRequirement> getItemRequirements() {
        return itemRequirements;
    }

    private void printFurniture() {
        Arrays.stream(Furniture.values())
                .forEach(furn -> System.out.println(furn.name() + " - " + furn.getItemRequirements().get(0).getName() + " - " + furn.getItemRequirements().get(0).getQuantity() + " - noted: " + furn.getItemRequirements().get(0).isNoted()));
    }

    private int getNotedPlanksId() {
        return Arrays.stream(Inventory.getAll())
                .filter(item -> Item.isNoted(item))
                .filter(item -> Item.nameContains(item, Furniture.getCurrentPlank(furnitureSet).itemRequirements[0].getName()))
                .map(RSItem::getID)
                .findFirst()
                .orElse(-1);
    }

    private boolean objectDisappeared(RSObject object) {
        return org.tribot.api2007.Objects.findNearest(1, object.getID()).length == 0;
    }

    private void typeKey(List<String> options) {
        Collections.reverse(options);
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).contains(" coins")) {
                Keyboard.typeKeys((char) (options.size() - i + 48));
                return;
            }
        }
    }

}
