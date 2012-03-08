package de.strasse36.rccities.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class PlotCommandAllocater implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        /////////////////////////////RESIDENTS//////////////////////////////////////////
        //show plot info
        if(args.length == 0)
        {
            PlotCommands.showPlotInfo(sender);
            return true;
        }

        /////////////////////////////CITY-STAFF//////////////////////////////////////////
        if(args.length > 0 && args[0].equals("claim"))
        {
            PlotCommands.claim(sender);
            return true;
        }
        return false;
    }

}
