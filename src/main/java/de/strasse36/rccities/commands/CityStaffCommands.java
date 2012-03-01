package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.ResidentHelper;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.TownMessaging;
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
        if(resident == null || resident.getCity() == null)
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
    
    public static void kickPlayer(CommandSender sender, String[] args)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
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
        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            CommandUtility.selfAction(sender);
            //TODO debug!
            //return;
        }

        //focused player no resident
        Resident kickResident = ResidentHelper.isResident(args[1], resident.getCity());
        if(kickResident == null)
        {
            RCMessaging.warn(sender, "Der Spieler '" + args[1] + "' ist kein Bürger von " + resident.getCity().getName());
            return;
        }
        //set city id = 0 & profession = 0
        kickResident.getCity().setId(0);
        kickResident.setProfession("");
        //save new city id = kick player
        TableHandler.get().getResidentTable().updateResident(kickResident);
        
        Player kickPlayer = Bukkit.getPlayerExact(kickResident.getName());
        if(kickPlayer != null)
            RCMessaging.send(kickPlayer, RCMessaging.red("Du wurdest aus der Stadt '" + resident.getCity().getName() + "' geworfen!") );
        TownMessaging.sendTownResidents(resident.getCity(), resident.getName() + " hat " + kickResident.getName() + " aus " + resident.getCity().getName() + "geworfen!");
    }
    
    public static void invitePlayer(CommandSender sender, String[] args)
    {

        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
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
        if(sender.getName().equalsIgnoreCase(args[1]))
        {
            CommandUtility.selfAction(sender);
            //TODO debug!
            //return;
        }

        Player player = Bukkit.getPlayerExact(args[1]);
        if(player == null)
        {
            CommandUtility.noPlayerFound(sender);
            return;
        }

        invites.put(player.getName(), resident.getCity());
        RCMessaging.send(sender, "Du hast " + player.getName() + " nach " + resident.getCity().getName() + "eingeladen!");
        RCMessaging.send(player, RCMessaging.blue("Du wurdest von " + sender.getName() + " in die Stadt " + resident.getCity().getName() + " eingeladen!"));
        RCMessaging.send(player, RCMessaging.blue("Bestätige die Einladung mit /town accept"));
    }
}
