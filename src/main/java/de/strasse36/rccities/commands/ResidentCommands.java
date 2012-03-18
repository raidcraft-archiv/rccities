package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import com.silthus.raidcraft.util.Task;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.config.MainConfig;
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

    public static void showTownInfo(CommandSender sender, String[] args)
    {
        City selectedCity;
        if(args.length == 0)
        {
            Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
            selectedCity = resident.getCity();
            //no resident
            if(resident == null || resident.getCity() == null)
            {
                TownCommandUtility.noResident(sender);
                return;
            }
        }
        else
        {
            selectedCity = TableHandler.get().getCityTable().getCity(args[1]);
            if(selectedCity == null)
            {
                TownCommandUtility.noCityFound(sender);
            }
        }

        String mayors = "", vicemayors = "", assistants = "", residents = "";
        int n_mayors = 0, n_vicemayors = 0, n_assistants = 0, n_residents = 0;

        List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(selectedCity);
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
        RCMessaging.send(sender, RCMessaging.green("Stadtinformationen für: ") + selectedCity.getName(), false);
        RCMessaging.send(sender, RCMessaging.green("Beschreibung: ") + selectedCity.getDescription(), false);
        RCMessaging.send(sender, RCMessaging.green("Stadtkasse: ") + RCCitiesPlugin.get().getEconomy().getBalance(selectedCity.getBankAccount()) + "c", false);
        RCMessaging.send(sender, RCMessaging.green("Steuerabgaben: ") + TableHandler.get().getPlotTable().getPlots(selectedCity).size()*MainConfig.getTaxAmount() + "c", false);
        RCMessaging.send(sender, RCMessaging.green("Chunks: ") + TableHandler.get().getPlotTable().getPlots(selectedCity).size() + "/" + selectedCity.getSize() + " claimed", false);
        RCMessaging.send(sender, RCMessaging.green("Bürgermeister (" + n_mayors + "): ") + mayors, false);
        if(n_vicemayors > 0)
            RCMessaging.send(sender, RCMessaging.green("Vize-Bürgermeister (" + n_vicemayors + "): ") + vicemayors, false);
        if(n_assistants > 0)
            RCMessaging.send(sender, RCMessaging.green("Assistenten (" + n_assistants + "): ") + assistants, false);
        if(n_residents > 0)
            RCMessaging.send(sender, RCMessaging.green("Einwohner (" + n_residents + "): ") + residents, false);
    }

    public static void townspawn(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
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
            TownCommandUtility.noCityFound(sender);
            return;
        }
        
        //warmup
        int warmup = 0;
        if(!sender.hasPermission("rccities.cmd.spawnall"))
            warmup = MainConfig.getTownspawnWarmup();
        RCMessaging.send(sender, RCMessaging.blue("Warten auf Teleport..."), false);
        Task task = new Task(RCCitiesPlugin.get(), (Player)sender, city)
        {
            @Override
            public void run()
            {
                final Player player = (Player)getArg(0);
                final City city = (City)getArg(1);
                Teleport.teleportPlayer(player, city);
                RCMessaging.send(player, RCMessaging.blue("Willkommen am Townspawn von '" + city.getName() + "'"), false);
            }
        };
        task.startDelayed(warmup*20);
        
    }

    public static void leaveTown(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return;
        }
        //mayor
        if(resident.isMayor())
        {
            RCMessaging.warn(sender, "Du kannst als Bürgermeister nicht die Stadt verlassen!");
            RCMessaging.warn(sender, "Dekradiere Dich mit '/town promote " + sender.getName() +" resident' zum Bürger und versuche es nocheinmal.");
            return;
        }
        City city = resident.getCity();
        resident.getCity().setId(0);
        resident.setProfession("");
        TableHandler.get().getResidentTable().updateResident(resident);
        TownMessaging.sendTownResidents(city, sender.getName() + " hat die Stadt " + city.getName() + " verlassen!");
        RCMessaging.send(sender, RCMessaging.blue("Du hast die Stadt " + city.getName() + " verlassen!"), false);

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
            TownCommandUtility.noResident(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town deposit <Betrag>' Überweist Coins in die Stadtkasse.");
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
