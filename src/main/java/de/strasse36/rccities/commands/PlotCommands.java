package de.strasse36.rccities.commands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
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

        if(player.getWorld() != resident.getCity().getSpawn().getWorld())
        {
            PlotCommandUtility.unclaimable(sender);
            return;
        }

        if(WorldGuardManager.getLocalRegions(player.getLocation()).size() > 0)
        {
            PlotCommandUtility.unclaimable(sender);
            return;
        }

        //enough plots left
        List<Plot> cityPlots = TableHandler.get().getPlotTable().getPlots(resident.getCity());
        if(cityPlots.size() >= resident.getCity().getSize())
        {
            PlotCommandUtility.notEnoughSpace(sender);
            return;
        }

        //generate serial id
        List<Plot> plots = TableHandler.get().getPlotTable().getPlots();
        int serialId = 0;
        for(Plot plot : plots)
        {
            if(plot.getId()>serialId)
                serialId = plot.getId();
        }
        serialId++;
        String regionId = resident.getCity().getName()+"_"+serialId;

        //insert new plot in database
        Plot newPlot = new Plot(resident.getCity(), regionId, player.getLocation().getChunk().getX(), player.getLocation().getChunk().getZ());
        try {
            TableHandler.get().getPlotTable().newPlot(newPlot);
        } catch (AlreadyExistsException e) {
            PlotCommandUtility.alreadyExist(sender);
            return;
        }

        //claim chunk with worldguard
        ProtectedRegion region = ChunkUtil.getProtectedCuboidRegion(regionId, player.getLocation());
        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(player.getWorld()).addRegion(region);
        WorldGuardManager.setTownFlags(regionId);
        WorldGuardManager.save();

        //success message
        PlotCommandUtility.successfullyClaimed(sender);
    }

    public static void unclaim(CommandSender sender)
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

        String regionId = "";

        //plot exist?
        List<Plot> cityPlots = TableHandler.get().getPlotTable().getPlots(resident.getCity());
        boolean exist = false;
        for(Plot plot : cityPlots)
        {
            if(plot.getX() == player.getLocation().getChunk().getX() && plot.getZ() == player.getLocation().getChunk().getZ())
            {
                exist = true;
                regionId = plot.getRegionId();
                break;
            }
        }
        if(!exist)
        {
            PlotCommandUtility.noplot(sender);
            return;
        }

        //delete region
        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(player.getWorld()).removeRegion(regionId);
        WorldGuardManager.save();

        //delete database entry
        TableHandler.get().getPlotTable().deletePlot(regionId);

        //success message
        PlotCommandUtility.successfullyUnclaimed(sender);
    }
}
