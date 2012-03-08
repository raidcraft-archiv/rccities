package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 08.03.12 - 14:05
 * Description:
 */
public class PlotCommandUtility {

    public static void unclaimable(CommandSender sender)
    {
        RCMessaging.warn(sender, "Dieser Chunk kann nicht geclaimed werden! - Bereits geclamied?");
    }
}
