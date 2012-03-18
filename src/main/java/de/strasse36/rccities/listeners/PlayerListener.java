package de.strasse36.rccities.listeners;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:15
 * Description:
 */
public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        //check if cancelled
        if (event.isCancelled())
            return;
           
        //check world
        if(!event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(MainConfig.getCityWorld()))
            return;
        
        //check if location is region
        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(event.getClickedBlock().getLocation());
        if (regionSet.size() != 0)
            return;

        if (event.getPlayer().hasPermission("rccities.build.place"))
            return;


        if (event.getItem().getType() != Material.WATER_BUCKET && event.getItem().getType() != Material.LAVA_BUCKET)
            return;

        event.setCancelled(true);
        RCMessaging.warn(event.getPlayer(), "Du hast hier keine Baurechte!");
    }
}
