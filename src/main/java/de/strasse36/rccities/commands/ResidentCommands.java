package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.Teleport;
import de.strasse36.rccities.util.Toolbox;
import de.strasse36.rccities.util.TownMessaging;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:27
 * Description:
 */
public class ResidentCommands {

    public static void showTownInfo(CommandSender sender)
    {
        //TODO
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
