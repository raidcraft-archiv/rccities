package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.Assignment;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
import de.strasse36.rccities.util.ChunkUtil;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.Bukkit;
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
        //no leadership
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

        //check money
        if(!RCCitiesPlugin.get().getEconomy().has("rccities_" + resident.getCity().getName().toLowerCase(), MainConfig.getClaimPrice()))
        {
            PlotCommandUtility.notEnoughMoney(sender);
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
        String regionId = resident.getCity().getName().toLowerCase()+"_"+serialId;

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

        //withdraw city account
        RCCitiesPlugin.get().getEconomy().remove("rccities_" + resident.getCity().getName().toLowerCase(), MainConfig.getClaimPrice());
        
        //update region owners
        ChunkUtil.updatePlotOwner(resident.getCity());
        
        //success message
        PlotCommandUtility.successfullyClaimed(sender);

        //update plot messages
        ChunkUtil.updateChunkMessages(resident.getCity());
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
        //no leadership
        if(!resident.isLeadership())
        {
            RCCitiesCommandUtility.noLeadership(sender);
            return;
        }

        //plot exist?
        List<Plot> cityPlots = TableHandler.get().getPlotTable().getPlots(resident.getCity());
        Plot thisPlot = null;
        for(Plot plot : cityPlots)
        {
            if(plot.getX() == player.getLocation().getChunk().getX() && plot.getZ() == player.getLocation().getChunk().getZ())
            {
                thisPlot = plot;
                break;
            }
        }
        if(thisPlot == null)
        {
            PlotCommandUtility.noplot(sender);
            return;
        }

        //delete region
        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(player.getWorld()).removeRegion(thisPlot.getRegionId());
        WorldGuardManager.save();

        //delete database entry
        TableHandler.get().getPlotTable().deletePlot(thisPlot.getRegionId());

        //delete plot assignments
        TableHandler.get().getAssignmentsTable().deleteAssignment(thisPlot);

        //success message
        PlotCommandUtility.successfullyUnclaimed(sender);
    }
    
    public static void give(CommandSender sender, String[] args)
    {
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no leadership
        if(!resident.isLeadership())
        {
            RCCitiesCommandUtility.noLeadership(sender);
            return;
        }

        //check target player
        Resident selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
        if(selectedResident == null || selectedResident.getCity().getId() != resident.getCity().getId())
        {
            RCCitiesCommandUtility.selectNoResident(sender);
            return;
        }

        //get plot
        Plot selectedPlot = null;
        if(args.length > 2)
        {
            selectedPlot = TableHandler.get().getPlotTable().getPlot(args[2]);
            if(selectedPlot == null)
            {
                PlotCommandUtility.noplot(sender);
                return;
            }
        }
        else
        {
            ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(player.getLocation());
            for(ProtectedRegion region : regionSet)
            {
                if(TableHandler.get().getPlotTable().getPlot(region.getId()).getCity().getId() == resident.getCity().getId())
                {
                    selectedPlot = TableHandler.get().getPlotTable().getPlot(region.getId());
                }
            }
        }
        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //assign plot
        try {
            TableHandler.get().getAssignmentsTable().newAssignment(selectedPlot, selectedResident);
        } catch (AlreadyExistsException e) {
            RCMessaging.warn(sender, e.getMessage());
            return;
        }

        //add selectedResident as region member
        WorldGuardManager.addMember(selectedPlot.getRegionId(), selectedResident.getName());
        WorldGuardManager.save();

        RCMessaging.send(sender, RCMessaging.blue(selectedResident.getName() + " ist nun Besitzer des Plot: '" + selectedPlot.getRegionId() + "'!"));
        Player selectedPlayer = Bukkit.getPlayer(selectedResident.getName());
        if(selectedPlayer != null && selectedPlayer.isOnline())
            RCMessaging.send(selectedPlayer, RCMessaging.blue("Dir gehört nun der Plot: '" + selectedPlot.getRegionId() + "'!"));

        //update plot messages
        ChunkUtil.updateChunkMessages(resident.getCity());
    }

    public static void take(CommandSender sender, String[] args)
    {
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no leadership
        if(!resident.isLeadership())
        {
            RCCitiesCommandUtility.noLeadership(sender);
            return;
        }

        //check target player
        Resident selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
        if(selectedResident == null)
        {
            RCCitiesCommandUtility.selectNoResident(sender);
            return;
        }

        //get plot
        Plot selectedPlot = null;
        if(args.length > 2)
        {
            selectedPlot = TableHandler.get().getPlotTable().getPlot(args[2]);
            if(selectedPlot == null)
            {
                PlotCommandUtility.noplot(sender);
                return;
            }
        }
        else
        {
            ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(player.getLocation());
            for(ProtectedRegion region : regionSet)
            {
                if(TableHandler.get().getPlotTable().getPlot(region.getId()).getCity().getId() == resident.getCity().getId())
                {
                    selectedPlot = TableHandler.get().getPlotTable().getPlot(region.getId());
                }
            }
        }
        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //check if selectedResident is region member
        boolean member = false;
        List<Assignment> assignmentList = TableHandler.get().getAssignmentsTable().getAssignments(selectedResident);
        for(Assignment assignment : assignmentList)
        {
            if(assignment.getPlot_id() == selectedPlot.getId())
            {
                member = true;
            }
        }
        if(!member)
        {
            PlotCommandUtility.selectedNoRegionmember(sender);
            return;
        }

        //remove assignment
        TableHandler.get().getAssignmentsTable().deleteAssignment(selectedPlot, selectedResident);

        //remove selectedResident as region member
        WorldGuardManager.removeMember(selectedPlot.getRegionId(), selectedResident.getName());
        WorldGuardManager.save();

        RCMessaging.send(sender, RCMessaging.blue(selectedResident.getName() + " wurde als Besitzer des Plot '" + selectedPlot.getRegionId() + "' entfernt!"));
        Player selectedPlayer = Bukkit.getPlayer(selectedResident.getName());
        if(selectedPlayer != null && selectedPlayer.isOnline())
            RCMessaging.send(selectedPlayer, RCMessaging.blue("Du wurdest als Besitzer des Plot '" + selectedPlot.getRegionId() + "' entfernt!"));

        //update plot messages
        ChunkUtil.updateChunkMessages(resident.getCity());
    }
}
