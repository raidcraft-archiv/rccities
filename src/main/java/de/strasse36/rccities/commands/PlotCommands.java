package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import com.silthus.rccoins.Bank;
import com.silthus.rccoins.MoneyTransfer;
import com.silthus.rccoins.database.Database;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.Assignment;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.database.TableHandler;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
import de.strasse36.rccities.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
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
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
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
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        RCMessaging.send(sender, RCMessaging.green("--- RCCities Plotinfo ---"), false);
        RCMessaging.send(sender, RCMessaging.green("Plotname: ") + thisPlot.getRegionId(), false);
        String pvp;
        if(thisPlot.isPvp())
            pvp = "Erlaubt";
        else
            pvp = "Verboten";
        RCMessaging.send(sender, RCMessaging.green("PVP: ") + pvp, false);
        String open;
        if(thisPlot.isOpen())
            open = "Ja";
        else
            open = "Nein";
        RCMessaging.send(sender, RCMessaging.green("Öffentlich: ") + open, false);
        String member;
        if(thisPlot.isOpen())
            member = "~alle Einwohner~";
        else
        {
            ProtectedRegion protectedRegion = WorldGuardManager.getRegion(thisPlot.getRegionId());
            if(protectedRegion == null)
            {
                protectedRegion = ChunkUtil.restoreCityPlot(thisPlot);
                ChunkUtil.saveWorldGuardDelayed();
            }
            member = protectedRegion.getMembers().toUserFriendlyString();
        }
        if(member.length() == 0)
            member = "~keine~";
        RCMessaging.send(sender, RCMessaging.green("Besitzer: ") + member, false);
    }

    public static void list(CommandSender sender, String[] args)
    {
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        Resident selectedResident;
        if(args.length > 1)
        {
            selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
            if(selectedResident == null)
            {
                TownCommandUtility.selectNoResident(sender);
                return;
            }
        }
        else
            selectedResident = resident;
        
        List<Assignment> assignmentList = TableHandler.get().getAssignmentsTable().getAssignments(selectedResident);
        RCMessaging.send(sender, RCMessaging.green("--- RCCities: Plotliste von " + selectedResident.getName() + " ---"), false);
        if(assignmentList == null)
        {
            RCMessaging.send(sender, RCMessaging.green("Der Spieler besitzt derzeit keine Plots!"), false);
            return;
        }
        String plotNames = "";
        for(Assignment assignment : assignmentList)
        {
            if(plotNames.length() > 0)
                plotNames += ", ";
            plotNames += TableHandler.get().getPlotTable().getPlot(assignment.getPlot_id()).getRegionId();
        }
        RCMessaging.send(sender, RCMessaging.green(plotNames), false);
    }

    public static void claim(CommandSender sender)
    {
        Player player = (Player)sender;
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        if(player.getWorld() != resident.getCity().getSpawn().getWorld())
        {
            PlotCommandUtility.unclaimable(sender);
            return;
        }

        if(WorldGuardManager.isClaimable(player.getLocation()))
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
        if(!RCCitiesPlugin.get().getEconomy().has(resident.getCity().getBankAccount(), MainConfig.getClaimPrice()))
        {
            PlotCommandUtility.notEnoughMoney(sender);
            return;
        }

        //generate serial id
        int serialId = TableHandler.get().getPlotTable().getNextIndex();
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

        //log in money flow
        MoneyTransfer moneyTransfer = new MoneyTransfer(resident.getCity().getName()
                , "RaidCraftBank"
                , -MainConfig.getClaimPrice()
                , Bank.getTimestamp()
                , true
                , "Claim");
        Database.addMoneyTransfer(moneyTransfer);

        //withdraw city account
        RCCitiesPlugin.get().getEconomy().remove(resident.getCity().getBankAccount(), MainConfig.getClaimPrice());
        
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
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

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
            PlotCommandUtility.noCitychunk(sender);
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
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/plot give <Spielername> [<Plotname>]' trägt den Spieler als Plotbesitzer ein.");
            return;
        }

        //check target player
        Resident selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
        if(selectedResident == null)
        {
            TownCommandUtility.selectNoResidentAny(sender);
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
            selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);
        }
        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //check if plot public
        if(selectedPlot.isOpen())
        {
            PlotCommandUtility.publicplot(sender);
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

        RCMessaging.send(sender, RCMessaging.blue(selectedResident.getName() + " ist nun Besitzer des Plot: '" + selectedPlot.getRegionId() + "'!"), false);
        Player selectedPlayer = Bukkit.getPlayer(selectedResident.getName());
        if(selectedPlayer != null && selectedPlayer.isOnline())
            RCMessaging.send(selectedPlayer, RCMessaging.blue("Dir gehört nun der Plot: '" + selectedPlot.getRegionId() + "'!"), false);

        //update plot messages
        ChunkUtil.updateChunkMessages(resident.getCity());
    }

    public static void take(CommandSender sender, String[] args)
    {
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/plot take <Spielername> [<Plotname>]' trägt den Spieler als Plotbesitzer aus.");
            return;
        }

        //check target player
        Resident selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
        if(selectedResident == null)
        {
            TownCommandUtility.selectNoResidentAny(sender);
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
            selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);
        }
        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //check if plot public
        if(selectedPlot.isOpen())
        {
            PlotCommandUtility.publicplot(sender);
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

        RCMessaging.send(sender, RCMessaging.blue(selectedResident.getName() + " wurde als Besitzer des Plot '" + selectedPlot.getRegionId() + "' entfernt!"), false);
        Player selectedPlayer = Bukkit.getPlayer(selectedResident.getName());
        if(selectedPlayer != null && selectedPlayer.isOnline())
            RCMessaging.send(selectedPlayer, RCMessaging.blue("Du wurdest als Besitzer des Plot '" + selectedPlot.getRegionId() + "' entfernt!"), false);

        //update plot messages
        ChunkUtil.updateChunkMessages(resident.getCity());
    }

    public static void buy(CommandSender sender, String[] args)
    {
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/plot buy <Anzahl>' kauft Chunks zur Stadt hinzu.");
            return;
        }

        //wrong input
        int amount = Toolbox.isInteger(args[1]);
        if(amount == -1)
        {
            TownCommandUtility.wrongAmount(sender);
            return;
        }

        double chunkPrice = amount*MainConfig.getChunkPrice();
        //not enough money
        if(!RCCitiesPlugin.get().getEconomy().has(resident.getCity().getBankAccount(), chunkPrice))
        {
            RCMessaging.warn(sender, "Es sind nicht genügend Coins in der Stadtkasse!");
            RCMessaging.warn(sender, chunkPrice + "c werden benötigt.");
            return;
        }

        //log in money flow
        MoneyTransfer moneyTransfer = new MoneyTransfer(resident.getCity().getName()
                , "RaidCraftBank"
                , -chunkPrice
                , Bank.getTimestamp()
                , true
                , "PlotBuy");
        Database.addMoneyTransfer(moneyTransfer);

        //decrease town account
        RCCitiesPlugin.get().getEconomy().remove(resident.getCity().getBankAccount(), chunkPrice);

        //increase town size
        resident.getCity().setSize(resident.getCity().getSize()+amount);
        TableHandler.get().getCityTable().updateCity(resident.getCity());

        TownMessaging.sendTownResidents(resident.getCity(), RCMessaging.blue("Die max. Stadtgrösse hat sich um " + amount + " Chunks erhöht!"));
    }

    public static void clear(CommandSender sender, String[] args)
    {
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //get plot
        Plot selectedPlot = null;
        if(args.length > 1)
        {
            selectedPlot = TableHandler.get().getPlotTable().getPlot(args[1]);
            if(selectedPlot == null)
            {
                PlotCommandUtility.noplot(sender);
                return;
            }
        }
        else
        {
            selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);
        }
        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //clear assignments
        TableHandler.get().getAssignmentsTable().deleteAssignment(selectedPlot);

        //clear region members
        WorldGuardManager.getRegion(selectedPlot.getRegionId()).setMembers(new DefaultDomain());
        WorldGuardManager.save();

        //update plot messages
        ChunkUtil.updateChunkMessages(resident.getCity());

        RCMessaging.send(sender, RCMessaging.blue("Alle Besitzer des Plot '" + selectedPlot.getRegionId() + "' wurden entfernt!"), false);
    }

    public static void pvp(CommandSender sender, String[] args)
    {
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //get plot
        Plot selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);

        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        if(args.length > 1
                && CommandUtility.togglePlotFlag(resident, selectedPlot, DefaultFlag.PVP, args[1])
                && CommandUtility.togglePlotFlag(resident, selectedPlot, DefaultFlag.POTION_SPLASH, args[1])) {
            if(args[1].equalsIgnoreCase("on"))
                selectedPlot.setPvp(true);
            else
                selectedPlot.setPvp(false);
            TableHandler.get().getPlotTable().updatePlot(selectedPlot);
            //update chunk messages
            ChunkUtil.updateChunkMessages(resident.getCity());
            return;
        }

        RCMessaging.warn(sender, "Eingabe fehlerhaft!");
        RCMessaging.warn(sender, "'/plot pvp on' schaltet PVP in diesem Chunk ein.");
        RCMessaging.warn(sender, "'/plot pvp off' schaltet PVP in diesem Chunk aus.");
    }

    public static void mobs(CommandSender sender, String[] args)
    {
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //get plot
        Plot selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);

        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        if(args.length > 1 && CommandUtility.togglePlotFlag(resident, selectedPlot, DefaultFlag.MOB_SPAWNING, args[1])) {
            if(args[1].equalsIgnoreCase("on"))
                selectedPlot.setMobs(true);
            else
                selectedPlot.setMobs(false);
            TableHandler.get().getPlotTable().updatePlot(selectedPlot);
            //update chunk messages
            ChunkUtil.updateChunkMessages(resident.getCity());
            return;
        }

        RCMessaging.warn(sender, "Eingabe fehlerhaft!");
        RCMessaging.warn(sender, "'/plot mobs on' schaltet das spawnen von Mobs in diesem Chunk ein.");
        RCMessaging.warn(sender, "'/plot mobs off' schaltet das spawnen von Mobs in diesem Chunk aus.");
    }

    public static void publicPlot(CommandSender sender, String[] args)
    {
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //get plot
        Plot selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);

        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        if(args.length > 1)
        {
            if(args[1].equalsIgnoreCase("on"))
            {
                selectedPlot.setOpen(true);
                TableHandler.get().getPlotTable().updatePlot(selectedPlot);
                ChunkUtil.setPublic(resident.getCity());
                RCMessaging.send(sender, RCMessaging.blue("Der Plot kann nun von allen Einwohnern bebaut werden!"), false);
                //update chunk messages
                ChunkUtil.updateChunkMessages(resident.getCity());
                return;
            }

            if(args[1].equalsIgnoreCase("off"))
            {
                selectedPlot.setOpen(false);
                TableHandler.get().getPlotTable().updatePlot(selectedPlot);
                //clear region members
                WorldGuardManager.getRegion(selectedPlot.getRegionId()).setMembers(new DefaultDomain());
                WorldGuardManager.save();
                RCMessaging.send(sender, RCMessaging.blue("Der Plot ist nun nicht mehr Öffentlich!"), false);
                //update chunk messages
                ChunkUtil.updateChunkMessages(resident.getCity());
                return;
            }
        }

        RCMessaging.warn(sender, "Eingabe fehlerhaft!");
        RCMessaging.warn(sender, "'/plot public on' Macht den Plot öffentlich bebaubar.");
        RCMessaging.warn(sender, "'/plot public off' Macht den Plot wieder Privat.");
    }

    public static void mark(CommandSender sender)
    {
        Player player = (Player)sender;
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //get plot
        Plot selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);

        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //check money
        if(!RCCitiesPlugin.get().getEconomy().has(resident.getCity().getBankAccount(), MainConfig.getMarkPrice()))
        {
            RCMessaging.warn(sender, "Das setzen des Fackelrahmens kostet " + MainConfig.getMarkPrice() + "c!");
            return;
        }

        //log in money flow
        MoneyTransfer moneyTransfer = new MoneyTransfer(resident.getCity().getName()
                , "RaidCraftBank"
                , -MainConfig.getMarkPrice()
                , Bank.getTimestamp()
                , true
                , "PlotMark");
        Database.addMoneyTransfer(moneyTransfer);

        //decrease money
        RCCitiesPlugin.get().getEconomy().remove(resident.getCity().getBankAccount(), MainConfig.getMarkPrice());

        //set torches
        Chunk chunk = player.getLocation().getChunk();
        ChunkUtil.markChunk(chunk);
        RCMessaging.send(sender, RCMessaging.blue("Der Plot wurde mit einem Fackelrahmen markiert!"), false);
    }

    public static void unmark(CommandSender sender)
    {
        Player player = (Player)sender;
        Resident resident = CommandUtility.checkAndGetResidentLeader(sender);

        if(resident == null)
            return;

        //get plot
        Plot selectedPlot = ChunkUtil.getLocalCityPlot(((Player) sender).getLocation(), resident);

        if(selectedPlot == null)
        {
            PlotCommandUtility.noCitychunk(sender);
            return;
        }

        //unmark chunk
        Chunk chunk = player.getLocation().getChunk();
        if(ChunkUtil.unmarkChunk(chunk))
            RCMessaging.send(sender, RCMessaging.blue("Die Markierung des Plots wurde entfernt!"), false);
        else
            RCMessaging.warn(sender, "Dieser Plot ist nicht markiert oder die Markierung ist zu lange her!");
    }

}
