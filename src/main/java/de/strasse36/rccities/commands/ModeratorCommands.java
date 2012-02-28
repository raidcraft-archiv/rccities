package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.util.Profession;
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
        if(args.length == 0)
        {

            return true;
        }

        //create city and nominate mayor
        if(args.length == 3 && args[0].equals("create"))
        {
            City newCity = new City(args[1]);
            Player player = Bukkit.getPlayerExact(args[2]);
            Profession.setMayor(player, newCity);
            RCMessaging.send(sender, "Die Stadt '" + args[1] + "' wurde erfolgreich geründet.");

        }



        return false;
    }

}
