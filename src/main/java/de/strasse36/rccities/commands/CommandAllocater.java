package de.strasse36.rccities.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class CommandAllocater implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        //show town info
        if(args.length == 0)
        {
            ResidentCommands.showTownInfo(sender);
            return true;
        }

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

        //teleport player to townspawn
        if(args.length > 0 && args[0].equals("spawn"))
        {
            ResidentCommands.teleportToTownspawn(sender, args);
            return true;
        }

        //mayor set townspawn
        if(args.length > 1 && args[0].equals("set") && args[1].equals("spawn"))
        {
            CityStaffCommands.setTownSpawn(sender);
            return true;
        }

        //mayor & assistants invite player
        if(args.length > 1 && args[0].equals("invite"))
        {
            CityStaffCommands.invitePlayer(sender, args);
            return true;
        }

        //mayor & assistants invite player
        if(args.length > 0 && args[0].equals("accept"))
        {
            NonResidentCommands.acceptTownInvite(sender);
            return true;
        }

        return false;
    }

}
