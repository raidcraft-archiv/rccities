package de.strasse36.rccities.commands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.ChunkUtil;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isLeadership())
        {
            RCCitiesCommandUtility.noLeadership(sender);
            return;
        }

        //TODO check whole chunk
        //other regions
        if(WorldGuardManager.getLocalRegions(player.getLocation()).size() > 0)
        {
            PlotCommandUtility.unclaimable(sender);
        }


        //TODO
        //genug plots übrig

        //generate serial id
        List<Plot> plots = TableHandler.get().getPlotTable().getPlots();
        int serialId = 0;
        for(Plot plot : plots)
        {
            if(plot.getId()>serialId)
                serialId = plot.getId();
        }
        serialId++;

        String plotName = resident.getCity().getName()+"_"+serialId;

        ProtectedRegion region = ChunkUtil.getProtectedCuboidRegion(plotName, player.getLocation());
        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(player.getWorld()).addRegion(region);
        //disable mobs
        //etc..

    }
}
