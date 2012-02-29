package de.strasse36.rccities.commands;

import com.silthus.raidcraft.database.UnknownTableException;
import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.database.ResidentTable;
import de.strasse36.rccities.util.TableNames;
import de.strasse36.rccities.util.Teleport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class ResidentCommands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        //teleport player to town spawn
        if(args.length > 1 && args[0].equals("spawn"))
        {
            City city;
            if(args.length > 1 && sender.hasPermission("rccities.cmd.spawnall"))
            {
                try {
                    city = ((CityTable)RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getResidentTable())).getCity(args[1]);
                } catch (UnknownTableException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    CommandUtility.noCityFound(sender);
                    return true;
                }
            }
            else
            {
                try {
                    city = ((ResidentTable)RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getResidentTable())).getResident(sender.getName()).getCity();
                } catch (UnknownTableException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    CommandUtility.noResident(sender);
                    return true;
                }
            }

            Teleport.teleportPlayer((Player)sender, city);
            RCMessaging.send(sender, "Willkommen am Townspawn von " + city.getName());
            return true;
        }
        return false;
    }

}
