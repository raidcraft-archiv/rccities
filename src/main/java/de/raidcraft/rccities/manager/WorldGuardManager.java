package de.raidcraft.rccities.manager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Philip Urban
 */
public class WorldGuardManager {

    private RCCitiesPlugin plugin;
    private WorldGuardPlugin worldGuard;

    public WorldGuardManager(RCCitiesPlugin plugin, WorldGuardPlugin worldGuard) {

        this.plugin = plugin;
        this.worldGuard = worldGuard;
    }

    public boolean claimable(Location location) {

        ApplicableRegionSet regions = worldGuard.getRegionManager(location.getWorld()).getApplicableRegions(location);
        if (regions.size() == 0) {
            return true;
        }
        for (ProtectedRegion region : regions) {
            for (String ignoredRegion : plugin.getConfig().ignoredRegions) {
                if (!region.getId().startsWith(ignoredRegion)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void save() {

        for (World world : Bukkit.getServer().getWorlds()) {
            try {
                worldGuard.getRegionManager(world).save();
            } catch (StorageException e) {
                RaidCraft.LOGGER.warning(e.getMessage());
            }
        }
    }
}
