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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
      * @param event
     */

    private Map<PlaceBlockEvent, Integer> events = new HashMap<>();

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onPlaceBlockLowest(final PlaceBlockEvent event) {

        // we are only interested in block causes
        if(!(event.getCause().getRootCause() instanceof Block)) return;

        Block block = (Block) event.getCause().getRootCause();

        // process pistons
        if(block.getType() == Material.PISTON_BASE ||
                block.getType() == Material.PISTON_EXTENSION ||
                block.getType() == Material.PISTON_MOVING_PIECE ||
                block.getType() == Material.PISTON_STICKY_BASE) {

            events.put(event, event.getBlocks().size());
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlaceBlockHighest(final PlaceBlockEvent event) {

        int originalSize = events.remove(event);

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

            if(event.getBlocks().size() != originalSize) {
                RaidCraft.LOGGER.info("[RCCDebug] Original size: " + originalSize + " | Current size: " + event.getBlocks().size());
                for(int i = 0; i < originalSize - event.getBlocks().size(); i++) {
                    event.getBlocks().add(event.getBlocks().get(0));
                }
            }
            event.setCancelled(false);
        }
    }
}
