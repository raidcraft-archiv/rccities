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

        if(args.length > 0 && args[0].equals("unclaim"))
        {
            PlotCommands.unclaim(sender);
            return true;
        }

        if(args.length > 1 && args[0].equals("give"))
        {
            PlotCommands.give(sender, args);
            return true;
        }

        if(args.length > 1 && args[0].equals("take"))
        {
            PlotCommands.take(sender, args);
            return true;
        }

        if(args.length > 1 && args[0].equals("buy"))
        {
            PlotCommands.buy(sender, args);
            return true;
        }

        if(args.length > 0 && args[0].equals("clear"))
        {
            PlotCommands.clear(sender, args);
            return true;
        }

        RCMessaging.warn(sender, "Der eigengebene Befehl konnte nicht zugeordnet werden!");
        RCMessaging.warn(sender, "Falsche Anzahl an Parameter?");
        RCMessaging.warn(sender, "Befehlt falsch geschrieben?");
        RCMessaging.warn(sender, "'/plot help' zeigt alle verfügbaren Befehle!");
        return true;
    }

}
