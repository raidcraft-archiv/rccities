package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class RCCitiesCommandAllocater implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        /////////////////////////////RESIDENTS//////////////////////////////////////////
        //show town info
        if(args.length == 0)
        {
            ResidentCommands.showTownInfo(sender);
            return true;
        }

        //leave town
        if(args.length > 0 && args[0].equals("leave"))
        {
            ResidentCommands.leaveTown(sender);
            return true;
        }

        //teleport player to townspawn
        if(args.length > 0 && args[0].equals("spawn"))
        {
            ResidentCommands.teleportToTownspawn(sender, args);
            return true;
        }

        //deposit into townbank
        if(args.length > 1 && args[0].equals("deposit"))
        {
            ResidentCommands.deposit(sender, args);
            return true;
        }

        //withdraw from townbank
        if(args.length > 1 && args[0].equals("withdraw"))
        {
            CityStaffCommands.withdraw(sender, args);
            return true;
        }

        /////////////////////////////CITY-STAFF//////////////////////////////////////////
        //mayor set townspawn
        if(args.length > 0 && args[0].equals("setspawn"))
        {
            CityStaffCommands.setTownSpawn(sender);
            return true;
        }

        //promote residents
        if(args.length > 2 && args[0].equals("promote"))
        {
            CityStaffCommands.promote(sender, args);
            return true;
        }

        //kick player from town
        if(args.length > 1 && args[0].equals("kick"))
        {
            CityStaffCommands.kickPlayer(sender, args);
            return true;
        }

        //mayor & assistants invite player
        if(args.length > 1 && args[0].equals("invite"))
        {
            CityStaffCommands.invitePlayer(sender, args);
            return true;
        }

        //change city description
        ///town set desc <description>
        if(args.length > 1 && args[0].equals("setdesc"))
        {
            CityStaffCommands.setCityDescription(sender, args);
            return true;
        }

        //show region farewell & greetings
        if(args.length > 1 && args[0].equals("greetings"))
        {
            CityStaffCommands.greetings(sender, args);
            return true;
        }

        //pvp
        if(args.length > 1 && args[0].equals("pvp"))
        {
            CityStaffCommands.pvp(sender, args);
            return true;
        }

        /////////////////////////////MODERATOR//////////////////////////////////////////
        //create city and nominate mayor
        if(args.length > 2 && args[0].equals("create"))
        {
            ModCommands.createCity(sender, args);
            return true;
        }

        //promote player as mayor
        if(args.length > 1 && args[0].equals("mayor"))
        {
            ModCommands.setMayor(sender, args);
            return true;
        }

        //change city name
        ///town set name <city> <name>
        if(args.length > 2 && args[0].equals("setname"))
        {
            ModCommands.setCityName(sender, args);
            return true;
        }

        //demolish city
        if(args.length > 1 && args[0].equals("demolish"))
        {
            ModCommands.demolishCity(sender, args);
            return true;
        }

        //confirm demolish city
        if(args.length > 1 && args[0].equals("confirm"))
        {
            ModCommands.confirmDemolish(sender, args);
            return true;
        }

        /////////////////////////////NON-RESIDENTS//////////////////////////////////////////
        //mayor & assistants invite player
        if(args.length > 0 && args[0].equals("accept"))
        {
            NonResidentCommands.acceptTownInvite(sender);
            return true;
        }

        //list all towns
        if(args.length > 0 && args[0].equals("list"))
        {
            NonResidentCommands.listTowns(sender);
            return true;
        }

        RCMessaging.warn(sender, "Der eigengebene Befehl konnte nicht zugeordnet werden!");
        RCMessaging.warn(sender, "'/town help' zeigt alle verfügbaren Befehle an!");
        return true;
    }

}
