package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.exceptions.UnknownProfessionException;
import de.strasse36.rccities.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:22
 * Description:
 */
public class CityStaffCommands {

    public static Map<String, City> invites = new HashMap<String, City>();


    public static void setspawn(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            TownCommandUtility.noMayor(sender);
            return;
        }

        if(((Player)sender).getWorld() != resident.getCity().getSpawn().getWorld())
        {
            TownCommandUtility.wrongWorld(sender);
            return;
        }

        resident.getCity().setSpawn(((Player)sender).getLocation());
        TableHandler.get().getCityTable().updateCity(resident.getCity());
        RCMessaging.send(sender, RCMessaging.blue("Der Townspawn von " + resident.getCity().getName() + " wurde erfolgreich verlegt!"), false);
        return;
    }

    public static void promote(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            TownCommandUtility.noMayor(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 3)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town promote <Spielername> <Beruf>' Weist einem Spieler ein Beruf zu.");
            return;
        }

        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            if(args[2].equalsIgnoreCase("mayor"))
            {
            TownCommandUtility.selfAction(sender);
            return;
            }
            else
            {
                //check if other mayor exists
                Boolean otherMayor = false;
                List<Resident> residentlist = TableHandler.get().getResidentTable().getResidents(resident.getCity());
                for(Resident otherResident : residentlist)
                {
                    if(otherResident.isMayor() && !otherResident.getName().equalsIgnoreCase(sender.getName()))
                    {
                        otherMayor = true;
                    }
                }
                if(!otherMayor)
                {
                    RCMessaging.warn(sender, "Du musst zuerst einen anderen Bürgermeister ernennen bevor Du dieses Amt verlassen kannst!");
                    return;
                }
            }
        }
        Resident selectedResident = ResidentHelper.isResident(args[1], resident.getCity());
        if(selectedResident == null)
        {
            TownCommandUtility.selectNoResident(sender);
            return;
        }

        if(args[2].equalsIgnoreCase("mayor"))
        {
            Player player = Bukkit.getPlayer(selectedResident.getName());
            if(player == null)
            {
                TownCommandUtility.futureMayorNotOnline(sender);
                return;
            }
            else if(!player.hasPermission("rccities.skill.mayor"))
            {
                TownCommandUtility.noMayorSkill(sender);
                return;
            }
        }

        try {
            Profession.changeProfession(selectedResident, args[2]);
            selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
            TownMessaging.sendTownResidents(selectedResident.getCity(), selectedResident.getName() + " ist nun " + Profession.translateProfession(selectedResident.getProfession()) + " von " + selectedResident.getCity().getName() + "!");
        } catch (UnknownProfessionException e) {
            RCMessaging.warn(sender, e.getMessage());
            RCMessaging.warn(sender, "Folgende Berufsgruppen gibt es:");
            RCMessaging.warn(sender, "mayor, vicemayor, assistant, resident");
        }

        //update region owners
        ChunkUtil.updatePlotOwner(resident.getCity());
    }
    
    public static void kick(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //no leadership
        if(!resident.isLeadership())
        {
            TownCommandUtility.noLeadership(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town kick <Spielername>' Kickt einen Spieler aus der Stadt.");
            return;
        }

        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            TownCommandUtility.selfAction(sender);
            return;
        }

        //focused player no resident
        Resident kickResident = ResidentHelper.isResident(args[1], resident.getCity());
        if(kickResident == null)
        {
            RCMessaging.warn(sender, "Der Spieler '" + args[1] + "' ist kein Bürger von " + resident.getCity().getName());
            return;
        }

        if(kickResident.isMayor())
        {
            RCMessaging.warn(sender, "Du kannst keinen Bürgermeister aus der Stadt werfen!");
            return;
        }

        //set city id = 0 & profession = 0
        kickResident.getCity().setId(0);
        kickResident.setProfession("");
        //save new city id = kick player
        TableHandler.get().getResidentTable().updateResident(kickResident);
        
        Player kickPlayer = Bukkit.getPlayerExact(kickResident.getName());
        if(kickPlayer != null)
            RCMessaging.warn(kickPlayer, "Du wurdest aus der Stadt '" + resident.getCity().getName() + "' geworfen!");
        TownMessaging.broadcast(RCMessaging.blue(resident.getName() + " hat " + kickResident.getName() + " aus " + resident.getCity().getName() + " geworfen!"));

        resident.getCity().setSize(resident.getCity().getSize()- MainConfig.getChunksPerPlayer());
        TableHandler.get().getCityTable().updateCity(resident.getCity());
        //update region owners
        ChunkUtil.updatePlotOwner(resident.getCity());

        //update public plots
        ChunkUtil.setPublic(resident.getCity());
    }
    
    public static void invite(CommandSender sender, String[] args)
    {

        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //no stafff
        if(!resident.isStaff())
        {
            TownCommandUtility.onlyStaff(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town invite <Spielername>' Läd einen Spieler in die Stadt ein.");
            return;
        }

        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            TownCommandUtility.selfAction(sender);
            return;
        }

        Player player = Bukkit.getPlayerExact(args[1]);
        if(player == null)
        {
            TownCommandUtility.noPlayerFound(sender);
            return;
        }

        if(ResidentHelper.isResident(player.getName(), resident.getCity()) != null)
        {
            TownCommandUtility.selectedAlreadyResident(sender);
            return;
        }

        invites.put(player.getName(), resident.getCity());
        RCMessaging.send(sender, RCMessaging.blue("Du hast " + player.getName() + " nach " + resident.getCity().getName() + " eingeladen!"), false);
        RCMessaging.send(player, RCMessaging.blue("Du wurdest von " + sender.getName() + " in die Stadt " + resident.getCity().getName() + " eingeladen!"), false);
        RCMessaging.send(player, RCMessaging.blue("Bestätige die Einladung mit /town accept"), false);
    }

    public static void setdesc(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //no stafff
        if(!resident.isMayor())
        {
            TownCommandUtility.noMayor(sender);
            return;
        }
        String newDesc = "";

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town setdesc <Beschreibung>' Ändert die Beschreibung der Stadt.");
            return;
        }

        for(int i = 1; i<args.length; i++)
        {
           newDesc += args[i]+" ";
        }

         City city = resident.getCity();
         city.setDescription(newDesc);
         TableHandler.get().getCityTable().updateCity(city);
         RCMessaging.send(sender, RCMessaging.blue("Die Beschreibung der Stadt wurde geändert!"), false);
    }
    
    public static void greetings(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //no leadership
        if(!resident.isLeadership())
        {
            TownCommandUtility.noLeadership(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town greetings <on / off>' Schaltet Plotnachrichten an oder aus.");
            return;
        }

        if(args[1].equalsIgnoreCase("on"))
        {
            //enable plot greetings & farewell messages
            resident.getCity().setGreetings(true);
            TableHandler.get().getCityTable().updateCity(resident.getCity());
            ChunkUtil.updateChunkMessages(resident.getCity());
            RCMessaging.send(sender, RCMessaging.blue("Plotnachrichten eingeschaltet!"), false);
        }
        else if(args[1].equalsIgnoreCase("off"))
        {
            //disable plot greetings & farwell messages
            resident.getCity().setGreetings(false);
            TableHandler.get().getCityTable().updateCity(resident.getCity());
            ChunkUtil.updateChunkMessages(resident.getCity());
            RCMessaging.send(sender, RCMessaging.blue("Plotnachrichten ausgeschaltet!"), false);
        }
        else
        {
            RCMessaging.warn(sender, "Unbekannter Parameter gefunden!");
            RCMessaging.warn(sender, "'/town greeting on' schaltet Plotnachrichten ein.");
            RCMessaging.warn(sender, "'/town greeting off' schaltet Plotnachrichten aus.");
        }
    }

    public static void withdraw(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }

        //no mayor
        if(!resident.isMayor())
        {
            TownCommandUtility.noMayor(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town withdraw <Betrag>' Hebt Coins aus der Stadtkasse ab.");
            return;
        }

        //wrong input
        double amount = Toolbox.isDouble(args[1]);
        if(amount == -1)
        {
            TownCommandUtility.wrongAmount(sender);
            return;
        }

        //not enough money
        if(!RCCitiesPlugin.get().getEconomy().has(resident.getCity().getBankAccount(), amount))
        {
            RCMessaging.warn(sender, "Es sind nicht genügend Coins in der Stadtkasse!");
            return;
        }

        //decrease town account
        RCCitiesPlugin.get().getEconomy().remove(resident.getCity().getBankAccount(), amount);

        //increase player account
        RCCitiesPlugin.get().getEconomy().add(sender.getName(), amount);

        //town message
        TownMessaging.sendTownResidents(resident.getCity(), RCMessaging.blue(resident.getName() + " hat " + amount + "c aus der Stadtkasse genommen!"));
    }

    public static void pvp(CommandSender sender, String[] args)
    {
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }

        //no leader
        if(!resident.isLeadership())
        {
            TownCommandUtility.noLeadership(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town pvp <on / off>' Schaltet PVP in der Stadt an oder aus.");
            return;
        }
        
        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(resident.getCity());
        
        if(args[1].equalsIgnoreCase("on"))
        {
            for(Plot plot : plotList)
            {
                WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
            }
            WorldGuardManager.save();
            TownMessaging.sendTownResidents(resident.getCity(), "PVP ist nun in der Stadt erlaubt!");
            //update chunk messages
            ChunkUtil.updateChunkMessages(resident.getCity());
            return;
        }

        if(args[1].equalsIgnoreCase("off"))
        {
            for(Plot plot : plotList)
            {
                if(!plot.isPvp())
                    WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
            }
            WorldGuardManager.save();
            TownMessaging.sendTownResidents(resident.getCity(), "PVP ist nun in in der Stadt verboten!");
            //update chunk messages
            ChunkUtil.updateChunkMessages(resident.getCity());
            return;
        }

        RCMessaging.warn(sender, "Unbekannter Parameter gefunden!");
        RCMessaging.warn(sender, "'/town pvp on' schaltet PVP in der Stadt ein.");
        RCMessaging.warn(sender, "'/town pvp off' schaltet PVP in der Stadt aus.");
    }

    public static void tnt(CommandSender sender, String[] args)
    {
        Player player = (Player)sender;
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }

        //no leader
        if(!resident.isLeadership())
        {
            TownCommandUtility.noLeadership(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town tnt <on / off>' Schaltet die TNT-Nutzung in der Stadt an oder aus.");
            return;
        }

        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(resident.getCity());

        if(args[1].equalsIgnoreCase("on"))
        {
            for(Plot plot : plotList)
            {
                WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.TNT, StateFlag.State.ALLOW);
            }
            WorldGuardManager.save();
            TownMessaging.sendTownResidents(resident.getCity(), "TNT ist nun in der Stadt erlaubt!");
            return;
        }

        if(args[1].equalsIgnoreCase("off"))
        {
            for(Plot plot : plotList)
            {
                WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.TNT, StateFlag.State.DENY);
            }
            WorldGuardManager.save();
            TownMessaging.sendTownResidents(resident.getCity(), "TNT ist nun in in der Stadt verboten!");
            return;
        }

        RCMessaging.warn(sender, "Unbekannter Parameter gefunden!");
        RCMessaging.warn(sender, "'/town tnt on' schaltet die TNT-Nutzung in der Stadt ein.");
        RCMessaging.warn(sender, "'/town tnt off' schaltet die TNT-Nutzung in der Stadt aus.");
    }
}
