package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.Teleport;
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
            CommandUtility.noCityFound(sender);
            return;
        }
        Teleport.teleportPlayer((Player) sender, city);
        RCMessaging.send(sender, "Willkommen am Townspawn von " + city.getName());
    }
}
