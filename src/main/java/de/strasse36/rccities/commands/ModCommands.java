package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
import de.strasse36.rccities.util.ChunkUtil;
import de.strasse36.rccities.util.Profession;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:29
 * Description:
 */
public class ModCommands 
{
    private static Map<String, City> demolish = new HashMap<String, City>();

    public static void createCity(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.create"))
        {
            RCMessaging.noPermission(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 3)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town create <Stadtname> <Spielername>' Gründet eine Stadt und ernennt den Bürgermeister.");
            return;
        }

        City newCity = new City(args[1], ((Player)sender).getLocation());
        Player player = Bukkit.getPlayerExact(args[2]);
        if(player == null)
        {
            TownCommandUtility.noPlayerFound(sender);
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
            TownCommandUtility.internalError(sender);
            return;
        }

        RCMessaging.send(sender, RCMessaging.blue("Die Stadt '" + args[1] + "' wurde erfolgreich gegründet."), false);
        //set mayor
        Profession.setMayor(player, city);

        //create economy account
        RCCitiesPlugin.get().getEconomy().add("rccities_" + city.getName().toLowerCase(), 0);
    }

    public static void setMayor(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.mayor"))
        {
            RCMessaging.noPermission(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town mayor <Stadtname> [<Spielername>]' Ernennt den Bürgermeister einer Stadt.");
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
                TownCommandUtility.noPlayerFound(sender);
                return;
            }
        }

        City city = TableHandler.get().getCityTable().getCity(args[1]);
        //city not found
        if(city == null)
        {
            TownCommandUtility.noCityFound(sender);
            return;
        }
        //set mayor
        Profession.setMayor(player, city);

        //update region owners
        ChunkUtil.updatePlotOwner(city);

        //update public plots
        ChunkUtil.setPublic(city);

        RCMessaging.send(sender, RCMessaging.blue("Du hast " + player.getName() + " zum Bürgermeister von " + city.getName() + " ernannt!"), false);
    }
    
    public static void setCityName(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.setname"))
        {
            RCMessaging.noPermission(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 3)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town setname <Stadtname> <Neuer Name>' Ändert den Name einer Stadt.");
            return;
        }

        City city = TableHandler.get().getCityTable().getCity(args[1]);
        if(city == null)
        {
            TownCommandUtility.noCityFound(sender);
            return;
        }
        String oldName = city.getName();
        city.setName(args[3]);
        TableHandler.get().getCityTable().updateCity(city);
        RCMessaging.broadcast(RCMessaging.blue("Der Name der Stadt " + oldName + " wurde in " + city.getName() + " geändert!"));
    }

    public static void demolishCity(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.demolish"))
        {
            RCMessaging.noPermission(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town demolish <Stadtname>' Bereitet die Löschung einer Stadt vor.");
            return;
        }

        City city = TableHandler.get().getCityTable().getCity(args[1]);
        if(city == null)
        {
            TownCommandUtility.noCityFound(sender);
            return;
        }
        String captcha;

        String allowedChars ="0123456789abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        int max = allowedChars.length();
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<7; i++) {
            int value = random.nextInt(max);
            builder.append(allowedChars.charAt(value));
        }
        captcha = builder.toString();

        demolish.put(captcha, city);
        RCMessaging.warn(sender, "Um die Stadt " + city.getName() + " zu löschen, gebe folgendes ein:");
        RCMessaging.send(sender, RCMessaging.yellow("/town confirm " + captcha), false);
        RCMessaging.warn(sender, "Es werden dadurch alle Daten der Stadt gelöscht!");
        RCMessaging.warn(sender, "Darunter auch alle Plots, Spielerzuordnungen usw.");
        RCMessaging.warn(sender, "Das löschen einer Stadt ist nicht umkehrbar!");
    }

    public static void confirmDemolish(CommandSender sender, String[] args)
    {
        if(!sender.hasPermission("rccities.cmd.demolish"))
        {
            RCMessaging.noPermission(sender);
            return;
        }

        //wrong parameter length
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Nicht genügend Parameter gefunden!");
            RCMessaging.warn(sender, "'/town confirm <Captcha>' bestätigt die Löschung einer Stadt.");
            return;
        }

        if(!demolish.containsKey(args[1]))
        {
            RCMessaging.warn(sender, "Der eingegebene Captcha-Code ist falsch!");
            return;
        }
        City city = demolish.get(args[1]);
        
        //remove all plot assignments, plot entries and regions
        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);
        for(Plot plot : plotList)
        {
            WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(city.getSpawn().getWorld()).removeRegion(plot.getRegionId());
            TableHandler.get().getAssignmentsTable().deleteAssignment(plot);
            TableHandler.get().getPlotTable().deletePlot(plot.getRegionId());
        }
        WorldGuardManager.save();
        
        //remove city in database
        TableHandler.get().getCityTable().deleteCity(city.getId());

        //remove economy account
        RCCitiesPlugin.get().getEconomy().remove(city.getBankAccount(), RCCitiesPlugin.get().getEconomy().getBalance(city.getBankAccount()));

        RCMessaging.broadcast(RCMessaging.blue("Die Stadt " + city.getName() + " wurde gelöscht!"));
    }
}
