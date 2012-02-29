package de.strasse36.rccities.commands;

import com.silthus.raidcraft.database.UnknownTableException;
import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.util.Profession;
import de.strasse36.rccities.util.TableNames;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class ModeratorCommands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        //create city and nominate mayor
        if(args.length > 2 && args[0].equals("create"))
        {
            if(!sender.hasPermission("rccities.cmd.create"))
            {
                RCMessaging.noPermission(sender);
                return true;
            }
            City newCity = new City(args[1]);
            Player player = Bukkit.getPlayerExact(args[2]);
            if(player == null)
            {
                CommandUtility.noPlayerFound(sender);
                return true;
            }
            Profession.setMayor(player, newCity);
            RCMessaging.send(sender, "Die Stadt '" + args[1] + "' wurde erfolgreich gegründet.");
            return true;
        }

        //promote player as mayor
        if(args.length > 1 && args[0].equals("mayor"))
        {
            if(!sender.hasPermission("rccities.cmd.mayor"))
            {
                RCMessaging.noPermission(sender);
                return true;
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
                   return true;
               }
            }
            try {
                City city = (City)RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getCityTable());
                Profession.setMayor(player, city);
            } catch (UnknownTableException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return true;
        }
        return false;
    }

}
