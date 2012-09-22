package de.strasse36.rccities.listeners;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.commands.ResidentCommands;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.database.TableHandler;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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

        //prevent lava and water placement
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
            if(event.getItem() != null && (event.getItem().getType() == Material.WATER_BUCKET || event.getItem().getType() == Material.LAVA_BUCKET)) {
                event.setCancelled(true);
                RCMessaging.warn(event.getPlayer(), "Du hast hier keine Baurechte!");
            }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(event.getPlayer().getName());
        if(resident != null && resident.getCity() != null && resident.isMayor() && !event.getPlayer().hasPermission("rccities.skill.mayor"))
        {
            resident.setProfession("vicemayor");
            TableHandler.get().getResidentTable().updateResident(resident);
            RCMessaging.send(event.getPlayer(), RCMessaging.blue("Du bist Bürgermeister ohne den passenden Skill!"));
            RCMessaging.send(event.getPlayer(), RCMessaging.blue("Deshalb wurdest du nun zum Vize-Bürgermeister dekradiert!"));
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(!ResidentCommands.isWarmup(event.getPlayer())) {
            return;
        }

        //track only large moves
        if(event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        ResidentCommands.cancelWarmup(event.getPlayer());
        RCMessaging.send(event.getPlayer(), RCMessaging.blue("Town Spawn Teleport abgebrochen!"), false);
    }
}
