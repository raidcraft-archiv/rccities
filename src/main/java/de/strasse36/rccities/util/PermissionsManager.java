package de.strasse36.rccities.util;

import com.sk89q.worldedit.bukkit.entity.BukkitItem;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * 27.11.11 - 11:18
 * @author Silthus
 */
public final class PermissionsManager {
    

    private static Permission getPermission() {
        return RCCitiesPlugin.get().getPermissions();
    }
    
    /**
     * Adds the player to the given group in all worlds
     * specified in the MainConfig.
     * @param player to add to group
     * @param group to add to player
     */
    public static void addGroup(String player, String group) {
        group = group.toLowerCase();
        for (World world : Bukkit.getWorlds()) {
            if (hasGroup(world.getName(), player, group)) continue;
            getPermission().playerAddGroup(world, player, group);
        }
    }

	public static void addGroup(String player, String group, Collection<String> worlds) {
		for (String world : worlds) {
			if (hasGroup(world, player, group)) return;
			getPermission().playerAddGroup(world, player, group);
		}
	}

    /**
     * Removes the player from the given group in all worlds
     * specified in the MainConfig.
     * @param player to delete to group
     * @param group to delete to player
     */
    public static void removeGroup(String player, String group) {
        for (World world : Bukkit.getWorlds()) {
            if (!hasGroup(world.getName(), player, group)) continue;
            getPermission().playerRemoveGroup(world, player, group);
        }
    }

    /**
     * Gets all groups of the player for all worlds defined in
     * the MainConfig
     * @param player to get groups for
     * @return list of all groups
     */
    public static List<String> getGroups(String player) {
        ArrayList<String> groups = new ArrayList<String>();
        for (World world : Bukkit.getWorlds()) {
            Collections.addAll(groups, getPermission().getPlayerGroups(world, player));
        }
        return groups;
    }

    /**
     * Checks if the player has the specified group
     * @param world of the player
     * @param player to check
     * @param group to check for
     * @return true on the first matching group of the defined worlds
     */
    public static boolean hasGroup(String world, String player, String group) {
        return getPermission().playerInGroup(world, player, group);
    }
}
