package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.util.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:27
 * Description:
 */
public class ResidentCommands {

    public static void showTownInfo(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }

        String mayors = "", vicemayors = "", assistants = "", residents = "";
        int n_mayors = 0, n_vicemayors = 0, n_assistants = 0, n_residents = 0;

        List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(resident.getCity());
        for(Resident res : residentList)
        {
            if(res.isMayor())
            {
                if(mayors.length()>0)
                    mayors += ", ";
                mayors += res.getName();
                n_mayors++;
            }
            else if(res.isViceMayor())
            {
                if(vicemayors.length()>0)
                    vicemayors += ", ";
                vicemayors += res.getName();
                n_vicemayors++;
            }
            else if(res.isAssistant())
            {
                if(assistants.length()>0)
                    assistants += ", ";
                assistants += res.getName();
                n_assistants++;
            }
            else
            {
                if(residents.length()>0)
                    residents += ", ";
                residents += res.getName();
                n_residents++;
            }
        }

        RCMessaging.send(sender, RCMessaging.green("--- RCCities Stadtinfo ---"), false);
        RCMessaging.send(sender, RCMessaging.green("Stadtinformationen für: ") + resident.getCity().getName(), false);
        RCMessaging.send(sender, RCMessaging.green("Beschreibung: ") + resident.getCity().getDescription(), false);
        RCMessaging.send(sender, RCMessaging.green("Stadtkasse: ") + RCCitiesPlugin.get().getEconomy().getBalance(resident.getCity().getBankAccount()) + "c", false);
        RCMessaging.send(sender, RCMessaging.green("Chunks: ") + TableHandler.get().getPlotTable().getPlots(resident.getCity()).size() + "/" + resident.getCity().getSize() + " claimed", false);
        RCMessaging.send(sender, RCMessaging.green("Bürgermeister (" + n_mayors + "): ") + mayors, false);
        if(n_vicemayors > 0)
            RCMessaging.send(sender, RCMessaging.green("Vize-Bürgermeister (" + n_vicemayors + "): ") + vicemayors, false);
        if(n_assistants > 0)
            RCMessaging.send(sender, RCMessaging.green("Assistenten (" + n_assistants + "): ") + assistants, false);
        if(n_residents > 0)
            RCMessaging.send(sender, RCMessaging.green("Einwohner (" + n_residents + "): ") + residents, false);
    }

    public static void teleportToTownspawn(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        City city;
        if(args.length > 1 && sender.hasPermission("rccities.cmd.spawnall"))
        {
            city = TableHandler.get().getCityTable().getCity(args[1]);
        }
        else
        {
            city = TableHandler.get().getResidentTable().getResident(sender.getName()).getCity();
        }
        //city not found
        if(city == null)
        {
            RCCitiesCommandUtility.noCityFound(sender);
            return;
        }
        Teleport.teleportPlayer((Player) sender, city);
        RCMessaging.send(sender, RCMessaging.blue("Willkommen am Townspawn von " + city.getName()));
    }

    public static void leaveTown(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
            return;
        }
        //mayor
        if(resident.isMayor())
        {
            RCMessaging.warn(sender, "Du kannst als Bürgermeister nicht die Stadt verlassen!");
            RCMessaging.warn(sender, "Dekradiere Dich mit '/town promote " + sender.getName() +" resident' zum einfachen Bürger und versuche es nocheinmal.");
            return;
        }
        City city = resident.getCity();
        resident.getCity().setId(0);
        resident.setProfession("");
        TableHandler.get().getResidentTable().updateResident(resident);
        TownMessaging.sendTownResidents(city, sender.getName() + " hat die Stadt " + city.getName() + " verlassen!");
        RCMessaging.send(sender, RCMessaging.blue("Du hast die Stadt " + city.getName() + " verlassen!"));

        //update region owners
        ChunkUtil.updatePlotOwner(resident.getCity());

        //update public plots
        ChunkUtil.setPublic(resident.getCity());
    }
    
    public static void deposit(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            RCCitiesCommandUtility.noResident(sender);
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
        if(!RCCitiesPlugin.get().getEconomy().has(resident.getName(), amount))
        {
            RCMessaging.warn(sender, "Du hast nicht genügend Coins auf dem Konto!");
            return;
        }

        //decrease player account
        RCCitiesPlugin.get().getEconomy().remove(sender.getName(), amount);

        //increase town account
        RCCitiesPlugin.get().getEconomy().add(resident.getCity().getBankAccount(), amount);

        //town message
        TownMessaging.sendTownResidents(resident.getCity(), RCMessaging.blue(resident.getName() + " hat " + amount + "c in die Stadtkasse eingezahlt!"));
    }
}
