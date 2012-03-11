package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
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


    public static void setTownSpawn(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            RCCitiesCommandUtility.noMayor(sender);
            return;
        }

        if(((Player)sender).getWorld() != resident.getCity().getSpawn().getWorld())
        {
            RCCitiesCommandUtility.wrongWorld(sender);
            return;
        }

        resident.getCity().setSpawn(((Player)sender).getLocation());
        TableHandler.get().getCityTable().updateCity(resident.getCity());
        RCMessaging.send(sender, RCMessaging.blue("Der Townspawn von " + resident.getCity().getName() + " wurde erfolgreich verlegt!"));
        return;
    }

    public static void promote(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            RCCitiesCommandUtility.noMayor(sender);
            return;
        }

        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            if(args[2].equalsIgnoreCase("mayor"))
            {
            RCCitiesCommandUtility.selfAction(sender);
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
            RCCitiesCommandUtility.selectNoResident(sender);
            return;
        }

        try {
            Profession.changeProfession(selectedResident, args[2]);
            selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
            TownMessaging.sendTownResidents(selectedResident.getCity(), selectedResident.getName() + " ist nun " + Profession.translateProfession(selectedResident.getProfession()) + " von " + selectedResident.getCity().getName() + "!");
        } catch (UnknownProfessionException e) {
            RCMessaging.warn(sender, e.getMessage());
            RCMessaging.warn(sender, "Folgende Berufsgruppen gibt es:");
            RCMessaging.warn(sender, "mayor, vicemayor, assistant, gardener, resident");
        }

        //update region owners
        ChunkUtil.updatePlotOwner(resident.getCity());
    }
    
    public static void kickPlayer(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            RCCitiesCommandUtility.noMayor(sender);
            return;
        }
        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            RCCitiesCommandUtility.selfAction(sender);
            return;
        }

        //focused player no resident
        Resident kickResident = ResidentHelper.isResident(args[1], resident.getCity());
        if(kickResident == null)
        {
            RCMessaging.warn(sender, "Der Spieler '" + args[1] + "' ist kein Bürger von " + resident.getCity().getName());
            return;
        }
        //set city id = 0 & profession = 0
        kickResident.getCity().setId(0);
        kickResident.setProfession("");
        //save new city id = kick player
        TableHandler.get().getResidentTable().updateResident(kickResident);
        
        Player kickPlayer = Bukkit.getPlayerExact(kickResident.getName());
        if(kickPlayer != null)
            RCMessaging.send(kickPlayer, RCMessaging.red("Du wurdest aus der Stadt '" + resident.getCity().getName() + "' geworfen!") );
        TownMessaging.sendTownResidents(resident.getCity(), RCMessaging.blue(resident.getName() + " hat " + kickResident.getName() + " aus " + resident.getCity().getName() + "geworfen!"));

        //update region owners
        ChunkUtil.updatePlotOwner(resident.getCity());

        //update public plots
        ChunkUtil.setPublic(resident.getCity());
    }
    
    public static void invitePlayer(CommandSender sender, String[] args)
    {

        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no stafff
        if(!resident.isStaff())
        {
            RCCitiesCommandUtility.onlyStaff(sender);
            return;
        }
        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            RCCitiesCommandUtility.selfAction(sender);
            return;
        }

        Player player = Bukkit.getPlayerExact(args[1]);
        if(player == null)
        {
            RCCitiesCommandUtility.noPlayerFound(sender);
            return;
        }

        invites.put(player.getName(), resident.getCity());
        RCMessaging.send(sender, "Du hast " + player.getName() + " nach " + resident.getCity().getName() + " eingeladen!");
        RCMessaging.send(player, RCMessaging.blue("Du wurdest von " + sender.getName() + " in die Stadt " + resident.getCity().getName() + " eingeladen!"));
        RCMessaging.send(player, RCMessaging.blue("Bestätige die Einladung mit /town accept"));
    }

    public static void setCityDescription(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no stafff
        if(!resident.isMayor())
        {
            RCCitiesCommandUtility.noMayor(sender);
            return;
        }
        String newDesc = "";
        
       for(int i = 1; i<args.length; i++)
       {
           newDesc += args[i]+" ";
       }
        
        City city = resident.getCity();
        city.setDescription(newDesc);
        TableHandler.get().getCityTable().updateCity(city);
        RCMessaging.send(sender, RCMessaging.blue("Die Beschreibung der Stadt wurde geändert!"));
    }
    
    public static void greetings(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            RCCitiesCommandUtility.noMayor(sender);
            return;
        }


        if(args[1].equalsIgnoreCase("on"))
        {
            //enable plot greetings & farewell messages
            resident.getCity().setGreetings(true);
            TableHandler.get().getCityTable().updateCity(resident.getCity());
            ChunkUtil.updateChunkMessages(resident.getCity());
            RCMessaging.send(sender, RCMessaging.blue("Plotnachrichten eingeschaltet!"));
        }
        else if(args[1].equalsIgnoreCase("off"))
        {
            //disable plot greetings & farwell messages
            resident.getCity().setGreetings(false);
            TableHandler.get().getCityTable().updateCity(resident.getCity());
            ChunkUtil.updateChunkMessages(resident.getCity());
            RCMessaging.send(sender, RCMessaging.blue("Plotnachrichten ausgeschaltet!"));
        }
        else
        {
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
            RCCitiesCommandUtility.noResident(sender);
            return;
        }

        //no mayor
        if(!resident.isMayor())
        {
            RCCitiesCommandUtility.noMayor(sender);
            return;
        }

        //wrong input
        double amount = Toolbox.isDouble(args[1]);
        if(amount == -1)
        {
            RCCitiesCommandUtility.wrongAmount(sender);
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
            RCCitiesCommandUtility.noResident(sender);
            return;
        }

        //no leader
        if(!resident.isLeadership())
        {
            RCCitiesCommandUtility.noLeadership(sender);
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
            return;
        }

        RCMessaging.warn(sender, "'/plot pvp on' schaltet PVP in diesem Chunk ein.");
        RCMessaging.warn(sender, "'/plot pvp off' schaltet PVP in diesem Chunk aus.");
    }

}
