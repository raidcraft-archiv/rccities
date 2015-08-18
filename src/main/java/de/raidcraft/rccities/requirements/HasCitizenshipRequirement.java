package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.requirement.Requirement;
import de.raidcraft.api.conversations.Conversations;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.manager.ResidentManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class HasCitizenshipRequirement implements Requirement<Player> {

    @Override
    @Information(
            value = "city.is-citizen",
            desc = "Checks if the player is member of any city.",
            aliases = {"HAS_CITIZENSHIP", "city.has-citizenship"}
    )
    public boolean test(Player player, ConfigurationSection config) {

        List<Resident> citizenships = RaidCraft.getComponent(ResidentManager.class).getCitizenships(player.getUniqueId(), RolePermission.LEAVE);
        if (citizenships == null || citizenships.size() == 0) {
            // player is not member of any city so fail the requirement
            Conversations.changeStage(player, config.getString("onfailure"));
            return false;
        } else {
            Conversations.changeStage(player, config.getString("onsuccess"));
            return true;
        }
    }
}
