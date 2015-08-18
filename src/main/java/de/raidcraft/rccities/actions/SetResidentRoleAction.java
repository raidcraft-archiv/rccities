package de.raidcraft.rccities.actions;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.action.action.Action;
import de.raidcraft.api.conversations.conversation.ConversationVariable;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public class SetResidentRoleAction implements Action<Player> {

    @Override
    @Information(
            value = "city.set-role",
            desc = "Sets the given role for the given player.",
            conf = {
                    "role: to set",
                    "city: to set role in",
                    "resident: to set role for (if none is given current player will be used)"
            },
            aliases = "SET_CITY_RESIDENT_ROLE"
    )
    public void accept(Player player, ConfigurationSection config) {

        String cityName = ConversationVariable.getString(player, "city").orElse(config.getString("city"));
        String residentName = ConversationVariable.getString(player, "resident_name").orElse(config.getString("resident", player.getName()));
        String roleName = ConversationVariable.getString(player, "role").orElse(config.getString("role"));

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(cityName);
        if (city == null) {
            player.sendMessage(ChatColor.RED + "City " + cityName + " does not exist!");
            return;
        }

        Resident resident = RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().getResident(UUIDUtil.convertPlayer(residentName), city);
        if (resident == null) {
            player.sendMessage(ChatColor.RED + "Resident " + residentName + " does not exist!");
            return;
        }

        Role newRole = Role.valueOf(roleName.toUpperCase());
        Role oldRole = resident.getRole();
        if (oldRole.isAdminOnly() && !player.hasPermission("rccities.resident.promote.all")) {
            player.sendMessage("Der jetzige Beruf des Einwohners kann nur von Administratoren geändert werden!");
            return;
        }

        if (newRole.isAdminOnly() && !player.hasPermission("rccities.resident.promote.all")) {
            player.sendMessage("Dieser Beruf kann nur von Administratoren vergeben werden!");
            return;
        }

        resident.setRole(newRole);
        // set owner on all city plots
        if (!oldRole.hasPermission(RolePermission.BUILD_EVERYWHERE) && newRole.hasPermission(RolePermission.BUILD_EVERYWHERE)) {
            for (Plot plot : RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlots(city)) {
                plot.updateRegion(false);
            }
        }
        // remove owner from all city plots
        if (oldRole.hasPermission(RolePermission.BUILD_EVERYWHERE) && !newRole.hasPermission(RolePermission.BUILD_EVERYWHERE)) {
            for (Plot plot : RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlots(city)) {
                plot.updateRegion(false);
            }
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + resident.getName() + " ist nun " + newRole.getFriendlyName() + " der Gilde '" + city.getFriendlyName() + "'!");
    }
}
