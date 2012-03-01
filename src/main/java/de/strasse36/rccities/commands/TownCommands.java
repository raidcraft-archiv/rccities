package de.strasse36.rccities.commands;

import com.silthus.raidcraft.database.UnknownTableException;
import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.database.ResidentTable;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
import de.strasse36.rccities.util.Profession;
import de.strasse36.rccities.util.TableGetter;
import de.strasse36.rccities.util.TableNames;
import de.strasse36.rccities.util.Teleport;
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
public class TownCommands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        //show help
        if(args.length == 0)
        {
            CommandUtility.commandHelp(sender);
            return true;
        }

        //create city and nominate mayor
        if(args.length > 2 && args[0].equals("create"))
        {
            if(!sender.hasPermission("rccities.cmd.create"))
            {
                RCMessaging.noPermission(sender);
                return true;
            }
            City newCity = new City(args[1], ((Player)sender).getLocation());
            Player player = Bukkit.getPlayerExact(args[2]);
            if(player == null)
            {
                CommandUtility.noPlayerFound(sender);
                return true;
            }

            try {
                try {
                    TableGetter.getCityTable().newCity(newCity);
                } catch (AlreadyExistsException e) {
                    RCMessaging.warn(sender, e.getMessage());
                    return true;
                }
                City city = TableGetter.getCityTable().getCity(args[2]);
                Profession.setMayor(player, city);
            } catch (UnknownTableException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                City city = ((CityTable)RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getCityTable())).getCity(args[2]);
                Profession.setMayor(player, city);
            } catch (UnknownTableException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return true;
        }

        //teleport player to townspawn
        if(args.length > 0 && args[0].equals("spawn"))
        {
            City city;
            if(args.length > 1 && sender.hasPermission("rccities.cmd.spawnall"))
            {
                try {
                    city = ((CityTable)RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getCityTable())).getCity(args[1]);
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

            Teleport.teleportPlayer((Player) sender, city);
            RCMessaging.send(sender, "Willkommen am Townspawn von " + city.getName());
            return true;
        }

        //mayor set townspawn
        if(args.length > 1 && args[0].equals("set") && args[1].equals("spawn"))
        {
            Resident resident;
            try {
                resident = TableGetter.getResidentTable().getResident(sender.getName());
            } catch (UnknownTableException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                CommandUtility.internalError(sender);
                return true;
            }
            if(!resident.isMayor())
            {
                CommandUtility.noMayor(sender);
                return true;
            }

            resident.getCity().setSpawn(((Player)sender).getLocation());

            try {
                TableGetter.getCityTable().updateCity(resident.getCity());
            } catch (UnknownTableException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                CommandUtility.internalError(sender);
                return true;
            }        
        }

        return false;
    }

}
