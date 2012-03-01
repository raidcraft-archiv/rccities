package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.TableHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:22
 * Description:
 */
public class CityStaffCommands {

    public static Map<String, City> invites = new HashMap<String, City>();


    public static void setTownSpawn(CommandSender sender)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null)
        {
            CommandUtility.noResident(sender);
            return;
        }
        //no mayor
        if(!resident.isMayor())
        {
            CommandUtility.noMayor(sender);
            return;
        }

        resident.getCity().setSpawn(((Player)sender).getLocation());
        TableHandler.get().getCityTable().updateCity(resident.getCity());
        RCMessaging.send(sender, "Der Townspawn von " + resident.getCity().getName() + " wurde erfolgreich verlegt!");
        return;
    }
    
    public static void invitePlayer(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null)
        {
            CommandUtility.noResident(sender);
            return;
        }
        //no stafff
        if(!resident.isStaff())
        {
            CommandUtility.onlyStaff(sender);
            return;
        }

        Player player = Bukkit.getPlayerExact(args[1]);
        if(player == null)
        {
            CommandUtility.noPlayerFound(sender);
            return;
        }

        invites.put(player.getName(), resident.getCity());
        RCMessaging.send(sender, "Du hast " + player.getName() + " nach " + resident.getCity().getName() + "eingeladen!");
        RCMessaging.send(player, RCMessaging.blue("Du wurdest von " + sender.getName() + " in die Stadt " + resident.getCity().getName() + "eingeladen!"));
        RCMessaging.send(player, RCMessaging.blue("Bestätige die Einladung mit /town accept"));
    }
}
