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
public class PlotCommandAllocater implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        /////////////////////////////RESIDENTS//////////////////////////////////////////
        //mark plot info
        if(args.length == 0)
        {
            PlotCommands.showPlotInfo(sender);
            return true;
        }

        if(args.length > 0)
        {
            /////////////////////////////CITY-STAFF//////////////////////////////////////////
            if(args[0].equals("claim"))
            {
                PlotCommands.claim(sender);
                return true;
            }

            if(args[0].equals("unclaim"))
            {
                PlotCommands.unclaim(sender);
                return true;
            }

            if(args[0].equals("give"))
            {
                PlotCommands.give(sender, args);
                return true;
            }

            if(args[0].equals("take"))
            {
                PlotCommands.take(sender, args);
                return true;
            }

            if(args[0].equals("buy"))
            {
                PlotCommands.buy(sender, args);
                return true;
            }

            if(args[0].equals("clear"))
            {
                PlotCommands.clear(sender, args);
                return true;
            }

            if(args[0].equals("pvp"))
            {
                PlotCommands.pvp(sender, args);
                return true;
            }

            if(args[0].equals("public"))
            {
                PlotCommands.publicPlot(sender, args);
                return true;
            }

            if(args[0].equals("mark"))
            {
                PlotCommands.mark(sender);
                return true;
            }

            if(args[0].equals("unmark"))
            {
                PlotCommands.unmark(sender);
                return true;
            }

            /////////////////////////////RESIDENTS///////////////////////////////////////
            if(args[0].equals("list"))
            {
                PlotCommands.list(sender, args);
                return true;
            }

            /////////////////////////////OTHERS//////////////////////////////////////////
            //help
            if(args[0].equals("help"))
            {
                PlotCommandUtility.help(sender);
                return true;
            }

        }
        RCMessaging.warn(sender, "Der eigengebene Befehl konnte nicht zugeordnet werden!");
        RCMessaging.warn(sender, "'/plot help' zeigt alle verfügbaren Befehle an!");
        return true;
    }

}
