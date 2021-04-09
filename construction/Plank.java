package scripts.construction;

import lombok.Getter;
import org.tribot.api2007.Skills;
import scripts.data.ItemCollections;
import scripts.data.ItemID;
import scripts.items.ItemList;
import scripts.magic.Spell;
import scripts.quests.requirements.item.ItemRequirement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Plank {
    REGULAR(29, new ItemRequirement("Plank", ItemID.PLANK), new ItemRequirement("Steel nails", ItemID.STEEL_NAILS, 2)),
    OAK(60, new ItemRequirement("Oak plank", ItemID.OAK_PLANK)),
    TEAK(90, new ItemRequirement("Teak plank", ItemID.TEAK_PLANK)),
    MAHOGANY(140, new ItemRequirement("Mahogany plank", ItemID.MAHOGANY_PLANK));

    @Getter
    int experienceGiven;

    @Getter
    ItemRequirement[] itemRequirements;

    Plank(int experienceGiven, ItemRequirement... itemRequirements) {
        this.experienceGiven = experienceGiven;
        this.itemRequirements = itemRequirements;
    }
}
