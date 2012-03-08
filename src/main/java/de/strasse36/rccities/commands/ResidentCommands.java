package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.Teleport;
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
        RCMessaging.send(sender, "Willkommen am Townspawn von " + city.getName());
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
        RCMessaging.send(sender, "Du hast die Stadt " + city.getName() + " verlassen!");
    }
}
