package de.strasse36.rccities.listeners;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Author: Philip Urban
 * Date: 11.03.12 - 01:37
 * Description:
 */
public class BlockListener implements Listener {

    @EventHandler( ignoreCancelled = true )
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(event.getPlayer().hasPermission("rccities.build.place"))
            return;

        //check world
        if(!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(MainConfig.getCityWorld()))
            return;

        //check if location is region
        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(event.getBlockPlaced().getLocation());
        if(regionSet.size() != 0)
            return;

        event.setCancelled(true);
        RCMessaging.warn(event.getPlayer(), "Du hast hier keine Baurechte!");
    }

    @EventHandler( ignoreCancelled = true )
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(event.getPlayer().hasPermission("rccities.build.destroy"))
            return;

        //check world
        if(!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(MainConfig.getCityWorld()))
            return;

        //check if location is region
        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(event.getBlock().getLocation());
        if(regionSet.size() != 0)
            return;

        event.setCancelled(true);
        RCMessaging.warn(event.getPlayer(), "Du hast hier keine Baurechte!");
    }
}
