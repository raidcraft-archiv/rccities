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

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        //check if cancelled
        if(event.isCancelled())
            return;

        //check world
        if(!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(MainConfig.getCityWorld()))
            return;

        //check if location is region
        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(event.getBlockPlaced().getLocation());
        if(regionSet.size() != 0)
            return;

        if(player.hasPermission("rccities.build.place"))
            return;

        event.setCancelled(true);
        RCMessaging.warn(player, "Du hast hier keine Baurechte!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();

        //check if cancelled
        if(event.isCancelled())
            return;

        //check world
        if(!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(MainConfig.getCityWorld()))
            return;

        //check if location is region
        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(event.getBlock().getLocation());
        if(regionSet.size() != 0)
            return;

        if(player.hasPermission("rccities.build.destroy"))
            return;

        event.setCancelled(true);
        RCMessaging.warn(player, "Du hast hier keine Baurechte!");
    }
}
