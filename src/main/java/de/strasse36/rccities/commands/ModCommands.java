package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
import de.strasse36.rccities.util.Profession;
import de.strasse36.rccities.util.TableHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:29
 * Description:
 */
public class ModCommands {

    public static void createCity(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.create"))
        {
            RCMessaging.noPermission(sender);
            return;
        }
        City newCity = new City(args[1], ((Player)sender).getLocation());
        Player player = Bukkit.getPlayerExact(args[2]);
        if(player == null)
        {
            CommandUtility.noPlayerFound(sender);
            return;
        }

        //create city
        try {
            TableHandler.get().getCityTable().newCity(newCity);
        } catch (AlreadyExistsException e) {
            RCMessaging.warn(sender, e.getMessage());
            return;
        }

        City city = TableHandler.get().getCityTable().getCity(args[1]);
        //city was not created
        if(city == null)
        {
            CommandUtility.internalError(sender);
            return;
        }

        RCMessaging.send(sender, "Die Stadt '" + args[1] + "' wurde erfolgreich gegründet.");
        //set mayor
        Profession.setMayor(player, city);
    }

    public static void setMayor(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.mayor"))
        {
            RCMessaging.noPermission(sender);
            return;
        }
        Player player;
        if(args.length < 3)
        {
            player = (Player)sender;
        }
        else
        {
            player = Bukkit.getPlayer(args[2]);
            if(player == null)
            {
                CommandUtility.noPlayerFound(sender);
                return;
            }
        }

        City city = TableHandler.get().getCityTable().getCity(args[1]);
        //city not found
        if(city == null)
        {
            CommandUtility.noCityFound(sender);
            return;
        }
        //set mayor
        Profession.setMayor(player, city);
    }
}
