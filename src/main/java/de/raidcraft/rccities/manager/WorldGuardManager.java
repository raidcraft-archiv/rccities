package de.raidcraft.rccities.manager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.event.block.PlaceBlockEvent;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * @author Philip Urban
 */
public class WorldGuardManager implements Listener {

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

    /**
     *  Allow pistons move across regions.
     *  Allow leves spawn across regions.
      * @param event
     */
    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlaceBlock(final PlaceBlockEvent event) {

        // we are only interested in cancelled events
        if(!event.isCancelled()) return;

        RaidCraft.LOGGER.info("[RCCDebug] Cancelled PlaceBlockEvent: " + event.getCause().getRootCause().getClass().getName());

        // we are only interested in block causes
        if(!(event.getCause().getRootCause() instanceof Block)) return;

        Block block = (Block) event.getCause().getRootCause();

        RaidCraft.LOGGER.info("[RCCDebug] BlockCause: " + block.getType());

        // process pistons
        if(block.getType() == Material.PISTON_BASE ||
                block.getType() == Material.PISTON_EXTENSION ||
                block.getType() == Material.PISTON_MOVING_PIECE ||
                block.getType() == Material.PISTON_STICKY_BASE) {

            event.setCancelled(false);
        }

        // process leaves
        else if(block.getType() == Material.LEAVES ||
                block.getType() == Material.LEAVES_2) {

            event.setCancelled(false);
        }
    }
}
