package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.RolePermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class LeaveCityAction implements Action<Player> {

    @Override
    @Information(
            value = "city.leave",
            desc = "Leaves the city/guild if possible.",
            conf = {
                    "city: to leave"
            },
            aliases = "LEAVE_CITY"
    )
    public void accept(Player player, ConfigurationSection config) {


        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "City '" + cityName + "' does not exist!");
            return;
        }

        Resident resident = RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager()
                .getResident(player.getUniqueId(), city);
        if (resident == null) {
            player.sendMessage(" ");
            player.sendMessage(ChatColor.RED + "Du bist kein Mitglied dieser Gilde!");
            return;
        }

        if (!resident.getRole().hasPermission(RolePermission.LEAVE)) {
            player.sendMessage(" ");
            player.sendMessage(ChatColor.RED + "Du darfst diese Gilde nichtverlassen!");
            return;
        }

        Bukkit.broadcastMessage(ChatColor.GOLD + resident.getName() + " hat die Gilde '" + resident.getCity().getFriendlyName() + "' verlassen!");
        resident.delete();
    }
}
