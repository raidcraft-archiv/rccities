package de.strasse36.rccities.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class PublicCommands implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length == 0)
        {
            CommandUtility.commandHelp(sender);
            return true;
        }
        return false;
    }

}
