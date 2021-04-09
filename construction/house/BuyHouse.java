package scripts.construction.house;

import org.tribot.api2007.Game;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSTile;
import scripts.NPC;
import scripts.data.ItemID;
import scripts.data.NpcID;
import scripts.data.Settings;
import scripts.framework.event.EventFramework;
import scripts.quests.requirements.item.ItemRequirement;

import java.util.Arrays;
import java.util.List;

public class BuyHouse extends EventFramework {

    Location location;
    List<ItemRequirement> itemRequirements = Arrays.asList(new ItemRequirement("Coins", ItemID.COINS_995, 1000));

    public BuyHouse() {
    }

    public BuyHouse(Location location) {
        this.location = location;
    }

    @Override
    public void execute() {
        if (!WorldHopper.isCurrentWorldMembers().orElse(false)) {
            WorldHopper.changeWorld(WorldHopper.getRandomWorld(true));
        } else
            NPC.talkTo(new RSTile(3240, 3475, 0), Arrays.asList("How can I get a house?", "Yes please!"), NpcID.ESTATE_AGENT);
    }

    @Override
    public boolean isCompleted() {
        return location != null ? House.getLocation().equals(location) : Game.getSetting(Settings.HOUSE_LOCATION.getId()) > 0;
    }

    @Override
    public String toString() {
        return "Buying a house";
    }

    @Override
    public List<ItemRequirement> getItemRequirements() {
        return itemRequirements;
    }

    @Override
    public int priority() {
        return 10;
    }

}
