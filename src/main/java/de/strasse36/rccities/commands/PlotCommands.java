package de.strasse36.rccities.commands;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.ChunkUtil;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

/**
 * Author: Philip Urban
 * Date: 03.03.12 - 20:15
 * Description:
 */
public class PlotCommands {

    public static void showPlotInfo(CommandSender sender)
    {

    }

    public static void claim(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            CommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isLeadership())
        {
            CommandUtility.noLeadership(sender);
            return;
        }

        WorldGuardManager.
        //TODO
        //keine andere region
        //genug plots übrig

        //serialId = lfd.Nr. von bestehenden Plots
        
        String plotName = resident.getCity().getName()+"_"+serialId;
        Player player = (Player)sender;
        ProtectedRegion region = ChunkUtil.getProtectedCuboidRegion(plotName, player.getLocation());
        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(player.getWorld()).addRegion(region);
        //disable mobs
        //etc..

    }
}
